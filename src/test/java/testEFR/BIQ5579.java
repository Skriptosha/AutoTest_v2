package testEFR;

import efr.pagesEFR.*;
import efr.testDataClient.TestClient;
import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utils.*;

import java.io.IOException;
import java.util.logging.Logger;

@Component("BIQ5579")
@DisplayName("BIQ 5579")
public class BIQ5579 extends TestUtils {

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

    private final String country = "Россия";

    @Autowired
    public void setCredit(Credit credit) { this.credit = credit; }

    @Autowired
    public void setjUnitUtils(JUnitUtils jUnitUtils) {
        this.jUnitUtils = jUnitUtils;
    }

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
        env.loadYAML("src/main/resources/efr/BIQ5579/BIQ5579.yaml");
        env.loadYAML("src/main/resources/efr/loan/creditParam.yaml");
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
    public void daDataEmployer() {
        PersonalDataPage[] job = {PersonalDataPage.ORGANIZATION_INN, PersonalDataPage.ORGANIZATION_TYPE,
                PersonalDataPage.ORGANIZATION_TEL, PersonalDataPage.EMPLOYEE_COUNT,
                PersonalDataPage.ACTIVITY_TYPE, PersonalDataPage.OLF,
                PersonalDataPage.REGION, PersonalDataPage.CITY,
                PersonalDataPage.STREET, PersonalDataPage.HOUSE_NUM,
                PersonalDataPage.CONSTRUCTION, PersonalDataPage.BUILDING,
                PersonalDataPage.APARTMENT, PersonalDataPage.INDEX,
        };
        performClick(ClientsPage.APPLY_LOAN.getPath(), ClientsPage.APPLY_LOAN.getLabel());
        //Инициация процесса
        performClick(CreditPage.CONTINUE.getPath(), CreditPage.CONTINUE.getLabel());

        //Кредитный калькулятор
        credit.creditCalculateLoan(testClient);

        //Проверяем ситенд для перехода к оформлению заявки
        loanTools.isEFRINT();

        // Указываем программу страхования
        loanTools.addInsurance();

        performClick(PersonalDataPage.BORROWER_TAB.getPath(), PersonalDataPage.BORROWER_TAB.getLabel());
        performClick(PersonalDataPage.JOB_TAB.getPath(), PersonalDataPage.JOB_TAB.getLabel());

        //Персональные данные
        //Работа
        daDataMinSymbols(PersonalDataPage.ORGANIZATION_NAME, 3);
        daDataMinSymbols(PersonalDataPage.ORGANIZATION_INN, 3);

        baseClass.choseSelectDaData(PersonalDataPage.ORGANIZATION_NAME, "orgNameSearch5579", "orgName5579");
        checkDaData("");

        baseClass.choseSelectDaData(PersonalDataPage.ORGANIZATION_INN, "inn5579_1", "inn5579_1");
        checkDaData("_1");

        baseClass.choseSelectDaData(PersonalDataPage.ORGANIZATION_INN, "inn5579_2", "orgName5579_2");
        checkDaData("_2");

        performClick(PersonalDataPage.JOB_DELETE.getPath(), PersonalDataPage.JOB_DELETE.getLabel());
        performClick(PersonalDataPage.JOB_ADD.getPath(), PersonalDataPage.JOB_ADD.getLabel());

        baseClass.choseSelectDaData(PersonalDataPage.ORGANIZATION_NAME, "orgNameSearch5579_1", "orgName5579_1");
        checkDaData("_1");

        baseClass.choseSelectDaData(PersonalDataPage.ORGANIZATION_INN, "inn5579", "inn5579");
        checkDaData("");

    }

    void checkDaData(String prefix) {
        assertEquals("orgName5579".concat(prefix), performGetValue(PersonalDataPage.ORGANIZATION_NAME.getPath(), PersonalDataPage.ORGANIZATION_NAME.getLabel()));
        assertEquals("inn5579".concat(prefix), performGetValue(PersonalDataPage.ORGANIZATION_INN.getPath(), PersonalDataPage.ORGANIZATION_INN.getLabel()));
        // assertEquals("orgType5579".concat(prefix), performGetValue(PersonalDataPage.ORGANIZATION_TYPE.getPath(), PersonalDataPage.ORGANIZATION_TYPE.getLabel()));
        // assertEquals("orgTel5579".concat(prefix), performGetValue(PersonalDataPage.ORGANIZATION_TEL.getPath(), PersonalDataPage.ORGANIZATION_TEL.getLabel()));
        assertEquals("empCount5579".concat(prefix), performGetText(PersonalDataPage.EMPLOYEE_COUNT.getPath(), PersonalDataPage.EMPLOYEE_COUNT.getLabel()));
        // assertEquals("activity5579".concat(prefix), performGetValue(PersonalDataPage.ACTIVITY_TYPE.getPath(), PersonalDataPage.ACTIVITY_TYPE.getLabel()));
        assertEquals("opf5579".concat(prefix), performGetText(PersonalDataPage.OLF.getPath(), PersonalDataPage.OLF.getLabel()));
        //Адрес смотрим
        assertEquals(country, performGetText(PersonalDataPage.COUNTRY_CHECK.getPath(), PersonalDataPage.COUNTRY_CHECK.getLabel()));
        assertEquals("region5579".concat(prefix), performGetValue(PersonalDataPage.REGION.getPath(), PersonalDataPage.REGION.getLabel()));
        assertEquals("area5579".concat(prefix), performGetValue(PersonalDataPage.AREA.getPath(), PersonalDataPage.AREA.getLabel()));
        assertEquals("city5579".concat(prefix), performGetValue(PersonalDataPage.CITY.getPath(), PersonalDataPage.CITY.getLabel()));
        assertEquals("street5579".concat(prefix), performGetValue(PersonalDataPage.STREET.getPath(), PersonalDataPage.STREET.getLabel()));
        assertEquals("house5579".concat(prefix), performGetValue(PersonalDataPage.HOUSE_NUM.getPath(), PersonalDataPage.HOUSE_NUM.getLabel()));
        assertEquals("construction5579".concat(prefix), performGetValue(PersonalDataPage.CONSTRUCTION.getPath(), PersonalDataPage.CONSTRUCTION.getLabel()));
        assertEquals("building5579".concat(prefix), performGetValue(PersonalDataPage.BUILDING.getPath(), PersonalDataPage.BUILDING.getLabel()));
        assertEquals("apart5579".concat(prefix), performGetValue(PersonalDataPage.APARTMENT.getPath(), PersonalDataPage.APARTMENT.getLabel()));
        assertEquals("index5579".concat(prefix), performGetValue(PersonalDataPage.INDEX.getPath(), PersonalDataPage.INDEX.getLabel()));
    }

    public void daDataMinSymbols(PageEnum select, int min) {
        StringBuilder stringBuilder = new StringBuilder("0123456789");
        stringBuilder.setLength(min - 1);
        performSendKeys(select.getPath(), select.getLabel(), stringBuilder.toString());
        driverUtils.sendKeys(select.getPath(), Keys.ARROW_RIGHT);
        String oc = MainPage.OPTIONS_CONTROL.getPath();
        performFindElements(oc.substring(0, oc.indexOf("[")), MainPage.OPTIONS_CONTROL.getLabel())
                .forEach(webElement -> {
                    logger.info(webElement.getAttribute("class"));
                    assertFalseContains("Подсказки появляются при вводе " + (min - 1) + " символов"
                            , webElement.getAttribute("class"), oc.substring(oc.indexOf("class='") + 7, oc.indexOf("']")));
                });
        performClear(select.getPath(), select.getLabel());
    }
}