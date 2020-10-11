package testEFR;

import efr.pagesEFR.BaseClass;
import efr.pagesEFR.ClientsPage;
import efr.pagesEFR.CreditPage;
import efr.pagesEFR.MainPage;
import efr.testDataClient.TestClient;
import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utils.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@Component("RefCredit")
@DisplayName("Рефинансирование")
public class RefCredit extends TestUtils {

    @Rule
    public JUnitUtils jUnitUtils;

    private DriverUtils driverUtils;

    private DataProvider env;

    private Logger logger;

    private CommonUtils commonUtils;

    private BaseClass baseClass;

    private BaseTest baseTest;

    private LoanTools loanTools;

    private final String contractType = "Потребительский кредит";

    @Autowired
    public void setLoanTools(LoanTools loanTools) { this.loanTools = loanTools; }

    @Autowired
    public void setDriverUtils(DriverUtils driverUtils) {
        this.driverUtils = driverUtils;
    }

    @Autowired
    public void setEnv(DataProvider env) {
        this.env = env;
    }

    @Autowired
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    @Autowired
    public void setCommonUtils(CommonUtils commonUtils) {
        this.commonUtils = commonUtils;
    }

    @Autowired
    public void setBaseClass(BaseClass baseClass) {
        this.baseClass = baseClass;
    }

    @Autowired
    public void setBaseTest(BaseTest baseTest) {
        this.baseTest = baseTest;
    }

    @Before
    public void beginTest() throws IOException {
        env.loadYAML("src/main/resources/efr/loan/refCreditParam.yaml");
        driverUtils.getURL(env.getProperty("urlEFR"));
        baseTest.authorization("loginEmp", "passwordEmp");
    }

    @Test
    @Epic("Тестирование UI")
    @Feature("Заявка на кредит")
    @Stories({@Story("Создание заявки на кредит")})
    @DisplayName("Рефинансирование")
    @Description("Создание заявки на рефинансирование текщего(-их) кредитов")
    public void testLoanRefCreate() {
        TestClient testClient = env.getClient();
        baseTest.findClient(testClient.getPassportSeries(), testClient.getPassportNumber());
        createRefinancingLoan(testClient);
        loanTools.confirmationLoan(testClient);
    }

    public void createRefinancingLoan(TestClient testClient) {
        performClick(ClientsPage.APPLY_LOAN.getPath(), ClientsPage.APPLY_LOAN.getLabel());
        //Инициация процесса
        performClick(CreditPage.CONTINUE.getPath(), CreditPage.CONTINUE.getLabel());
        //Кредитный калькулятор
        creditCalculateRef(testClient);

        //Проверяем ситенд для перехода к оформлению заявки
        loanTools.isEFRINT();

        //Кредитная заявка
        baseClass.choseSelect(CreditPage.CREDIT_PURPOSE, "creditPurpose");
        //Список блоков рефинансируемых кредитов
        refCreditBlock();

        // Указываем программу страхования
        loanTools.addInsurance();

        //Персональные данные
        loanTools.addPersonalProfile(testClient);
        performClick(CreditPage.CONTINUE.getPath(), CreditPage.CONTINUE.getLabel());
        staleness(CreditPage.CONTINUE);

        //Визуальная оценка
        loanTools.addVisualEstimate(testClient);
        performClick(CreditPage.CONTINUE.getPath(), CreditPage.CONTINUE.getLabel());

        loanTools.getByproducts();

        performClick(CreditPage.CONTINUE.getPath(), CreditPage.CONTINUE.getLabel());
        performClick(CreditPage.CONFIRM.getPath(), CreditPage.CONFIRM.getLabel());
        //Загрузка
        staleness(CreditPage.PRELOADER);
        //МАТРИЦА РЕШЕНИЙ (Выбор варианта кредитования)
        loanTools.getDecisionMatrix();
        performClick(CreditPage.CONTINUE.getPath(), CreditPage.CONTINUE.getLabel());
        //Добавляем документы
        loanTools.addDocuments();
        //Заключительный этап, отправляем на подтверждение
        performClick(CreditPage.CONTINUE.getPath(), CreditPage.CONTINUE.getLabel());
        performClick(CreditPage.CONFIRM.getPath(), CreditPage.CONFIRM.getLabel());

        assertTrueContains("Отсутсвует информационное сообщение: "
                , performGetText(CreditPage.NOTIFICATION.getPath(), CreditPage.NOTIFICATION.getLabel()), env.getProperty("loanInfo"));
        logger.info(performGetText(CreditPage.STATUS_LOAN.getPath(), CreditPage.STATUS_LOAN.getLabel()));
        // Нажимаем выход
        performClick(MainPage.EXIT.getPath(), MainPage.EXIT.getLabel());
    }

