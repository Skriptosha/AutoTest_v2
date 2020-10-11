package efr.pagesEFR;

import lombok.Getter;
import utils.PageEnum;

public enum ClientsPage implements PageEnum {

    PASSPORT_SERIES("//input[@data-id='field-name-docSeries']", "Серия"),
    PASSPORT_NUMBER("//input[@data-id='field-name-docNumber']", "Номер"),
    POPULATION_CITIES("//div[@data-id='population-cities']", "Справочник городов"),
    SEARCH("//button[@data-id='search-button']", "Найти"),
    CLEAR("//button[@data-id='clean-button']", "Очистить"),
    APPLY_LOAN("//a[@data-id='new-credits-block-collapse-right-data']", "Оформить кредит")

    ;

    @Getter
    private final String path;

    @Getter
    private final String label;

    ClientsPage(String path, String label) {
        this.path = path;
        this.label = label;
    }
}
