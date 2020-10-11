package testEFR;

import efr.pagesEFR.BaseClass;
import efr.pagesEFR.ClientsPage;
import efr.pagesEFR.CreditPage;
import efr.pagesEFR.MainPage;
import efr.testDataClient.TestClient;
import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utils.*;

import java.io.IOException;
import java.util.logging.Logger;

@Component("Credit")
@DisplayName("Кредитная заявка")
public class Credit extends TestUtils {

    @Rule
    public JUnitUtils jUnitUtils;
    private DriverUtils driverUtils;
    private DataProvider env;
    private Logger logger;
    private CommonUtils commonUtils;
    private BaseClass baseClass;
    private BaseTest baseTest;
    private LoanTools loanTools;

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
        env.loadYAML("src/main/resources/efr/loan/creditParam.yaml");
        driverUtils.getURL(env.getProperty("urlEFR"));
        baseTest.authorization("loginEmp", "passwordEmp");
    }

    @Test
    @Epic("Тестирование UI")
    @Feature("Заявка на кредит")
    @Stories({@Story("Создание заявки на кредит")})
    @DisplayName("Потребительский кредит")
    @Description("Создание заявки на новый кредит")
    public void testLoanCreate() {
        TestClient testClient = env.getClient();
        baseTest.findClient(testClient.getPassportSeries(), testClient.getPassportNumber());
        createNewLoan(testClient);
        loanTools.confirmationLoan(testClient);
    }

    public void createNewLoan(TestClient testClient) {
        performClick(ClientsPage.APPLY_LOAN.getPath(), ClientsPage.APPLY_LOAN.getLabel());
        //Инициация процесса
        performClick(CreditPage.CONTINUE.getPath(), CreditPage.CONTINUE.getLabel());

        //Кредитный калькулятор
        creditCalculateLoan(testClient);

        //Проверяем ситенд для перехода к оформлению заявки
        loanTools.isEFRINT();

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

    public void creditCalculateLoan(TestClient testClient) {
        //Кредитный калькулятор
        baseClass.choseSelect(CreditPage.CREDIT_PRODUCT, "creditProduct");
        performSendKeys(CreditPage.AMOUNT.getPath(), CreditPage.AMOUNT.getLabel(), "amount");
        performSendKeys(CreditPage.TERM.getPath(), CreditPage.TERM.getLabel(), "term");

        loanTools.getAvgAmount(testClient);

        performClick(replacement(CreditPage.BASE_RATE.getPath(), "baseRate"), CreditPage.BASE_RATE.getLabel());
    }
}