    public void creditCalculateRef(TestClient testClient) {
        String temp;
        //Кредитный калькулятор
        baseClass.choseSelect(CreditPage.CREDIT_PRODUCT, "creditProduct");
        Assert.assertTrue("Необходимо в поле \"Кредитный продукт\" указать продукт с рефинансированием", loanTools.isHasRef());
        int sum = 0;
        List<WebElement> webElements = performFindElements(CreditPage.REF_BLOCK.getPath(), CreditPage.REF_BLOCK.getLabel());
        logger.info("webElements size - " + webElements.size());
        for (int i = 0; ; i++) {
            temp = replacement("refAmount%d", i);
            if (!temp.contains("refAmount")) {
                performClick(CreditPage.REF_ADD.getPath(), CreditPage.REF_ADD.getLabel());
                int lineCount = webElements.size() == 0 ? i : i + webElements.size();
                String path = replacement(CreditPage.REF_AMOUNT.getPath(), lineCount);
                String label = replacement(CreditPage.REF_AMOUNT.getLabel(), lineCount);
                if (performFind(path, label).isEnabled()) {
                    performSendKeys(path, label, temp);
                    baseClass.choseSelect(CreditPage.REF_CURRENCY, String.valueOf(lineCount), "refCurrency");
                }
            } else break;
        }
        // Считаем сумму
        webElements = performFindElements(CreditPage.REF_BLOCK.getPath(), CreditPage.REF_BLOCK.getLabel());
        for (int i = 0; i < webElements.size(); i++) {
            sum = sum + baseClass.getInt(performGetValue(replacement(CreditPage.REF_AMOUNT.getPath(), i)
                    , replacement(CreditPage.REF_AMOUNT.getLabel(), i)));
        }
        performSendKeys(CreditPage.REF_ADDITIONAL_AMOUNT.getPath(), CreditPage.REF_ADDITIONAL_AMOUNT.getLabel(), "additionalAmount");
        // Проверяем что правильно посчиталась сумма при рефинансировании
        assertEquals(sum + baseClass.getInt(env.getProperty("additionalAmount")),
                baseClass.getInt(performGetValue(CreditPage.REF_REQUEST_AMOUNT.getPath(), CreditPage.REF_REQUEST_AMOUNT.getLabel())));

        performSendKeys(CreditPage.TERM.getPath(), CreditPage.TERM.getLabel(), "term");

        //performClear(CreditPage.INCOME);
        loanTools.getAvgAmount(testClient);

        performClick(replacement(CreditPage.BASE_RATE.getPath(), "baseRate"), CreditPage.BASE_RATE.getLabel());
    }

    public void refCreditBlock() {
        String temp;
        int count = 0;
        // Список блоков рефинансируемых кредитов
        List<WebElement> webElements = performFindElements(CreditPage.REF_CONTAINER.getPath(), CreditPage.REF_CONTAINER.getLabel());
        for (int i = 0; i < webElements.size(); i++) {
            WebElement element = webElements.get(i);
            /*
             * Проверяем контейнер по полю refBank в тестовых данных и по полю "Остаток ссудной задолженности"
             * на странице
             */
            if (element.findElement(By.xpath(CreditPage.REF_BANK.getPath())).isEnabled()) {
                //logger.info(element.findElement(By.xpath(CreditPage.REF_DEBT.getPath())).getAttribute("value"));
                //logger.info(String.valueOf(element.findElement(By.xpath(CreditPage.REF_BANK.getPath())).isEnabled()));
                if ("РСХБ".contains(Objects.requireNonNull(replacement("refBank%d", i)))) {
                    performClick(CreditPage.REF_RSHB_TRUE.getPath(), CreditPage.REF_RSHB_TRUE.getLabel(), element);
                } else {
                    performSendKeys(CreditPage.REF_BANK.getPath(), CreditPage.REF_BANK.getLabel(), element, replacement("refBank%d", count));
                }
                baseClass.choseSelect(CreditPage.REF_CONTRACT_TYPE, element, replacement("contractType%d", count));
                performSendKeys(CreditPage.REF_DATE_FROM.getPath(), CreditPage.REF_DATE_FROM.getLabel(), element, replacement("dateFrom%d", count));
                performSendKeys(CreditPage.REF_CREDIT_AMOUNT.getPath(), CreditPage.REF_CREDIT_AMOUNT.getLabel(), element, replacement("creditAmount%d", count));
                performSendKeys(CreditPage.REF_DEBT.getPath(), CreditPage.REF_DEBT.getLabel(), element, Keys.SHIFT, Keys.HOME, Keys.DELETE);
                performSendKeys(CreditPage.REF_DEBT.getPath(), CreditPage.REF_DEBT.getLabel(), element, replacement("refAmount%d", count));
                count++;
            } else {
                baseClass.choseSelect(CreditPage.REF_CONTRACT_TYPE, element
                        , (temp = replacement("contractType%d", i)).contains("contractType") ? contractType : temp);
            }
        }
    }
}
