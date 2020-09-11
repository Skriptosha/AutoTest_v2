package testEFR;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pagesEFR.BaseClass;
import pagesEFR.ClientsPage;
import pagesEFR.CreditPage;
import pagesEFR.PersonalDataPage;
import utils.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@Component("LoanTest")
@DisplayName("Кредитная заявка")
public class LoanTest extends TestUtils {

    @Rule
    public JUnitUtils jUnitUtils;

    private DriverUtils driverUtils;

    private DataProvider env;

    private Logger logger;

    private CommonUtils commonUtils;

    private BaseClass baseClass;

    private BaseTest baseTest;

    private String[] names;

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
    public void beginTest() {
        driverUtils.getURL(env.getProperty("urlEFRLoan"));
        baseTest.authorization("loginEmp", "passwordEmp");
        baseTest.findClient("passSeriesLoan", "passNumberLoan");
    }

    @Test
    @Epic("Тестирование UI")
    @Feature("Заявка на кредит")
    @Stories({@Story("Создание заявки на кредит")})
    @DisplayName("Потребительский кредит")
    @Description("Создание заявки на новый кредит")
    public void testLoanCreate() {
        createNewLoan();
    }

    public void createNewLoan() {
        performClick(ClientsPage.APPLY_LOAN.getPath(), ClientsPage.APPLY_LOAN.getLabel());
        //Инициация процесса
        performClick(CreditPage.CONTINUE.getPath(), CreditPage.CONTINUE.getLabel());

        //Кредитный калькулятор
        creditCalculateLoan();

        //Проверяем ситенд для перехода к оформлению заявки
        isefrint();

        // Указываем программу страхования
        addInsurance();

        //Персональные данные
        addPersonalProfile();
        performClick(CreditPage.CONTINUE.getPath(), CreditPage.CONTINUE.getLabel());
        staleness(CreditPage.CONTINUE);

        //Визуальная оценка (ничего не трогаем)
        performClick(CreditPage.CONTINUE.getPath(), CreditPage.CONTINUE.getLabel());

        getByproducts();

        performClick(CreditPage.CONTINUE.getPath(), CreditPage.CONTINUE.getLabel());
        performClick(CreditPage.CONFIRM.getPath(), CreditPage.CONFIRM.getLabel());

        //Загрузка
        staleness(CreditPage.PRELOADER);
        //МАТРИЦА РЕШЕНИЙ (Выбор варианта кредитования)
        getDecisionMatrix();
        performClick(CreditPage.CONTINUE.getPath(), CreditPage.CONTINUE.getLabel());
        //Добавляем документы
        addDocuments();
        //Заключительный этап, отправляем на подтверждение
        performClick(CreditPage.CONTINUE.getPath(), CreditPage.CONTINUE.getLabel());
        performClick(CreditPage.CONFIRM.getPath(), CreditPage.CONFIRM.getLabel());

        assertTrueContains("Отсутсвует информационное сообщение: "
                , performGetText(CreditPage.NOTIFICATION.getPath(), CreditPage.NOTIFICATION.getLabel()), env.getProperty("loanInfo"));
        logger.info(performGetText(CreditPage.STATUS_LOAN.getPath(), CreditPage.STATUS_LOAN.getLabel()));
    }

    public void creditCalculateLoan(){
        //Кредитный калькулятор
        baseClass.choseSelect(CreditPage.CREDIT_PRODUCT, "creditProduct");
        performSendKeys(CreditPage.AMOUNT.getPath(), CreditPage.AMOUNT.getLabel(), "amount");
        performSendKeys(CreditPage.TERM.getPath(), CreditPage.TERM.getLabel(), "term");

        performSendKeys(CreditPage.INCOME.getPath(), CreditPage.INCOME.getLabel(), Keys.SHIFT, Keys.HOME, Keys.DELETE);
        performSendKeys(CreditPage.INCOME.getPath(), CreditPage.INCOME.getLabel(), "income");
        performClick(replacement(CreditPage.BASE_RATE.getPath(), "baseRate"), CreditPage.BASE_RATE.getLabel());
    }

