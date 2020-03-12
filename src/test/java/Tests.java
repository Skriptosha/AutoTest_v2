//import io.qameta.allure.junit4.DisplayName;
//import org.junit.Assert;
//import org.junit.Test;
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebElement;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.env.Environment;
//import pages.MainPage;
//import utils.DriverUtils;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.logging.Logger;
//
///**
// * Тестовые сценарии
// */
//public class Tests extends DriverUtils {
//
//    private static final String yandex = "https://yandex.ru";
//
//    @Autowired
//    private MainPage mainPage;
//
//    @Autowired
//    private DriverUtils driverUtils;
//
//    @Autowired
//    private Environment environment;
//
//    @Autowired
//    private Logger logger;
//
//    @Test
//    @DisplayName("Test case 1")
//    public void test1() {
//        driverUtils.getURL(yandex)
//                .click(mainPage.getMarket())
//                .click(mainPage.getElectronics())
//                .click(mainPage.getSmartphone())
//                .sendKeys(mainPage.getPriceFrom(), "20000")
//                .findElement(mainPage.getManufacturer())
//                .findElement(By.xpath(mainPage.getShowAll()))
//                .click();
//        WebElement webElement = driverUtils.findElement(mainPage.getTable());
//        driverUtils.sendKeys(mainPage.getManufacturerInput(), "Apple")
//                .click(mainPage.getManufacturerFromFilter("Apple"))
//                .clear(mainPage.getManufacturerInput())
//                .sendKeys(mainPage.getManufacturerInput(), "Samsung")
//                .click(mainPage.getManufacturerFromFilter("Samsung"))
//                .staleness(webElement);
//        List<WebElement> elements = driverUtils.findElement(mainPage.getTable())
//                .findElements(By.xpath(mainPage.getElementsOfTable()));
//        Assert.assertEquals(48, elements.size());
//        String first = elements.get(0).findElement(By.xpath(mainPage.getTitle())).getText();
//        webElement = driverUtils.findElement(mainPage.getTable());
//        driverUtils.sendKeys(mainPage.getSearchInput(), first)
//                .click(mainPage.getSearchButton())
//                .staleness(webElement);
//        String second = driverUtils.findElement(mainPage.getTable())
//                .findElements(By.xpath(mainPage.getElementsOfTable())).get(0)
//                .findElement(By.xpath(mainPage.getTitle()))
//                .getText();
//        Assert.assertEquals(first, second);
//    }
//
//    @Test
//    @DisplayName("Test case 2")
//    public void test2() {
//        driverUtils.getURL(yandex)
//                .click(mainPage.getMarket())
//                .click(mainPage.getElectronics())
//                .click(mainPage.getHeadphones())
//                .sendKeys(mainPage.getPriceFrom(), "5000")
//                .findElement(mainPage.getManufacturer())
//                .findElement(By.xpath(mainPage.getShowAll()))
//                .click();
//        WebElement webElement = driverUtils.findElement(mainPage.getTable());
//        driverUtils.sendKeys(mainPage.getManufacturerInput(), "Beats")
//                .click(mainPage.getManufacturerFromFilter("Beats"))
//                .staleness(webElement);
//        List<WebElement> elements = driverUtils.findElement(mainPage.getTable())
//                .findElements(By.xpath(mainPage.getElementsOfTable()));
//        Assert.assertEquals(17, elements.size());
//        String first = elements.get(0).findElement(By.xpath(mainPage.getTitle())).getText();
//        webElement = driverUtils.findElement(mainPage.getTable());
//        driverUtils.sendKeys(mainPage.getSearchInput(), first)
//                .click(mainPage.getSearchButton())
//                .staleness(webElement);
//        String second = driverUtils.findElement(mainPage.getTable())
//                .findElements(By.xpath(mainPage.getElementsOfTable())).get(0)
//                .findElement(By.xpath(mainPage.getTitle()))
//                .getText();
//        Assert.assertEquals(first, second);
//    }
//
//    @Test
//    @DisplayName("Test case 3")
//    public void test3() {
//        driverUtils.getURL(yandex)
//                .click(mainPage.getMarket())
//                .click(mainPage.getElectronics())
//                .click(mainPage.getSmartphone());
//        WebElement webElement = driverUtils.findElement(mainPage.getTable());
//        driverUtils.click(mainPage.getAtThePriceOf())
//                .staleness(webElement);
//        List<WebElement> elements = driverUtils.findElement(mainPage.getTable())
//                .findElements(By.xpath(mainPage.getPrice()));
//        logger.info(String.valueOf(elements.size()));
//        Integer[] prices  = new Integer[elements.size()] , prices2 = new Integer[elements.size()];
//        for (int i = 0; i < elements.size(); i++) {
//            prices[i] = Integer.valueOf(elements.get(i).getText().replace(" ₽", "")
//                    .replace(" ", ""));
//        }
//        System.arraycopy(prices, 0, prices2, 0, prices.length);
//        Arrays.sort(prices2);
//        for(int i = 0; i < prices.length; i++){
//            Assert.assertEquals(prices[i], prices2[i]);
//        }
//    }
//}
