package testEFR;

import efr.pagesEFR.BaseClass;
import efr.pagesEFR.CreditPage;
import efr.pagesEFR.MainPage;
import efr.pagesEFR.PersonalDataPage;
import efr.testDataClient.TestClient;
import org.junit.Rule;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utils.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Component("LoanTools")
public class LoanTools {

    @Rule
    public JUnitUtils jUnitUtils;
    private DriverUtils driverUtils;
    private DataProvider env;
    private Logger logger;
    private CommonUtils commonUtils;
    private BaseClass baseClass;
    private BaseTest baseTest;
    private TestUtils testUtils;

    private final String ourSalary = "ЗП клиенты";
    private final String stateEmp = "Бюджетник";
    private final String pCreditHistory = "Положительная КИ";
    private final String pensCredit = "Пенсионный";
    private final String confirmProcess = "Процесс кредитования";
    private final String confirmTask = "Подтверждение работником мидл-офиса";
    private boolean skfo = false;
    private boolean pensOur = false;

    @Autowired
    public LoanTools(JUnitUtils jUnitUtils, DriverUtils driverUtils, DataProvider env, Logger logger, CommonUtils commonUtils, BaseClass baseClass, BaseTest baseTest, TestUtils testUtils
    ) {
        this.jUnitUtils = jUnitUtils;
        this.driverUtils = driverUtils;
        this.env = env;
        this.logger = logger;
        this.commonUtils = commonUtils;
        this.baseClass = baseClass;
        this.baseTest = baseTest;
        this.testUtils = testUtils;
    }

    void getAvgAmount(TestClient testClient) {
        String avgAmount;
        logger.info(testUtils.performGetValue(CreditPage.INCOME.getPath(), CreditPage.INCOME.getLabel()));
        if ((avgAmount = testUtils.performGetValue(CreditPage.INCOME.getPath(), CreditPage.INCOME.getLabel())).equals("0.00")) {
            testUtils.performSendKeys(CreditPage.INCOME.getPath(), CreditPage.INCOME.getLabel(), Keys.SHIFT, Keys.HOME, Keys.DELETE);
            testUtils.performSendKeys(CreditPage.INCOME.getPath(), CreditPage.INCOME.getLabel(), testClient.getClientOrganization().getAvgAmount());
        } else {
            testClient.getClientOrganization().setAvgAmount(avgAmount);
            testClient.getClientOrganization().setPensionAmount(avgAmount);
            //pensOur = true;
        }
    }

    /**
     * Если стенд - efrint - то можно перейти сразу к оформлению заявки
     */
    void isEFRINT() {
        if (!env.getProperty("urlEFR").contains("efrint")) {
            testUtils.performClick(CreditPage.CALCULATE.getPath(), CreditPage.CALCULATE.getLabel());
            testUtils.staleness(CreditPage.CALCULATE);
            //Загрузка
            testUtils.staleness(CreditPage.PRELOADER);
        }
        testUtils.performClick(CreditPage.SUBMIT.getPath(), CreditPage.SUBMIT.getLabel()); //оформить заявку
    }

    void addInsurance() {
        if (env.getProperty("IndividualOrNot").equalsIgnoreCase("Да")) {
            testUtils.performClick(CreditPage.INSURANCE_INDIVIDUAL.getPath(), CreditPage.INSURANCE_INDIVIDUAL.getLabel());
            baseClass.choseSelect(CreditPage.INSURANCE_PROGRAM, "insuranceProg");
            testUtils.performSendKeys(CreditPage.INSURANCE_AMOUNT.getPath(), CreditPage.INSURANCE_AMOUNT.getLabel(), "insuranceAmount");
        } else {
            baseClass.choseSelect(CreditPage.INSURANCE_PROGRAM, "insuranceProg");
        }
    }

