package pagesEFR;

import lombok.Getter;
import pagesOnlineApp.PageEnum;

public enum PersonalDataPage implements PageEnum {

    //Заемщик
    BORROWER_TAB("//a[@data-id='borrower-credit-tab']", "Заемщик"),
    //Персональные данные
    PERSONAL_DATA("//a[@data-id='personal-data-credit-tab']", "Персональные данные"),
    EDUCATION("//div[@data-id='select-form-field-education']", "Образование"),
    MILITARY_ID_YES("//div[@data-id='switcher-form-field-militaryCardtrue']", "Военный билет - Да"),
    PERIOD_RESIDENCE("//input[@data-id='input-form-field-residenceAddressTerm']", "Срок проживания по адресу, лет"),
    TYPE_HOUSING("//div[@data-id='select-form-field-housingType']", "Тип жилья"),
    FAMILY_STATUS("//div[@data-id='select-form-field-familyStatus']", "Семейное положение"),
    FAMILY_COUNT("//input[@data-id='input-form-field-familyMemberCount']", "Всего членов семьи"),
    UNDERAGE_COUNT("//input[@data-id='input-form-field-underageChildCount']", "Из них детей до 18 лет"),
    DEPENDANT_COUNT("//input[@data-id='input-form-field-dependantCount']", "Иных иждивенцев"),
    CONTACT_TEL_NUMBER("//input[@data-id='input-form-field-otherContactMemberPhone']", "Контактный телефон"),
    CONTACT_REFERENCE("//input[@data-id='input-form-field-otherContactMemberReference']", "Кем является Заемщику"),
    FOR_SKFO_BLOCK("//div[@data-id='personal-data']/div/div", "Персональные данные"),
    TEMPORARY_REGISTRATION_NO("//button[@data-id='switcher-form-field-temporaryRegistrationfalse']", "Временная регистрация - нет"),
    OTHER_REGION_JOB_NO("//button[@data-id='switcher-form-field-otherRegionJobfalse']"
            , "Планируете ли Вы в период погашения кредита вести трудовую деятельность в другом субъекте РФ/в другой стране?* - нет"),
    RELOCATION_PLAN_NO("//button[@data-id='switcher-form-field-relocationPlanfalse']"
            , "Планируете ли Вы переезд на ПМЖ в другой субъект РФ/в другую страну в ближайшее время? - нет"),
    EDUCATION_PLACE("//div[@data-id='select-form-field-educationPlace']", "Место получения высшего образования"),

    //Работа
    JOB_TAB("//a[@data-id='job-data-credit-tab']", "Работа"),
    EMPLOYMENT_INFO("//div[@data-id='select-form-field-employment']", "Информация о трудоустройстве"),
    IS_LPH_NO("//div[@data-id='switcher-form-field-isLphfalse']", "Ведение ЛПХ - Нет"),

    JOB_INFO_BLOCK("//div[text()='Информация о местах работы']//..", "Информация о местах работы"),
    JOB_ADD("//a[@data-id='job-info-fieldset-sub-title-link']", "Добавить место работы"),

    ORGANIZATION_TYPE("//div[@data-id='select-form-field-organizationType']", "Тип организации"),
    OLF("//div[@data-id='select-form-field-organizationalAndLegalFormOfEmployer']", "ОПФ"),
    ORGANIZATION_NAME("//input[@data-id='input-form-field-organizationName']", "Наименование организации"),
    ORGANIZATION_INN("//input[@data-id='input-form-field-organizationInn']", "ИНН организации"),
    INN_FIND("//input[@data-id='remove-refinancing-credit-link']", "Найти"),
    ORGANIZATION_TEL("//input[@data-id='input-form-field-organizationPhone']", "Телефон работодателя"),
    EMPLOYEE_COUNT("//div[@data-id='select-form-field-employeeCount']", "Количество работающих в организации, чел"),
    CONTRACT_PERPETUAL("//button[@data-id='switcher-form-field-contractsType2']", "Трудовой договор - Бессрочный"),
    ACTIVITY_TYPE("//div[@data-id='select-form-field-activityType']", "Вид деятельности"),
    POSITION("//div[@data-id='select-form-field-position']", "Категория занимаемой должности"),
    // Адрес работы
    COUNTRY_CHANGE("//div[@data-id='search-address-country-0-change-country']", "Страна - Изменить"),
    COUNTRY_NAME("//input[@data-id='countries-popup-input-filter']", "Название"),
    COUNTRY_SHOW("//button[@data-id='countries-popup-view']", "Показать"),
    COUNTRY_CHOICE("//a[@data-id='link-country']", "Найденная страна в списке"),
    ADDRESS("//input[@data-id='search-input-search-address-0']", "Адрес"),
    ADDRESS_MANUAL("//span[text()='Заполнить адрес вручную']/..", "Заполнить адрес вручную"),
    REGION("//input[@data-id='input-form-field-region']", "Регион"),
    CITY("//input[@data-id='input-form-field-city']", "Город"),
    LOCALITY("//input[@data-id='input-form-field-locality']", "Населенный пункт"),
    STREET("//input[@data-id='input-form-field-street']", "Улица"),
    HOUSE_NUM("//input[@data-id='input-form-field-house']", "Номер дома"),
    INDEX("//input[@data-id='input-form-field-index']", "Индекс"),
    START_DATE_JOB("//input[@data-id='date-form-field-startDate-input']", "Дата начала работы в организации"),
    //
    OPTIONS_CONTROL("//ul[@class='options formcontrol']//*[text()='%d']", "ДаДата"),

