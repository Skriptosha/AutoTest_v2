package pages;

import org.springframework.stereotype.Component;

/**
 * Описание элементов страницы
 */
@Component
public class MainPage {

    private String market = "//a[@data-id='market']";


    private String electronics = "//span[contains(text(), 'Электроника')]/..";

    private String smartphone = "//a[text()='Смартфоны']";

    private String headphones = "//a[text()='Наушники и Bluetooth-гарнитуры']";

    private String atThePriceOf = "//a[text()='по цене']";

    private String price = "//div[@class='price']";

    private String priceFrom = "//input[@id='glpricefrom']";

    private String manufacturer = "//fieldset[@data-autotest-id='7893318']";

    private String showAll = ".//button[text()='Показать всё']";

    private String manufacturerInput = "//input[@name='Поле поиска']";

    private String manufacturerFromFilter = "//span[text()='%d']/..";

    private String table = "//div[contains (@class, 'n-filter-applied-results__content')]/div[1]";

    private String elementsOfTable = ".//div[contains (@class, 'n-snippet-cell2 i-bem b-zone')]";

    private String title = ".//div[@class='n-snippet-cell2__title']";

    private String searchInput = "//input[@id='header-search']";

    private String searchButton = "//span[text()='Найти']/..";

    public String getManufacturerFromFilter(String manufacturer) {
        return manufacturerFromFilter.replace("%d", manufacturer);
    }

    public String getMarket() {
        return market;
    }

    public String getElectronics() {
        return electronics;
    }

    public String getSmartphone() {
        return smartphone;
    }

    public String getHeadphones() {
        return headphones;
    }

    public String getAtThePriceOf() {
        return atThePriceOf;
    }

    public String getPrice() {
        return price;
    }

    public String getPriceFrom() {
        return priceFrom;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getShowAll() {
        return showAll;
    }

    public String getManufacturerInput() {
        return manufacturerInput;
    }

    public String getTable() {
        return table;
    }

    public String getElementsOfTable() {
        return elementsOfTable;
    }

    public String getTitle() {
        return title;
    }

    public String getSearchInput() {
        return searchInput;
    }

    public String getSearchButton() {
        return searchButton;
    }

}