    void getByproducts() {
        // Смотрим субпродукты и ставку
        logger.info("BYPRODUCT - " + testUtils.performGetText(CreditPage.BYPRODUCT.getPath(), CreditPage.BYPRODUCT.getLabel()));
        testUtils.performClick(CreditPage.BYPRODUCT.getPath(), CreditPage.BYPRODUCT.getLabel());
        /*
        List<WebElement> webElements = testUtils.performFindElements(CreditPage.BYPRODUCT_NOT_APPLY.getPath(), CreditPage.BYPRODUCT_NOT_APPLY.getLabel());
        webElements.forEach(p -> {
            logger.info("BYPRODUCT_NOT_APPLY - " + p.getText());
        });
         */
        testUtils.performClick(CreditPage.CREDIT_RATE.getPath(), CreditPage.CREDIT_RATE.getLabel());
        logger.info("CREDIT_RATE - " + testUtils.performGetText(CreditPage.CREDIT_RATE.getPath(), CreditPage.CREDIT_RATE.getLabel()));
    }

    void getDecisionMatrix() {
        List<WebElement> webElements = testUtils.performFindElements(CreditPage.TABLE_DECISION_MATRIX.getPath()
                , CreditPage.TABLE_DECISION_MATRIX.getLabel());
        webElements.forEach(p -> {
            logger.info(p.getText());
            /*
            if (removeSymbols(p.getText()).contains(Objects.requireNonNull(env.getProperty("matrixParam"))))
                testUtils.performClick(CreditPage.TABLE_DM_CHECK_ROW, p);

            */
        });
        testUtils.performClick(CreditPage.TABLE_DM_FIRST_ROW.getPath(), CreditPage.TABLE_DM_FIRST_ROW.getLabel());
    }