    // Информация о стаже
    EXPERIENCE_YEARS("//input[@data-id='input-form-field-experience-years']", "Общий стаж (лет)"),
    EXPERIENCE_MONTHS("//input[@data-id='input-form-field-experience-months']", "Общий стаж (месяцев)"),
    EXPERIENCE_LAST_5YEARS("//input[@data-id='input-form-field-fiveYearsExperience-years']", "Общий стаж за последние 5 лет (лет)"),
    EXPERIENCE_LAST_5MONTHS("//input[@data-id='input-form-field-fiveYearsExperience-months']", "Общий стаж за последние 5 лет (месяцев)"),
    LAST5_JOB_COUNT("//input[@data-id='input-form-field-fiveYearsJobCount']", "Количество мест работы за последние 5 лет"),

    //Доходы и расходы
    EXPENSES_TAB("//a[@data-id='incomes-expenses-data-credit-tab']", "Доходы и расходы"),
    CONFIRM_MAIN_REVENUE("//div[@data-id='confirm-docmainIncomeConfirmDocument']", "Способ подтверждения дохода по основному месту работы"),
    REVENUE_2NDFL("//a[@data-id='confirm-doc-link']", "Справка 2-НДФЛ"),
    REVENUE_DATE("//input[@data-id='InputDatePickerDataId-input']", "Дата справки"),
    REVENUE_NUMBER("//input[@data-id='0-number']", "Номер"),
    REVENUE_YEAR("//input[@data-id='0-year']", "Год"),
    REVENUE_ADD("//a[@data-id='button-add-doc']", "Добавить"),
    REVENUE_TABLE("//span[text()='Доход']/ancestor::div[@class='']//div[@class='margin-bottom-10']", "Таблица месяцев"),
    REVENUE_TABLE_MONTH(".//div[contains(@data-id, 'calculatorMonth')]", "Месяц"),
    REVENUE_TABLE_ITEM(".//div[contains(@data-id, 'item')]", "Статья"),
    REVENUE_TABLE_AMOUNT(".//input[contains(@data-id, 'calculatorAmount')]", "Доход"),

    //REVENUE_ADD("//div[@data-id='button-add-doc']", "Добавить"),

    MAIN_REVENUE_AMOUNT("//input[@data-id='input-form-field-avgAmount']", "Среднемесячный доход по основному месту работы"),
    MANDATORY_PAYMENTS("//input[@data-id='input-form-field-requiredPayments']", "Обязательные выплаты"),
    OTHER_PAYMENTS("//input[@data-id='input-form-field-otherExpenses']", "Прочие выплаты"),

    // Иная информация
    OTHER_INFO_TAB("//a[@data-id='other-info-credit-tab']", "Иная информация"),
    MILITARY_RELATION("//div[@data-id='select-form-field-militaryServiceRelation']", "Отношение к воинской службе"),
    BANK_INFO_SOURCE("//div[@data-id='select-form-field-bankInfoSource']", "Источник информации о Банке"),
    ;

    @Getter
    private final String path;

    @Getter
    private final String label;

    PersonalDataPage(String path, String label) {
        this.path = path;
        this.label = label;
    }
}