    @Test
    @Epic("Тестирование UI")
    @Feature("Заявка на кредит")
    @Stories({@Story("Создание заявки на кредит")})
    @DisplayName("Рефинансирование")
    @Description("Создание заявки на рефинансирование текщего(-их) кредитов")
    public void testLoanRefCreate() {
        createRefinancingLoan();
    }

    public void createRefinancingLoan() {
        performClick(ClientsPage.APPLY_LOAN.getPath(), ClientsPage.APPLY_LOAN.getLabel());
        //Инициация процесса
        performClick(CreditPage.CONTINUE.getPath(), CreditPage.CONTINUE.getLabel());
        //Кредитный калькулятор
        creditCalculateRef();

        //Проверяем ситенд для перехода к оформлению заявки
        isefrint();

        //Кредитная заявка
        baseClass.choseSelect(CreditPage.CREDIT_PURPOSE, "creditPurpose");
        //Список блоков рефинансируемых кредитов
        refCreditBlock();

        // Указываем программу страхования
        addInsurance();

        //Персональные данные
        addPersonalProfile();
        performClick(CreditPage.CONTINUE.getPath(), CreditPage.CONTINUE.getLabel());
        staleness(CreditPage.CONTINUE);

        //Визуальная оценка (ничего не трогаем)
        performClick(CreditPage.CONTINUE.getPath(), CreditPage.CONTINUE.getLabel());

        getByproducts();

        performClick(CreditPage.CONTINUE.getPath(), CreditPage.CONTINUE.getLabel());
        performClick(CreditPage.CONFIRM.getPath(), CreditPage.CONFIRM.getLabel());
        //Загрузка
        staleness(CreditPage.PRELOADER);
        //МАТРИЦА РЕШЕНИЙ (Выбор варианта кредитования)
        getDecisionMatrix();
        performClick(CreditPage.CONTINUE.getPath(), CreditPage.CONTINUE.getLabel());
        //Добавляем документы
        addDocuments();
        //Заключительный этап, отправляем на подтверждение
        performClick(CreditPage.CONTINUE.getPath(), CreditPage.CONTINUE.getLabel());
        performClick(CreditPage.CONFIRM.getPath(), CreditPage.CONFIRM.getLabel());

        assertTrueContains("Отсутсвует информационное сообщение: "
                , performGetText(CreditPage.NOTIFICATION.getPath(), CreditPage.NOTIFICATION.getLabel()), env.getProperty("loanInfo"));
        logger.info(performGetText(CreditPage.STATUS_LOAN.getPath(), CreditPage.STATUS_LOAN.getLabel()));
    }

    public void creditCalculateRef(){
        String temp;
        //Кредитный калькулятор
        baseClass.choseSelect(CreditPage.CREDIT_PRODUCT, "creditProductRef");
        Assert.assertTrue("Необходимо в поле \"Кредитный продукт\" указать продукт с рефинансированием", isHasRef());
        int sum = 0;
        for (int i = 0; ; i++) {
        temp = replacement("refAmount%d", i);
            if (!temp.contains("refAmount")) {
                performClick(CreditPage.REF_ADD.getPath(), CreditPage.REF_ADD.getLabel());
                temp = replacement("refAmount%d", i);
                performSendKeys(replacement(CreditPage.REF_AMOUNT.getPath(), i)
                        , replacement(CreditPage.REF_AMOUNT.getLabel(), i)
                        , temp);
                baseClass.choseSelect(CreditPage.REF_CURRENCY, String.valueOf(i), "refCurrency");
                sum = sum + baseClass.getInt(temp);
                logger.info(String.valueOf(sum));
            } else break;
        }
        performSendKeys(CreditPage.REF_ADDITIONAL_AMOUNT.getPath(), CreditPage.REF_ADDITIONAL_AMOUNT.getLabel(), "additionalAmount");
        // Проверяем что правильно посчиталась сумма при рефинансировании
        assertEquals(sum + baseClass.getInt(env.getProperty("additionalAmount")),
                baseClass.getInt(performGetValue(CreditPage.REF_REQUEST_AMOUNT.getPath(), CreditPage.REF_REQUEST_AMOUNT.getLabel())));

        performSendKeys(CreditPage.TERM.getPath(), CreditPage.TERM.getLabel(), "termRef");

        //performClear(CreditPage.INCOME);
        performSendKeys(CreditPage.INCOME.getPath(), CreditPage.INCOME.getLabel(), Keys.SHIFT, Keys.HOME, Keys.DELETE);
        performSendKeys(CreditPage.INCOME.getPath(), CreditPage.INCOME.getLabel(), "incomeRef");
        performClick(replacement(CreditPage.BASE_RATE.getPath(),"baseRateRef"), CreditPage.BASE_RATE.getLabel());
    }