    void addPersonalProfile(TestClient testClient) {
        testUtils.performClick(PersonalDataPage.BORROWER_TAB.getPath(), PersonalDataPage.BORROWER_TAB.getLabel());
        testUtils.performClick(PersonalDataPage.PERSONAL_DATA.getPath(), PersonalDataPage.PERSONAL_DATA.getLabel());
        // Проверяем зп клиент или нет
        if (env.getProperty("baseRate").toLowerCase().contains(ourSalary.toLowerCase())) {
            if (!baseClass.isCheckBoxOn(PersonalDataPage.IS_OUR_SALARY)) {
                testUtils.performClick(PersonalDataPage.IS_OUR_SALARY.getPath(), PersonalDataPage.IS_OUR_SALARY.getLabel());
            }
        } else {
            if (baseClass.isCheckBoxOn(PersonalDataPage.IS_OUR_SALARY)) {
                testUtils.performClick(PersonalDataPage.IS_OUR_SALARY.getPath(), PersonalDataPage.IS_OUR_SALARY.getLabel());
            }
        }

        // Проверяем Положительная КИ у клиента или нет
        if (env.getProperty("baseRate").toLowerCase().contains(pCreditHistory.toLowerCase())) {
            if (!baseClass.isCheckBoxOn(PersonalDataPage.IS_P_CREDIT_HISTORY)) {
                testUtils.performClick(PersonalDataPage.IS_P_CREDIT_HISTORY.getPath(), PersonalDataPage.IS_P_CREDIT_HISTORY.getLabel());
            }
        } else {
            if (baseClass.isCheckBoxOn(PersonalDataPage.IS_P_CREDIT_HISTORY)) {
                testUtils.performClick(PersonalDataPage.IS_P_CREDIT_HISTORY.getPath(), PersonalDataPage.IS_P_CREDIT_HISTORY.getLabel());
            }
        }

        //Пенсия в банке
        if (pensOur) {
            if (!baseClass.isCheckBoxOn(PersonalDataPage.IS_OUR_PENS)) {
                testUtils.performClick(PersonalDataPage.IS_OUR_PENS.getPath(), PersonalDataPage.IS_OUR_PENS.getLabel());
            }
        } else {
            if (baseClass.isCheckBoxOn(PersonalDataPage.IS_OUR_PENS)) {
                testUtils.performClick(PersonalDataPage.IS_OUR_PENS.getPath(), PersonalDataPage.IS_OUR_PENS.getLabel());
            }
        }

        baseClass.choseSelect(PersonalDataPage.EDUCATION, testClient.getEducation());
        testUtils.performSendKeys(PersonalDataPage.PERIOD_RESIDENCE.getPath(), PersonalDataPage.PERIOD_RESIDENCE.getLabel()
                , testClient.getPeriodResidence());
        baseClass.choseSelect(PersonalDataPage.TYPE_HOUSING, testClient.getTypeHousing());
        baseClass.choseSelect(PersonalDataPage.FAMILY_STATUS, testClient.getFamilyStatus());

        testUtils.performSendKeys(PersonalDataPage.FAMILY_COUNT.getPath(), PersonalDataPage.FAMILY_COUNT.getLabel()
                , testClient.getFamilyMemberCount());
        testUtils.performSendKeys(PersonalDataPage.UNDERAGE_COUNT.getPath(), PersonalDataPage.FAMILY_COUNT.getLabel()
                , testClient.getUnderageChildCount());

        testUtils.performSendKeys(PersonalDataPage.DEPENDANT_COUNT.getPath(), PersonalDataPage.DEPENDANT_COUNT.getLabel()
                , testClient.getDependantCount());
        testUtils.performSendKeys(PersonalDataPage.CONTACT_TEL_NUMBER.getPath(), PersonalDataPage.CONTACT_TEL_NUMBER.getLabel()
                , testClient.getContactTelNumber());

        if (!env.getProperty("urlEFR").contains("/efr/")){
        testUtils.performSendKeys(PersonalDataPage.CONTACT_REFERENCE.getPath(), PersonalDataPage.CONTACT_REFERENCE.getLabel()
                , testClient.getContactReference());}
        else {
            baseClass.choseSelect(PersonalDataPage.CONTACT_REFERENCE_DIV, testClient.getContactReference());
        }

        if (isHasSKFO()) {
            testUtils.performClick(PersonalDataPage.SKFO_TEMPORARY_REGISTRATION_NO.getPath(), PersonalDataPage.SKFO_TEMPORARY_REGISTRATION_NO.getLabel());
            testUtils.performClick(PersonalDataPage.SKFO_OTHER_REGION_JOB_NO.getPath(), PersonalDataPage.SKFO_OTHER_REGION_JOB_NO.getLabel());
            testUtils.performClick(PersonalDataPage.SKFO_RELOCATION_PLAN_NO.getPath(), PersonalDataPage.SKFO_RELOCATION_PLAN_NO.getLabel());
            baseClass.choseSelect(PersonalDataPage.SKFO_EDUCATION_PLACE, testClient.getEducationPlace());
        }

        testUtils.performClick(PersonalDataPage.JOB_TAB.getPath(), PersonalDataPage.JOB_TAB.getLabel());
        //logger.info("EMPLOYMENT_INFO 1 " + testUtils.performFind(PersonalProfileCreditPage.EMPLOYMENT_INFO).getText());
        //logger.info("ORGANIZATION_NAME 2 " + testUtils.performFind(PersonalProfileCreditPage.ORGANIZATION_NAME).getAttribute("value"));
        /*
        baseClass.choseSelectDaData(PersonalDataPage.ORGANIZATION_NAME, "orgNameSearch5579", "orgName5579");
        testUtils.assertEquals("orgName5579", testUtils.performGetValue(PersonalDataPage.ORGANIZATION_NAME.getPath(), PersonalDataPage.ORGANIZATION_NAME.getLabel()));
        testUtils.assertEquals("inn5579", testUtils.performGetValue(PersonalDataPage.ORGANIZATION_INN.getPath(), PersonalDataPage.ORGANIZATION_INN.getLabel()));

        testUtils.assertEquals("empCount5579", testUtils.performGetText(PersonalDataPage.EMPLOYEE_COUNT.getPath(), PersonalDataPage.EMPLOYEE_COUNT.getLabel()));

        testUtils.assertEquals("opf5579", testUtils.performGetText(PersonalDataPage.OLF.getPath(), PersonalDataPage.OLF.getLabel()));

        testUtils.assertEquals("Россия", testUtils.performGetText(PersonalDataPage.COUNTRY_CHECK.getPath(), PersonalDataPage.COUNTRY_CHECK.getLabel()));
        testUtils.assertEquals("region5579", testUtils.performGetValue(PersonalDataPage.REGION.getPath(), PersonalDataPage.REGION.getLabel()));
        testUtils.assertEquals("area5579", testUtils.performGetValue(PersonalDataPage.AREA.getPath(), PersonalDataPage.AREA.getLabel()));
        testUtils.assertEquals("city5579", testUtils.performGetValue(PersonalDataPage.CITY.getPath(), PersonalDataPage.CITY.getLabel()));
        testUtils.assertEquals("street5579", testUtils.performGetValue(PersonalDataPage.STREET.getPath(), PersonalDataPage.STREET.getLabel()));
        testUtils.assertEquals("house5579", testUtils.performGetValue(PersonalDataPage.HOUSE_NUM.getPath(), PersonalDataPage.HOUSE_NUM.getLabel()));
        testUtils.assertEquals("construction5579", testUtils.performGetValue(PersonalDataPage.CONSTRUCTION.getPath(), PersonalDataPage.CONSTRUCTION.getLabel()));
        testUtils.assertEquals("building5579", testUtils.performGetValue(PersonalDataPage.BUILDING.getPath(), PersonalDataPage.BUILDING.getLabel()));
        testUtils.assertEquals("apart5579", testUtils.performGetValue(PersonalDataPage.APARTMENT.getPath(), PersonalDataPage.APARTMENT.getLabel()));
        testUtils.assertEquals("index5579", testUtils.performGetValue(PersonalDataPage.INDEX.getPath(), PersonalDataPage.INDEX.getLabel()));
         */

        if (testUtils.performGetText(PersonalDataPage.EMPLOYMENT_INFO.getPath(), PersonalDataPage.EMPLOYMENT_INFO.getLabel()).length() == 0) {
            baseClass.choseSelect(PersonalDataPage.EMPLOYMENT_INFO, testClient.getEmploymentInfo());
            testUtils.performClick(PersonalDataPage.JOB_ADD.getPath(), PersonalDataPage.JOB_ADD.getLabel());
            addWork(testClient);
        } else if (testUtils.performGetValue(PersonalDataPage.ORGANIZATION_NAME.getPath(), PersonalDataPage.ORGANIZATION_NAME.getLabel()).length() == 0) {
            logger.info("ORGANIZATION_NAME : " + testUtils.performGetValue(PersonalDataPage.ORGANIZATION_NAME.getPath()
                    , PersonalDataPage.ORGANIZATION_NAME.getLabel()).length());
            addWork(testClient);
        }
        //Проверяем Бюджетник или нет
        if (env.getProperty("baseRate").toLowerCase().contains(stateEmp.toLowerCase().toLowerCase())) {
            baseClass.choseSelect(PersonalDataPage.ORGANIZATION_TYPE, "Бюджетная");
        } else {
            baseClass.choseSelect(PersonalDataPage.ORGANIZATION_TYPE, testClient.getClientOrganization().getOrganizationType());
        }
        //logger.info("EXPERIENCE_YEARS " + testUtils.performFind(PersonalProfileCreditPage.EXPERIENCE_YEARS).getAttribute("value"));
        if (testUtils.performGetValue(PersonalDataPage.EXPERIENCE_YEARS.getPath(), PersonalDataPage.EXPERIENCE_YEARS.getLabel()).length() == 0) {
            testUtils.performSendKeys(PersonalDataPage.EXPERIENCE_YEARS.getPath(), PersonalDataPage.EXPERIENCE_YEARS.getLabel()
                    , testClient.getClientOrganization().getExperience());
            testUtils.performSendKeys(PersonalDataPage.EXPERIENCE_MONTHS.getPath(), PersonalDataPage.EXPERIENCE_MONTHS.getLabel(), "0");
            testUtils.performSendKeys(PersonalDataPage.EXPERIENCE_LAST_5YEARS.getPath(), PersonalDataPage.EXPERIENCE_LAST_5YEARS.getLabel()
                    , testClient.getClientOrganization().getFiveYearsExperience());
            testUtils.performSendKeys(PersonalDataPage.EXPERIENCE_LAST_5MONTHS.getPath(), PersonalDataPage.EXPERIENCE_LAST_5MONTHS.getLabel(), "0");
            testUtils.performSendKeys(PersonalDataPage.LAST5_JOB_COUNT.getPath(), PersonalDataPage.LAST5_JOB_COUNT.getLabel()
                    , testClient.getClientOrganization().getFiveYearsJobCount());
        }
        // Информация о доходах
        testUtils.performClick(PersonalDataPage.EXPENSES_TAB.getPath(), PersonalDataPage.EXPENSES_TAB.getLabel());

        addRevenue(testClient);

        testUtils.performSendKeys(PersonalDataPage.MANDATORY_PAYMENTS.getPath(), PersonalDataPage.MANDATORY_PAYMENTS.getLabel()
                , testClient.getRequiredPayments());

        testUtils.performSendKeys(PersonalDataPage.OTHER_PAYMENTS.getPath(), PersonalDataPage.OTHER_PAYMENTS.getLabel()
                , testClient.getOtherExpenses());

        testUtils.performClick(PersonalDataPage.OTHER_INFO_TAB.getPath(), PersonalDataPage.OTHER_INFO_TAB.getLabel());
        baseClass.choseSelect(PersonalDataPage.MILITARY_RELATION, testClient.getMilitaryServiceRelation());
        baseClass.choseSelect(PersonalDataPage.BANK_INFO_SOURCE, testClient.getBankInfoSource());

    }

