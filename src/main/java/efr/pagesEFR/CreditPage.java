package efr.pagesEFR;

import lombok.Getter;
import utils.PageEnum;

public enum CreditPage implements PageEnum {
    //Основные элементы кредитной заявки - загрузка, продолжить и подтверждение
    //Загрузка
    PRELOADER("//div[text()='Подождите, информация загружается']", "Подождите, информация загружается"),
    CONFIRM("//button[@data-id='confirmYesButton']", "Да"),
    CONTINUE("//span[text()='Продолжить']//..", "Продолжить"),

    // Инициация процесса
    EDIT("//button[@data-id='buttonEdit']", "Редактировать"),
    //Продолжить (CONTINUE)

    // Кредитный калькулятор (2 страница)
    CREDIT_PRODUCT("//div[@data-id='creditProduct']", "Кредитный продукт"),
    CURRENCY("//div[@data-id='creditProduct']", "Валюта кредита"),
    AMOUNT("//input[@data-id='requestAmount']", "Запрашиваемая сумма"),
    TERM("//input[@data-id='term']", "Срок"),
    INCOME("//input[@data-id='income']", "Совокупный доход"),
    RATE("//div[contains(text(), '% годовых')]", "Ставка"),
    BASE_RATE("//span[text()='%d']", "Базовая ставка"),
    ALLOWANCES("//span[text()='%d']", "Надбавки"),
    MESSAGE("//div[contains(text(), 'Максимальная сумма кредита')]", "Сообщение"),
    CALCULATE("//button[@data-id='buttonCalculate']", "Рассчитать"),

    // Сообщение с расчетом
    SUBMIT("//button[@data-id='buttonNext']", "Оформить заявку"),

    //Кредитная заявка (3 страница)
    //Параметры заявки
    CREDIT_PARAMS("//div[@data-id='credit-params-credit-tab']", "Параметры заявки"),
    CREDIT_PURPOSE("//div[@data-id='select-form-field-creditPurpose']", "Цель кредитования"),

    INSURANCE("//input[@data-id='toggle-form-field-insuranceParams']", "Личное страхование"),
    INSURANCE_COLLECTIVE("//button[@data-id='switcher-form-field-insuranceTypecollective']", "Коллективное"),
    INSURANCE_INDIVIDUAL("//button[@data-id='switcher-form-field-insuranceTypeindividual']", "Индивидуальное"),
    INSURANCE_PROGRAM("//div[@data-id='select-form-field-insuranceProgram']", "Страховая программа"),
    INSURANCE_AMOUNT("//input[@data-id='input-form-field-insuranceAmount']", "Страховая плата"),

    //Рефинансирование
    REF_BLOCK("//div[@class='display-table']/div", "Список кредитов на рефинансирование"),

    REF_ADD("//a[@data-id='link-create']", "Добавить"),
    REF_AMOUNT("//input[@data-id='input-amount%d']", "Рефинансируемые кредиты - сумма, строка %d"),
    REF_CURRENCY("//div[@data-id='select-currency%d']", "Рефинансируемые кредиты - валюта, строка %d"),
    REF_ADDITIONAL_AMOUNT("//input[@data-id='additionalAmount']", "Дополнительная сумма"),
    REF_REQUEST_AMOUNT("//input[@data-id='requestAmount']", "Запрашиваемая сумма"),

    //Сохранить
    SAVE("//button[@data-id='save']", "Сохранить"),
    //Продолжить (CONTINUE) (continue)

    //Визуальная оценка
    //Продолжить (CONTINUE) (continue)

    //Рефинансирование
    //CREDIT_PRODUCT("//input[@value='%d']", "Кредитный продукт"),
    REF_CONTAINER("//div[@data-id='edit-credit-params-tab-form']//div[@class='fieldset-content']", "Блок рефинансируемого кредита"),
    REF_RSHB_TRUE(".//button[@data-id='switcher-form-field-rshbCredittrue']", "Кредит в РСХБ - да"),
    REF_BANK(".//input[@data-id='input-form-field-bank']", "Банк"),
    REF_CONTRACT_TYPE(".//div[@data-id='select-form-field-type']", "Тип договора"),
    REF_DATE_FROM(".//input[@data-id='date-form-field-dateFrom-input']", "Дата заключения"),
    REF_CREDIT_AMOUNT(".//input[@data-id='input-form-field-creditAmount']", "Сумма кредита"),
    REF_DEBT(".//input[@data-id='input-form-field-debt']", "Остаток ссудной задолженности"),

    //Запрошенные параметры
    BYPRODUCT("//div[@data-id='sub-product-collapse']", "Субпродукт"),
    BYPRODUCT_NOT_APPLY("//div[@data-id='sub-product-collapse']//tr//td", "Субпродукты, не удовлетворяющие параметрам заявки:"),
    CREDIT_RATE("//div[@data-id='rate-credit-collapse']//span[@class='collapse-title-text']", "Рассчитанная ставка"),
    //specification-tab-credit-tab Выбор варианта кредитования

    //Продолжить (CONTINUE)

    //МАТРИЦА РЕШЕНИЙ (Выбор варианта кредитования)
    TABLE_DECISION_MATRIX ("//div[@class='table-wrap']//tr[contains(@data-id,'option-selector')]", "Таблица матрицы решений"),
    TABLE_DM_FIRST_ROW("//div[@class='table-wrap']//tr[contains(@data-id,'option-selector')][1]//label", "Выбираем первую строку"),
    TABLE_DM_CHECK_ROW(".//label", "Выбираем нужную строку"),
    LOAN_AMOUNT("//input[@data-id='input-form-field-amount']", "Сумма кредита"),
    TERM_1("//input[@data-id='input-form-field-term']", "Срок"),
    INSURANCE_FEE("//div[@data-id='input-form-field-insuranceAmount']", "Страховая плата"),
    //Продолжить (CONTINUE)

    //Документы
    SHOW_DOC_ALL("//input[@data-id='checkbox-form-field-issuanceAccount']", "Показать документы - Все"),
    ADD_DOCUMENT("//a[@data-id='add-doc-id']", "Добавить"),

    //Приложение скан-копии документов
    DOCUMENT_TYPE("//div[@data-id='documentType']", "Тип документа"),
    SCAN_UPLOAD("//input[@data-id='field-scan-attach-file-upload']", "Приложить файл"),
    ADD_ATTACH("//button[@data-id='attach-scan-button']", "Добавить"),
    PRINT("//button[@data-id='print']", "Печать"),
    //Продолжить (CONTINUE)
    //Перевести кредитную заявку на следующий шаг обработки? (CONFIRM),

    // Последняя страница - Подтверждение контролирующим работником (статус заявки - Контроль)
    STATUS_LOAN("//span[text()='Статус']/ancestor::div[@class='field undefined']//div[@class='field-value']"
            , "Статус КЗ"),
    NOTIFICATION("//div[@class='notification notification-notice']", "Окно уведомления"),
    //CREDIT_PRODUCT("//div[@data-id='creditProduct']", "Кредитный продукт"),
    ;

    @Getter
    private final String path;

    @Getter
    private final String label;

    CreditPage(String path, String label) {
        this.path = path;
        this.label = label;
    }
}
