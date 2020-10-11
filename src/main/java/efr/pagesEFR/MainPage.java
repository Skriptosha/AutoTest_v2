package efr.pagesEFR;

import lombok.Getter;
import utils.PageEnum;

public enum MainPage implements PageEnum {

    DICTIONARIES("//li[@data-id='dictionaries']", "Справочники"),
    CLIENTS("//li[@data-id='menuItemClients']", "Клиенты"),
    TASKS("//li[@data-id='menuItemTasks']", "Задачи"),
    ASSIGNED("//button[@data-id='assigned']", "Мои задачи"),
    NOT_ASSIGNED("//button[@data-id='not_assigned']", "Доступные задачи"),
    POPULATION_CITIES("//div[@data-id='population-cities']", "Справочник городов"),
    EXIT("//a[@data-id='exit']", "Выход"),
    // Селект который используется в ЕФР
    SELECT_EFR("//div[@class='Select-menu-outer']//*[normalize-space(text())='%d']", "Выбираем значение из списка - \"%d\""),
    // Выпадающий список Дадаты
    OPTIONS_CONTROL("//ul[@class='options formcontrol']//*[contains(text(),'%d')]", "Выбираем значение из списка - \"%d\""),
    // Мои задачи
    MY_TASK_TABLE("//tbody//tr", "Таблица \"Мои задачи\""),
    MY_TASK_TABLE_ROW_EX(".//span[text()='Исполнить']", "Исполнить"),
    MY_TASK_TABLE_ROW_GET_EX(".//span[text()='Взять и исполнить']", "Взять и исполнить"),
    MY_TASK_CONFIRM("//span[text()='Подтвердить']/..", "Подтвердить"),
    CONFIRM("//button[@data-id='confirmYesButton']", "Да"),


    ;


    @Getter
    private final String path;

    @Getter
    private final String label;

    MainPage(String path, String label) {
        this.path = path;
        this.label = label;
    }
}