    public void refCreditBlock(){
        // Список блоков рефинансируемых кредитов
        List<WebElement> webElements = performFindElements(CreditPage.REF_CONTAINER.getPath(), CreditPage.REF_CONTAINER.getLabel());
        for (int i = 0; i < webElements.size(); i++) {
            WebElement element = webElements.get(i);
            if (replacement("refAmount%d", i) != null) {
                /*
                 * Проверяем контейнер по полю refBank в тестовых данных и по полю "Остаток ссудной задолженности"
                 * на странице
                 */
                if ("РСХБ".contains(Objects.requireNonNull(replacement("refBank%d", i)))
                        && removeSymbols(performGetValue(CreditPage.REF_DEBT.getPath(), CreditPage.REF_DEBT.getLabel()))
                        .equals(env.getProperty(replacement("refAmount%d", i)))) {
                    performClick(CreditPage.REF_RSHB_TRUE.getPath(), CreditPage.REF_RSHB_TRUE.getLabel(), element);
                } else {
                    performSendKeys(CreditPage.REF_BANK.getPath(), CreditPage.REF_BANK.getLabel(), element, replacement("refBank%d", i));
                }
                baseClass.choseSelect(CreditPage.REF_CONTRACT_TYPE, element, replacement("contractType%d", i));
                performSendKeys(CreditPage.REF_DATE_FROM.getPath(), CreditPage.REF_DATE_FROM.getLabel(), element, replacement("dateFrom%d", i));
                performSendKeys(CreditPage.REF_CREDIT_AMOUNT.getPath(), CreditPage.REF_CREDIT_AMOUNT.getLabel(), element, replacement("creditAmount%d", i));
            } else break;
        }
    }

    /**
     * Если стенд - efrint - то можно перейти сразу к оформлению заявки
     */
    void isefrint() {
        if (!env.getProperty("urlEFRLoan").contains("efrint")) {
            performClick(CreditPage.CALCULATE.getPath(), CreditPage.CALCULATE.getLabel());
            staleness(CreditPage.CALCULATE);
            //Загрузка
            staleness(CreditPage.PRELOADER);
        }
        performClick(CreditPage.SUBMIT.getPath(), CreditPage.SUBMIT.getLabel()); //оформить заявку
    }

    void addInsurance() {
        if (env.getProperty("IndividualOrNot").equalsIgnoreCase("Да")) {
            performClick(CreditPage.INSURANCE_INDIVIDUAL.getPath(), CreditPage.INSURANCE_INDIVIDUAL.getLabel());
            baseClass.choseSelect(CreditPage.INSURANCE_PROGRAM, "insuranceProg");
            performSendKeys(CreditPage.INSURANCE_AMOUNT.getPath(), CreditPage.INSURANCE_AMOUNT.getLabel(), "insuranceAmount");
        } else {
            baseClass.choseSelect(CreditPage.INSURANCE_PROGRAM, "insuranceProg");
        }
    }

