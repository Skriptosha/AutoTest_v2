package efr.pagesEFR;

import lombok.Getter;
import utils.PageEnum;

public enum PopulationCitiesPage implements PageEnum {

    TABLE("//div[@data-id='population-city-grid']//tbody", "Таблица городов"),
    ADD("//button[@data-id='button-create']", "Добавить"),
    REGION_SEARCH("//input[@data-id='search-text']", "Поле ввода региона"),
    SHOW( "//button[@data-id='search']", "Показать"),
    CLEAR_REGION("//button[@data-id='clearSearch']", "Очистить результаты поиска"),

    DELETE(".//a[@data-id='button-remove']", "Удалить"),
    DELETE_YES("//button[@data-id='confirmYesButton']", "Удалить - Да"),

    PREV("//a[@data-id='gridPrevIcon']", "Предыдущая страница"),
    NEXT("//a[@data-id='gridNextIcon']","Следущая страница"),
    ROW("//div[@data-id='population-city-grid']//tbody//td[text()='%d']/..","Строка таблицы"),
    FIRST_ROW("//div[@data-id='population-city-grid']//tbody//tr//td[2]","Первая строка таблицы"),

    TOWNINFO("//div[@data-id='details-population-city-panel']","Информация о городе"),
    FIAS("//input[@data-id='input-fias-data-id']","Код ФИАС"),
    TOWN("//input[@data-id='input-name-data-id']","Город"),
    REGION("//input[@data-id='input-gni-data-id']","Регион"),
    SIGN_NBSM("//input[@data-id='input-nbsm-data-id']","Признак для NBSM"),
    SAVE_WHEN_NEW("//button[@data-id='save-button']","Сохранить"),
    SAVE_WHEN_EDIT("//button[@data-id='clearSearch']","Сохранить"),
    BACK("//button[@data-id='back-button']","Назад"),

    POPUP("//div[@data-id='popup-content-message-content']","Обратите внимание"),
    CLOSE_POPUP("//button[@data-id='alertCloseButton']","Закрыть");

    @Getter
    private final String path;

    @Getter
    private final String label;

    PopulationCitiesPage(String path, String label) {
        this.path = path;
        this.label = label;
    }
}