    private void addRevenue(TestClient testClient) {
        baseClass.choseSelect(PersonalDataPage.CONFIRM_MAIN_REVENUE, testClient.getClientOrganization().getDocMainIncomeConfirmDocument());
        if (testClient.getClientOrganization().getDocMainIncomeConfirmDocument().contains("доходах ФЛ")) {
            testUtils.performClick(PersonalDataPage.REVENUE_2NDFL.getPath(), PersonalDataPage.REVENUE_2NDFL.getLabel());
            testUtils.performSendKeys(PersonalDataPage.REVENUE_DATE.getPath(), PersonalDataPage.REVENUE_DATE.getLabel()
                    , testClient.getClientOrganization().getRevenueDate());

            String[] months = testClient.getClientOrganization().getRevenueMonths().split(",");
            for (int i = 0; i < months.length - 3; i++) {
                testUtils.performClick(PersonalDataPage.REVENUE_ADD.getPath(), PersonalDataPage.REVENUE_ADD.getLabel());
            }
            testUtils.performSendKeys(PersonalDataPage.REVENUE_NUMBER.getPath()
                    , PersonalDataPage.REVENUE_NUMBER.getLabel(), "366");
            testUtils.performSendKeys(PersonalDataPage.REVENUE_YEAR.getPath()
                    , PersonalDataPage.REVENUE_YEAR.getLabel(), testClient.getClientOrganization().getRevenueDate().substring(4));
            List<WebElement> webElements = testUtils.performFindElements(PersonalDataPage.REVENUE_TABLE.getPath()
                    , PersonalDataPage.REVENUE_TABLE.getLabel());

            logger.info(Arrays.toString(months));
            for (int i = 0; i < months.length; i++) {
                baseClass.choseSelect(PersonalDataPage.REVENUE_TABLE_MONTH, webElements.get(i), months[i]);
                baseClass.choseSelect(PersonalDataPage.REVENUE_TABLE_ITEM, webElements.get(i)
                        , testClient.getClientOrganization().getRevenueItem());
                testUtils.performSendKeys(PersonalDataPage.REVENUE_TABLE_AMOUNT.getPath()
                        , PersonalDataPage.REVENUE_TABLE_AMOUNT.getLabel(), webElements.get(i)
                        , testClient.getClientOrganization().getAvgAmount());
            }
            testUtils.performClick(CreditPage.ADD_ATTACH.getPath(), CreditPage.ADD_ATTACH.getLabel());
        } else {
            testUtils.performSendKeys(PersonalDataPage.MAIN_REVENUE_AMOUNT.getPath(), PersonalDataPage.MAIN_REVENUE_AMOUNT.getLabel()
                    , testClient.getClientOrganization().getAvgAmount());
        }
        if (env.getProperty("creditProduct").contains(pensCredit)) {
            baseClass.choseSelect(PersonalDataPage.CONFIRM_PENS_REVENUE, testClient.getClientOrganization().getDocPensionIncome());
            testUtils.performSendKeys(PersonalDataPage.PENS_REVENUE_AMOUNT.getPath(), PersonalDataPage.PENS_REVENUE_AMOUNT.getLabel(),
                    testClient.getClientOrganization().getPensionAmount());
        }
    }