    void getByproducts() {
        // Смотрим субпродукты и ставку
        logger.info("BYPRODUCT - " + performGetText(CreditPage.BYPRODUCT.getPath(), CreditPage.BYPRODUCT.getLabel()));
        performClick(CreditPage.BYPRODUCT.getPath(), CreditPage.BYPRODUCT.getLabel());
        List<WebElement> webElements = performFindElements(CreditPage.BYPRODUCT_NOT_APPLY.getPath(), CreditPage.BYPRODUCT_NOT_APPLY.getLabel());
        webElements.forEach(p -> {
            logger.info("BYPRODUCT_NOT_APPLY - " + p.getText());
        });
        performClick(CreditPage.CREDIT_RATE.getPath(), CreditPage.CREDIT_RATE.getLabel());
        logger.info("CREDIT_RATE - " + performGetText(CreditPage.CREDIT_RATE.getPath(), CreditPage.CREDIT_RATE.getLabel()));
    }

    private void getDecisionMatrix() {
        List<WebElement> webElements = performFindElements(CreditPage.TABLE_DECISION_MATRIX.getPath(), CreditPage.TABLE_DECISION_MATRIX.getLabel());
        webElements.forEach(p -> {
            logger.info(p.getText());
            /*
            if (removeSymbols(p.getText()).contains(Objects.requireNonNull(env.getProperty("matrixParam"))))
                performClick(CreditPage.TABLE_DM_CHECK_ROW, p);

             */
            performClick(CreditPage.TABLE_DM_FIRST_ROW.getPath(), CreditPage.TABLE_DM_FIRST_ROW.getLabel());
        });
    }

    void addPersonalProfile() {
        performClick(PersonalDataPage.BORROWER_TAB.getPath(), PersonalDataPage.BORROWER_TAB.getLabel());
        performClick(PersonalDataPage.PERSONAL_DATA.getPath(), PersonalDataPage.PERSONAL_DATA.getLabel());
        baseClass.choseSelect(PersonalDataPage.EDUCATION, "education");
        performSendKeys(PersonalDataPage.PERIOD_RESIDENCE.getPath(), PersonalDataPage.PERIOD_RESIDENCE.getLabel(), "periodResidence");
        baseClass.choseSelect(PersonalDataPage.TYPE_HOUSING, "typeHousing");
        baseClass.choseSelect(PersonalDataPage.FAMILY_STATUS, "familyStatus");

        performSendKeys(PersonalDataPage.FAMILY_COUNT.getPath(), PersonalDataPage.FAMILY_COUNT.getLabel(), "familyMemberCount");

        performSendKeys(PersonalDataPage.UNDERAGE_COUNT.getPath(), PersonalDataPage.FAMILY_COUNT.getLabel(), "underageChildCount");

        performSendKeys(PersonalDataPage.DEPENDANT_COUNT.getPath(), PersonalDataPage.DEPENDANT_COUNT.getLabel(), "dependantCount");
        performSendKeys(PersonalDataPage.CONTACT_TEL_NUMBER.getPath(), PersonalDataPage.CONTACT_TEL_NUMBER.getLabel(), "contactTelNumber");
        performSendKeys(PersonalDataPage.CONTACT_REFERENCE.getPath(), PersonalDataPage.CONTACT_REFERENCE.getLabel(), "contactReference");

        if (isHasSKFO()) {
            performClick(PersonalDataPage.TEMPORARY_REGISTRATION_NO.getPath(), PersonalDataPage.TEMPORARY_REGISTRATION_NO.getLabel());
            performClick(PersonalDataPage.OTHER_REGION_JOB_NO.getPath(), PersonalDataPage.OTHER_REGION_JOB_NO.getLabel());
            performClick(PersonalDataPage.RELOCATION_PLAN_NO.getPath(), PersonalDataPage.RELOCATION_PLAN_NO.getLabel());
            baseClass.choseSelect(PersonalDataPage.EDUCATION_PLACE, "educationPlace");
        }

        performClick(PersonalDataPage.JOB_TAB.getPath(), PersonalDataPage.JOB_TAB.getLabel());
        //logger.info("EMPLOYMENT_INFO 1 " + performFind(PersonalProfileCreditPage.EMPLOYMENT_INFO).getText());
        //logger.info("ORGANIZATION_NAME 2 " + performFind(PersonalProfileCreditPage.ORGANIZATION_NAME).getAttribute("value"));
        if (performFind(PersonalDataPage.EMPLOYMENT_INFO.getPath(), PersonalDataPage.EMPLOYMENT_INFO.getLabel()).getText().length() == 0) {
            baseClass.choseSelect(PersonalDataPage.EMPLOYMENT_INFO, "employmentInfo");
            performClick(PersonalDataPage.JOB_ADD.getPath(), PersonalDataPage.JOB_ADD.getLabel());
            addWork();
        } else if (performFind(PersonalDataPage.ORGANIZATION_NAME.getPath(), PersonalDataPage.ORGANIZATION_NAME.getLabel()).getAttribute("value").length() == 0) {
            addWork();
        }
        //logger.info("EXPERIENCE_YEARS " + performFind(PersonalProfileCreditPage.EXPERIENCE_YEARS).getAttribute("value"));
        if (performFind(PersonalDataPage.EXPERIENCE_YEARS.getPath(), PersonalDataPage.EXPERIENCE_YEARS.getLabel()).getAttribute("value").length() == 0) {
            performSendKeys(PersonalDataPage.EXPERIENCE_YEARS.getPath(), PersonalDataPage.EXPERIENCE_YEARS.getLabel(), "experience");
            performSendKeys(PersonalDataPage.EXPERIENCE_MONTHS.getPath(), PersonalDataPage.EXPERIENCE_MONTHS.getLabel(), "0");
            performSendKeys(PersonalDataPage.EXPERIENCE_LAST_5YEARS.getPath(), PersonalDataPage.EXPERIENCE_LAST_5YEARS.getLabel(),"fiveYearsExperience");
            performSendKeys(PersonalDataPage.EXPERIENCE_LAST_5MONTHS.getPath(), PersonalDataPage.EXPERIENCE_LAST_5MONTHS.getLabel(), "0");
            performSendKeys(PersonalDataPage.LAST5_JOB_COUNT.getPath(), PersonalDataPage.LAST5_JOB_COUNT.getLabel(), "fiveYearsJobCount");
        }
        // Информация о доходах
        performClick(PersonalDataPage.EXPENSES_TAB.getPath(), PersonalDataPage.EXPENSES_TAB.getLabel());

        addRevenue();

        performSendKeys(PersonalDataPage.MANDATORY_PAYMENTS.getPath(), PersonalDataPage.MANDATORY_PAYMENTS.getLabel(), "requiredPayments");

        performSendKeys(PersonalDataPage.OTHER_PAYMENTS.getPath(), PersonalDataPage.OTHER_PAYMENTS.getLabel(), "otherExpenses");

        performClick(PersonalDataPage.OTHER_INFO_TAB.getPath(), PersonalDataPage.OTHER_INFO_TAB.getLabel());
        baseClass.choseSelect(PersonalDataPage.MILITARY_RELATION, "militaryServiceRelation");
        baseClass.choseSelect(PersonalDataPage.BANK_INFO_SOURCE, "bankInfoSource");

    }

