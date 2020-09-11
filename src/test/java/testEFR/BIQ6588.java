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
    public void byproductLoan(){
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
        loanTest.addPersonalProfile();
        performClick(CreditPage.CONTINUE.getPath(), CreditPage.CONTINUE.getLabel());
        staleness(CreditPage.CONTINUE);

        //Визуальная оценка (ничего не трогаем)
        performClick(CreditPage.CONTINUE.getPath(), CreditPage.CONTINUE.getLabel());

        loanTest.getByproducts();

        assertEquals("byProduct", performGetText(CreditPage.BYPRODUCT.getPath(), CreditPage.BYPRODUCT.getLabel()).substring(0, 11));
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
        loanTest.creditCalculateRef();

        //Проверяем ситенд для перехода к оформлению заявки
        loanTest.isefrint();

        //Кредитная заявка
        baseClass.choseSelect(CreditPage.CREDIT_PURPOSE, "creditPurpose");
        //Список блоков рефинансируемых кредитов
        loanTest.refCreditBlock();

        // Указываем программу страхования
        loanTest.addInsurance();

        //Персональные данные
        loanTest.addPersonalProfile();
        performClick(CreditPage.CONTINUE.getPath(), CreditPage.CONTINUE.getLabel());
        staleness(CreditPage.CONTINUE);

        //Визуальная оценка (ничего не трогаем)
        performClick(CreditPage.CONTINUE.getPath(), CreditPage.CONTINUE.getLabel());

        loanTest.getByproducts();

        assertEquals("byProduct", performGetText(CreditPage.BYPRODUCT.getPath(), CreditPage.BYPRODUCT.getLabel()).substring(0, 11));
    }
}
