package efr.pagesEFR;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utils.DataProvider;
import utils.DriverUtils;
import utils.PageEnum;
import utils.TestUtils;

import java.util.logging.Logger;

@Component
public class BaseClass {

    private TestUtils testUtils;

    private DriverUtils driverUtils;

    private Logger logger;

    private DataProvider env;

    private String checkBox = "/span/span";

    @Autowired
    public BaseClass(TestUtils testUtils, DriverUtils driverUtils, Logger logger, DataProvider env) {
        this.testUtils = testUtils;
        this.driverUtils = driverUtils;
        this.logger = logger;
        this.env = env;
    }

    private WebElement webElement;

    /**
     * Селект ЕФРа
     *
     * @param select элемент списка
     * @param value значение которое надо выбрать в этом списке
     */
    public void choseSelect(PageEnum select, String value){
        String expected = env.getProperty(String.valueOf(value));
        stepChose(select.getLabel(), expected == null ? value : expected, select.getPath());
    }

    /**
     * Селект ЕФРа
     *
     * @param select элемент списка
     * @param search элемент, в котором находится селект
     * @param value значение которое надо выбрать в этом списке
     */
    public void choseSelect(PageEnum select, WebElement search, String value){
        String expected = env.getProperty(String.valueOf(value));
        webElement = search;
        stepChoseSearch(select.getLabel(), expected == null ? value : expected, select.getPath());
        //testUtils.performClick(select, search);
        //testUtils.performClick(MainPage.SELECT_EFR, value);
    }

    /**
     * Селект ЕФРа
     *
     * @param select элемент списка
     * @param replacement строка, для замены в пути select
     * @param value значение которое надо выбрать в этом списке
     */
    public void choseSelect(PageEnum select, String replacement, String value){
        String expected = env.getProperty(String.valueOf(value));
        stepChose(testUtils.replacement(select.getLabel(), replacement)
                , expected == null ? value : expected
                , testUtils.replacement(select.getPath(), replacement));
        //testUtils.performClick(select, replacement);
        //testUtils.performClick(MainPage.SELECT_EFR, value);
    }

    /**
     * Селект ЕФРа
     *
     * @param select элемент списка
     * @param value значение которое надо выбрать в этом списке
     */
    public void choseSelectDaData(PageEnum select, String search, String value){
        String expected = env.getProperty(String.valueOf(value));
        testUtils.performSendKeys(select.getPath(), select.getLabel(), search);
        driverUtils.sendKeys(select.getPath(), Keys.ARROW_RIGHT);
        stepChoseDaData(select.getLabel(), expected == null ? value : expected);
    }

    @Step("Выбираем из списка \"{0}\" значение \"{1}\"")
    private void stepChose(String select, String value, String path){
        driverUtils.click(path);
        driverUtils.click(testUtils.replacement(MainPage.SELECT_EFR.getPath(), value));
    }

    @Step("Выбираем из списка \"{0}\" значение \"{1}\"")
    private void stepChoseSearch(String select, String value, String path){
        webElement.findElement(By.xpath(path)).click();
        driverUtils.click(testUtils.replacement(MainPage.SELECT_EFR.getPath(), value));
    }

    @Step("Выбираем из списка ДаДаты \"{0}\" значение \"{1}\"")
    private void stepChoseDaData(String select, String value){
        driverUtils.click(testUtils.replacement(MainPage.OPTIONS_CONTROL.getPath(), value));
    }

    StringBuilder stringBuilder = new StringBuilder("01234567890123456789");

    /**
     * Проверка того, сколько символов можно ввести в поле (вводяться только цифры)
     *
     * @param element элемент для ввода текста
     * @param length длина поля, которая должна быть
     */
    public void inputLengthValidator(PageEnum element, int length){
        String temp = "0123456789";
        while (stringBuilder.length() < length + 1){
            stringBuilder.append(temp);
        }
        testUtils.performSendKeys(element.getPath(), element.getLabel(), stringBuilder.toString());
        testUtils.assertEquals(length, testUtils.performGetValue(element.getPath(), element.getLabel()).length());
        testUtils.performClear(element.getPath(), element.getLabel());
    }

    /**
     * Берем инт из строки формата 999.99
     * @param s строки
     * @return int без дробной части
     */
    public int getInt(String s){
        String temp = testUtils.removeSymbols(s);
        return temp.contains(".") ? Integer.parseInt(temp.substring(0, temp.length() - 3)): Integer.parseInt(temp);
    }

    public boolean isCheckBoxOn(PageEnum element){
        return !testUtils.performGetClass(element.getPath().concat(checkBox), element.getLabel()).contains("disagree");
    }
}