    private void addRevenue() {
        baseClass.choseSelect(PersonalDataPage.CONFIRM_MAIN_REVENUE, "docmainIncomeConfirmDocument");
        if (env.getProperty("docmainIncomeConfirmDocument").contains("доходах ФЛ")) {
            performClick(PersonalDataPage.REVENUE_2NDFL.getPath(), PersonalDataPage.REVENUE_2NDFL.getLabel());
            performSendKeys(PersonalDataPage.REVENUE_DATE.getPath(), PersonalDataPage.REVENUE_DATE.getLabel(), "revenueDate");

            String[] months = env.getProperty("revenueMonths").split(",");
            for (int i = 0; i < months.length - 3; i++) {
                performClick(PersonalDataPage.REVENUE_ADD.getPath(), PersonalDataPage.REVENUE_ADD.getLabel());
            }
            performSendKeys(PersonalDataPage.REVENUE_NUMBER.getPath()
                    , PersonalDataPage.REVENUE_NUMBER.getLabel(), "366");
            performSendKeys(PersonalDataPage.REVENUE_YEAR.getPath()
                    , PersonalDataPage.REVENUE_YEAR.getLabel(), env.getProperty("revenueDate").substring(4));
            List<WebElement> webElements = performFindElements(PersonalDataPage.REVENUE_TABLE.getPath()
                    , PersonalDataPage.REVENUE_TABLE.getLabel());

            logger.info(Arrays.toString(months));
            for (int i = 0; i < months.length; i++) {
                baseClass.choseSelect(PersonalDataPage.REVENUE_TABLE_MONTH, webElements.get(i), months[i]);
                baseClass.choseSelect(PersonalDataPage.REVENUE_TABLE_ITEM, webElements.get(i), "revenueItem");
                performSendKeys(PersonalDataPage.REVENUE_TABLE_AMOUNT.getPath()
                        , PersonalDataPage.REVENUE_TABLE_AMOUNT.getLabel(), webElements.get(i), "revenueAmount");
            }
            performClick(CreditPage.ADD_ATTACH.getPath(), CreditPage.ADD_ATTACH.getLabel());
        } else {
            performSendKeys(PersonalDataPage.MAIN_REVENUE_AMOUNT.getPath(), PersonalDataPage.MAIN_REVENUE_AMOUNT.getLabel(), "avgAmount");
        }
    }