    private void addWork(TestClient testClient) {
        baseClass.choseSelect(PersonalDataPage.OLF, testClient.getClientOrganization().getOlf());
        testUtils.performSendKeys(PersonalDataPage.ORGANIZATION_INN.getPath(), PersonalDataPage.ORGANIZATION_INN.getLabel()
                , testClient.getClientOrganization().getInn());
        testUtils.performClick(PersonalDataPage.INN_FIND.getPath(), PersonalDataPage.INN_FIND.getLabel());

        baseClass.choseSelect(PersonalDataPage.EMPLOYEE_COUNT, testClient.getClientOrganization().getEmployeeCount());
        testUtils.performClick(PersonalDataPage.CONTRACT_PERPETUAL.getPath(), PersonalDataPage.CONTRACT_PERPETUAL.getLabel());
        baseClass.choseSelect(PersonalDataPage.ACTIVITY_TYPE, testClient.getClientOrganization().getActivityType());
        baseClass.choseSelect(PersonalDataPage.POSITION, testClient.getClientOrganization().getPosition());
        testUtils.performSendKeys(PersonalDataPage.START_DATE_JOB.getPath(), PersonalDataPage.START_DATE_JOB.getLabel()
                , testClient.getClientOrganization().getStartDate());
    }

    public void addVisualEstimate(TestClient testClient) {
        if (skfo) {
            testUtils.performClick(testUtils.replacement(PersonalDataPage.SKFO_RUSSIAN_LEVEL.getPath(),
                    testClient.getSkfoRusLevel())
                    , PersonalDataPage.SKFO_RUSSIAN_LEVEL.getLabel());
        }
    }

