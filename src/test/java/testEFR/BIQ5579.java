package testEFR;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pagesEFR.BaseClass;
import pagesEFR.ClientsPage;
import pagesEFR.CreditPage;
import utils.*;

import java.util.logging.Logger;

@Component("BIQ5579")
@DisplayName("BIQ 5579")
public class BIQ5579 extends TestUtils {

    @Rule
    private JUnitUtils jUnitUtils;

    private DriverUtils driverUtils;

    private DataProvider env;

    private Logger logger;

    private CommonUtils commonUtils;

    private BaseClass baseClass;

    private BaseTest baseTest;

    private String[] names;

    private LoanTest loanTest;

    @Autowired
    public TestUtils testUtils;

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
    public void setLoanTest(LoanTest loanTest) {
        this.loanTest = loanTest;
    }

    @Before
    public void beginTest() {
        driverUtils.getURL(env.getProperty("urlEFRLoan"));
        baseTest.authorization("loginEmp", "passwordEmp");
        baseTest.findClient("passSeriesLoan", "passNumberLoan");
    }

    @Test
    @Epic("Тестирование UI")
    @Feature("BIQ 6588")
    @Stories({@Story("Заявка на кредит для проверки субпродуктов")})
    @DisplayName("Потребительский кредит")
    @Description("Проверка субпродуктов")
    public void daDataEmployer(){
        performClick(ClientsPage.APPLY_LOAN.getPath(), ClientsPage.APPLY_LOAN.getLabel());
        //Инициация процесса
        performClick(CreditPage.CONTINUE.getPath(), CreditPage.CONTINUE.getLabel());

        //Кредитный калькулятор
        loanTest.creditCalculateLoan();

        //Проверяем ситенд для перехода к оформлению заявки
        loanTest.isefrint();

        // Указываем программу страхования
        loanTest.addInsurance();

        //Персональные данные
        //Работа
    }
}