    private void addWork() {
        baseClass.choseSelect(PersonalDataPage.ORGANIZATION_TYPE, "organizationType");
        baseClass.choseSelect(PersonalDataPage.OLF, "olf");
        performSendKeys(PersonalDataPage.ORGANIZATION_INN.getPath(), PersonalDataPage.ORGANIZATION_INN.getLabel(), "inn");
        performClick(PersonalDataPage.INN_FIND.getPath(), PersonalDataPage.INN_FIND.getLabel());

        baseClass.choseSelect(PersonalDataPage.EMPLOYEE_COUNT, "employeeCount");
        performClick(PersonalDataPage.CONTRACT_PERPETUAL.getPath(), PersonalDataPage.CONTRACT_PERPETUAL.getLabel());
        baseClass.choseSelect(PersonalDataPage.ACTIVITY_TYPE, "activityType");
        baseClass.choseSelect(PersonalDataPage.POSITION, "position");
        performSendKeys(PersonalDataPage.ORGANIZATION_INN.getPath(), PersonalDataPage.ORGANIZATION_INN.getLabel(), "inn");
        performSendKeys(PersonalDataPage.START_DATE_JOB.getPath(), PersonalDataPage.START_DATE_JOB.getLabel(), "startDate");
    }

    private void addDocuments() {
        String temp;
        for (int i = 0; ; i++) {
            temp = replacement("documentType%d", i);
            if (!temp.contains("documentType")) {
                performClick(CreditPage.ADD_DOCUMENT.getPath(), CreditPage.ADD_DOCUMENT.getLabel());
                baseClass.choseSelect(CreditPage.DOCUMENT_TYPE, replacement("documentType%d", i));
                performSendKeys(CreditPage.SCAN_UPLOAD.getPath(), CreditPage.SCAN_UPLOAD.getLabel(), replacement("fileUpload%d", i));
                performClick(CreditPage.ADD_ATTACH.getPath(), CreditPage.ADD_ATTACH.getLabel());
            } else break;
        }
    }

    private boolean isHasSKFO() {
        return performFindElements(PersonalDataPage.FOR_SKFO_BLOCK.getPath(), PersonalDataPage.FOR_SKFO_BLOCK.getLabel()).size()
                == Integer.parseInt(Objects.requireNonNull(env.getProperty("personalDataCount")));
    }

    boolean isHasRef() {
        //staleness(CreditPage.CREDIT_PRODUCT);
        logger.info("REF_IS_ACTIVE - " + performFindElements(CreditPage.REF_IS_ACTIVE.getPath(), CreditPage.REF_IS_ACTIVE.getLabel()).size());
        return performFindElements(CreditPage.REF_IS_ACTIVE.getPath(), CreditPage.REF_IS_ACTIVE.getLabel()).size()
                == Integer.parseInt(Objects.requireNonNull(env.getProperty("refDataCount")));
    }

    public void confirmationLoan() {
        baseTest.authorization("loginEmpBack", "passwordEmpBack");
    }
}