import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import pages.AuthPage;
import pages.MainPage;
import pages.PrimaryDataPage;
import utils.DriverUtils;
import utils.RandomNames;

import java.util.logging.Logger;

/**
 * Тестовые сценарии
 */
@Component
public class TEST1 extends DriverUtils {

    private static final String syp = "http://seriyps.ru/postget/";

    @Autowired
    private AuthPage authPage;

    @Autowired
    private MainPage FirstNameList;


    @Autowired
    private PrimaryDataPage primaryDataPage;

    @Autowired
    private DriverUtils driverUtils;

    @Autowired
    WebDriver webDriver;

    @Autowired
    private Environment environment;

    @Autowired
    private Logger logger;
    int a = 111111; // Начальное значение диапазона - "от"
    int b = 999999; // Конечное значение диапазона - "до"
    int rn1 = a + (int) (Math.random() * b);
    int rn2 = a + (int) (Math.random() * b);
    String smsCode = "12345";



    @Test
    @DisplayName("Test case 1")
    public void test1() throws InterruptedException {
        driverUtils.getURL(syp)
                .sendKeys(authPage.getUrl(), Keys.CONTROL, "a")
                .sendKeys(authPage.getUrl(), "https://test02.rshb.ru/ib6/creditnoauth")
                .click(authPage.getMethod())
                .sendKeys(authPage.getMethod(), Keys.DOWN, Keys.ENTER)
                .sendKeys(authPage.getNam_1(), "url")
                .sendKeys(authPage.getVal_1(), "https://branches.go.rshbank.ru/natural/application-for-credit/?type=mortgage-credit-ref&time=1&period=")
                .sendKeys(authPage.getNam_2(), "CancelURL")
                .sendKeys(authPage.getVal_2(), "https://branches.go.rshbank.ru/natural/application-for-credit/cancel.php")
                .sendKeys(authPage.getNam_3(), "amount")
                .sendKeys(authPage.getVal_3(), "100000")
                .sendKeys(authPage.getNam_4(), "term")
                .sendKeys(authPage.getVal_4(), "12")
                .sendKeys(authPage.getNam_5(), "name")
                .sendKeys(authPage.getVal_5(), RandomNames.FirstName())
                .sendKeys(authPage.getNam_6(), "patronymic")
                .sendKeys(authPage.getVal_6(), RandomNames.MiddleName())
                .sendKeys(authPage.getNam_7(), "surname")
                .sendKeys(authPage.getVal_7(), RandomNames.LastName())
                .sendKeys(authPage.getNam_8(), "birthday")
                .sendKeys(authPage.getVal_8(), "02.05.1985")
                .sendKeys(authPage.getNam_9(), "mobilePhone")
                .sendKeys(authPage.getVal_9(), "9998" + rn1)
                .sendKeys(authPage.getNam_10(), "email")
                .sendKeys(authPage.getVal_10(), "testdbo2@go.rshbank.ru")
                .sendKeys(authPage.getNam_11(), "productCode")
                .sendKeys(authPage.getVal_11(), "П26")
                .sendKeys(authPage.getNam_12(), "tariffCode")
                .sendKeys(authPage.getVal_12(), "УДАЛИТЬ")
                .sendKeys(authPage.getNam_13(), "requestId")
                .sendKeys(authPage.getVal_13(), "892367265")
                .sendKeys(authPage.getNam_14(), "branchCode")
                .sendKeys(authPage.getVal_14(), "0000")
                .sendKeys(authPage.getNam_15(), "officeCode")
                .sendKeys(authPage.getVal_15(), "0003")
                .sendKeys(authPage.getVal_12(), Keys.CONTROL, "a", Keys.BACK_SPACE)
                .click(authPage.getSendit());
        /**
         * Первичные данные
         */
        Thread.sleep(5000);
//        WebDriver driver = (WebDriver) driverUtils;
        {//переключение на новое окно
            for (String winHandle : webDriver.getWindowHandles()) {
                webDriver.switchTo().window(winHandle); // switch focus of WebDriver to the next found window handle (that your newly opened window)
            }
        }
//        if(wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='checkbox-style']\n")))==null);

        click(primaryDataPage.getCheckboxAccept())
                .click(primaryDataPage.getAccept())
                .sendKeys(primaryDataPage.getSmsVerification(),"12345")

        ;
Thread.sleep(5000);
//
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
        }
    }