    void addDocuments() {
        String temp;
        for (int i = 0; ; i++) {
            temp = testUtils.replacement("documentType%d", i);
            if (!temp.contains("documentType")) {
                testUtils.performClick(CreditPage.ADD_DOCUMENT.getPath(), CreditPage.ADD_DOCUMENT.getLabel());
                baseClass.choseSelect(CreditPage.DOCUMENT_TYPE, testUtils.replacement("documentType%d", i));
                testUtils.performSendKeys(CreditPage.SCAN_UPLOAD.getPath(), CreditPage.SCAN_UPLOAD.getLabel(), testUtils.replacement("fileUpload%d", i));
                testUtils.performClick(CreditPage.ADD_ATTACH.getPath(), CreditPage.ADD_ATTACH.getLabel());
            } else break;
        }
    }

    private boolean isHasSKFO() {
        return false;
        /*
                (skfo = testUtils.performFindElements(PersonalDataPage.FOR_SKFO_BLOCK.getPath(), PersonalDataPage.FOR_SKFO_BLOCK.getLabel()).size()
                == Integer.parseInt(Objects.requireNonNull(env.getProperty("personalDataCount"))));

         */
    }

    boolean isHasRef() {
        //testUtils.staleness(CreditPage.CREDIT_PRODUCT);
        return env.getProperty("creditProduct").toLowerCase().contains("рефинансирование");
    }

    /**
     * Подтверждение кредитной заявки
     */
    public void confirmationLoan(TestClient testClient) {
        baseTest.authorization("loginEmpBack", "passwordEmpBack");
        testUtils.performClick(MainPage.TASKS.getPath(), MainPage.TASKS.getLabel());
        testUtils.performClick(MainPage.NOT_ASSIGNED.getPath(), MainPage.NOT_ASSIGNED.getLabel());

        List<WebElement> webElements = testUtils.performFindElements(MainPage.MY_TASK_TABLE.getPath(), MainPage.MY_TASK_TABLE.getLabel());
        for (WebElement webElement : webElements) {
            String s = webElement.getText();
            logger.info(s);
            String surname = testClient.getFio().split("\\s")[0];
            if (s.contains(surname) && s.contains(confirmProcess) && s.contains(confirmTask)
                    && s.contains(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))) {
                testUtils.performClick(MainPage.MY_TASK_TABLE_ROW_GET_EX.getPath(), MainPage.MY_TASK_TABLE_ROW_GET_EX.getLabel(), webElement);
                break;
            }
        }
        testUtils.performClick(MainPage.MY_TASK_CONFIRM.getPath(), MainPage.MY_TASK_CONFIRM.getLabel());
        testUtils.performClick(MainPage.CONFIRM.getPath(), MainPage.CONFIRM.getLabel());
    }
}