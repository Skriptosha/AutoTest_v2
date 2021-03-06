package testEFR;

import efr.pagesEFR.BaseClass;
import efr.pagesEFR.ClientsPage;
import efr.pagesEFR.CreditPage;
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

@Component("BIQ6588")
@DisplayName("BIQ 6588")
public class BIQ6588 extends TestUtils {

    @Rule
    public JUnitUtils jUnitUtils;
    private DriverUtils driverUtils;
    private DataProvider env;
    private Logger logger;
    private CommonUtils commonUtils;
    private BaseClass baseClass;
    private BaseTest baseTest;
    private LoanTools loanTools;
    private TestClient testClient;
    private Credit credit;
    private RefCredit refCredit;

    @Autowired
    public void setRefCredit(RefCredit refCredit) { this.refCredit = refCredit; }

    @Autowired
    public void setCredit(Credit credit) { this.credit = credit; }

    @Autowired
    public void setjUnitUtils(JUnitUtils jUnitUtils) { this.jUnitUtils = jUnitUtils; }

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

    @Autowired
    public void setLoanTools(LoanTools loanTools) {
        this.loanTools = loanTools;
    }

    @Before
    public void beginTest() throws IOException {
        env.loadYAML("src/main/resources/efr/BIQ6587/BIQ6587.yaml");
        testClient = env.getClient();
        driverUtils.getURL(env.getProperty("urlEFR"));
        baseTest.authorization("loginEmp", "passwordEmp");
        baseTest.findClient(testClient.getPassportSeries(), testClient.getPassportNumber());
    }

    @Test
    @Epic("Тестирование UI")
    @Feature("BIQ 6588")
    @Stories({@Story("Заявка на кредит для проверки субпродуктов")})
    @DisplayName("Потребительский кредит")
    @Description("Проверка субпродуктов")
    public void byproductLoan(){
        performClick(ClientsPage.APPLY_LOAN.getPath(), ClientsPage.APPLY_LOAN.getLabel());
        //Инициация процесса
        performClick(CreditPage.CONTINUE.getPath(), CreditPage.CONTINUE.getLabel());

        //Кредитный калькулятор
        credit.creditCalculateLoan(testClient);

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

        assertEquals("byProduct6588", performGetText(CreditPage.BYPRODUCT.getPath(), CreditPage.BYPRODUCT.getLabel()).substring(0, 11));
    }

    @Test
    @Epic("Тестирование UI")
    @Feature("BIQ 6588")
    @Stories({@Story("Заявка на кредит для проверки субпродуктов")})
    @DisplayName("Рефинансирование")
    @Description("Проверка субпродуктов")
    public void byproductRef(){
        String temp;
        performClick(ClientsPage.APPLY_LOAN.getPath(), ClientsPage.APPLY_LOAN.getLabel());
        //Инициация процесса
        performClick(CreditPage.CONTINUE.getPath(), CreditPage.CONTINUE.getLabel());

        //Кредитный калькулятор
        refCredit.creditCalculateRef(testClient);

        //Проверяем ситенд для перехода к оформлению заявки
        loanTools.isEFRINT();

        //Кредитная заявка
        baseClass.choseSelect(CreditPage.CREDIT_PURPOSE, "creditPurpose");
        //Список блоков рефинансируемых кредитов
        refCredit.refCreditBlock();

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

        try {
            Thread.sleep(200000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals("byProduct6588", performGetText(CreditPage.BYPRODUCT.getPath(), CreditPage.BYPRODUCT.getLabel()).substring(0, 11));
    }
}
