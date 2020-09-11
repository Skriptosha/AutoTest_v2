package pagesEFR;

import lombok.Getter;
import pagesOnlineApp.PageEnum;

public enum MainPage implements PageEnum {

    DICTIONARIES("//li[@data-id='dictionaries']", "Справочники"),
    CLIENTS("//li[@data-id='menuItemClients']", "Клиенты"),
    TASKS("//li[@data-id='menuItemTasks']", "Задачи"),
    ASSIGNED("//button[@data-id='assigned']", "Мои задачи"),
    POPULATION_CITIES("//div[@data-id='population-cities']", "Справочник городов"),
    EXIT("//a[@data-id='exit']", "Выход"),
    // Селект который используется в ЕФР
    SELECT_EFR("//div[@class='Select-menu-outer']//*[text()='%d']", "Выбираем значение из списка - \"%d\""),
    // Мои задачи
    MY_TASK_TABLE("//tbody//tr", "Таблица \"Мои задачи\""),
    MY_TASK_TABLE_ROW(".//span[text()='Исполнить']", "Исполнить"),

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
