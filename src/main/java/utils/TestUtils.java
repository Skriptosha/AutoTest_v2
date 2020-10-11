package utils;

import io.qameta.allure.Step;
import lombok.NonNull;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@Component
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConf.class})
public class TestUtils {

    @Rule
    @Autowired
    public JUnitUtils jUnitUtils;

    @Autowired
    private WebDriver webDriver;

    @Autowired
    private Logger logger;

    @Autowired
    private DataProvider env;

    @Autowired
    private DriverUtils driverUtils;

    private Object testClass;

    /*
        @Autowired
        public void setWebDriver(WebDriver webDriver) {
            this.webDriver = webDriver;
        }

        @Autowired
        public void setLogger(Logger logger) {
            this.logger = logger;
        }

        @Autowired
        public void setEnv(DataProvider env) {
            this.env = env;
        }

        @Autowired
        public void setDriverUtils(DriverUtils driverUtils) {
            this.driverUtils = driverUtils;
        }

         */

    @AfterClass
    public static void tearDown() {
        WebDriver webDriver = SpringConf.getBean(WebDriver.class);
        SpringConf.getBean(DriverUtils.class).quit(webDriver);
        SpringConf.getBean(CommonUtils.class).setEnvironment(webDriver);
    }

    /**
     * Удаляет символы из строки согласно проперти файлу mainSettings
     *
     * @param exp строка для удаления символов
     * @return модифицированная строка
     */
    public String removeSymbols(@NonNull String exp) {
        return exp.replaceAll(Objects.requireNonNull(env.getProperty("regExp")), "");
    }

    /**
     * Проверка Assert.assertEquals параметр expectedKey пытается найти в проперт файле,
     * если не находит, пробует сначала сравнить параметры как есть, если результат false
     * то удаляет символы (указаны в проперти по ключу regExp) через метод removeSymbols()
     * и пробует сравнить повторно. Сравнивает как Текст, так и другие типы
     *
     * @param expectedKey ожидаемый Обьект
     * @param actual      Обьект, полученный на самом деле
     */
    public void assertEquals(Object expectedKey, Object actual) {
        if (expectedKey instanceof String && actual instanceof String) {
            logger.info("actual: " + actual);
            String expected = env.getProperty(String.valueOf(expectedKey));
            logger.info("Значение из проперти файла: " + expected);
            if (expected == null) stepAssertEquals(expectedKey, actual);
            else {
                if (expected.equals(actual)) stepAssertEquals(expected, actual);
                else stepAssertEquals(removeSymbols(expected), removeSymbols(String.valueOf(actual)));
            }
        } else {
            stepAssertEquals(expectedKey, actual);
        }
    }

    String massage;

    public void assertTrueContains(String infoMessage, String chek, String contains) {
        massage = infoMessage + contains;
        stepAssertTrueContains(contains, chek.contains(contains));
    }

    public void assertFalseContains(String infoMessage, String chek, String contains) {
        massage = infoMessage;
        stepAssertFalseContains(contains, chek.contains(contains));
    }

    /**
     * Замена символа %d в строке и поиск значения в тестовых данных, если такой имеется
     * Если параметр
     *
     * @param expression выражение, в которм нужно искать
     * @param newChars   подстрока для поиска
     * @return подстроку после замены и поиска в тестовых данных
     */
    public String replacement(String expression, Object newChars) {
        String value;
        String expected;
        String newCharsString = String.valueOf(newChars);
        if (newCharsString.length() > 2 | (newCharsString.length() < 3 && !newCharsString.matches(".*\\d.*"))) {
            expected = env.getProperty(String.valueOf(newChars));
            return expected == null ? CommonUtils.customReplaceAll(expression, newCharsString)
                    : CommonUtils.customReplaceAll(expression, expected);
        } else if (newCharsString.matches(".*\\d.*")) {
            expected = CommonUtils.customReplaceAll(expression, newCharsString);
            return (value = env.getProperty(expected)) != null ? value : expected;
        } else {
            return expression;
        }
    }

    @Step("Сравниваем значения")
    private void stepAssertEquals(Object expected, Object actual) {
        Assert.assertEquals(expected, actual);
    }

    @Step("Проверяем текст + \"{0}\"")
    private void stepAssertTrueContains(String contains, Boolean bool) {
        Assert.assertTrue(massage, bool);
    }

    @Step("Проверяем текст + \"{0}\"")
    private void stepAssertFalseContains(String contains, Boolean bool) {
        Assert.assertFalse(massage, bool);
    }

    private WebElement webElement;

    public TestUtils performSendKeys(String pagePath, String pageLabel, CharSequence... key) {
        /*logger.info("Размер массива key: " + key.length + "\n"
                + "Значение key[0]: " + key[0].toString());
        */
        String value;
        if (key.length == 1) {
            value = env.getProperty(key[0].toString());
            driverUtils.clear(pagePath);
            stepSendKeys(pageLabel, pagePath, value == null ? key[0] : value);
        } else {
            driverUtils.clear(pagePath);
            stepSendKeys(pageLabel, pagePath, key);
        }
        return this;
    }

    /*public TestUtils performSendKeysReplace(PageEnum page, String replacement, CharSequence... key) {
        path = replacement(page.getPath(), replacement);
        //logger.info(path);
        String value;
        if (key.length == 1) {
            value = env.getProperty(key[0].toString());
            //logger.info(String.valueOf(key));
            stepSendKeys(replacement(page.getLabel(), replacement), value == null ? key[0] : value);
        } else {
            //logger.info(Arrays.toString(key));
            stepSendKeys(replacement(page.getLabel(), replacement), key);
        }
        return this;
    }
    */

    public TestUtils performSendKeys(String pagePath, String pageLabel, WebElement search, CharSequence... key) {
        webElement = search;
        String value;
        if (key.length == 1) {
            value = env.getProperty(key[0].toString());
            driverUtils.clear(pagePath);
            stepSendKeysParent(pageLabel, pagePath, value == null ? key[0] : value);
        } else {
            driverUtils.clear(pagePath);
            stepSendKeysParent(pageLabel, pagePath, key);
        }
        return this;
    }

    public void staleness(PageEnum page) {
        driverUtils.staleness(page.getPath(), page.getLabel());
    }

    /*public WebElement performFind(PageEnum page, String replacement) {
        path = env.getProperty(replacement(page.getPath(), replacement));
        return stepFind(replacement(page.getLabel(), replacement));
    }

     */

    public WebElement performFind(String pagePath, String pageLabel) {
        return stepFind(pageLabel, pagePath);
    }

    public List<WebElement> performFindElements(String pagePath, String pageLabel) {
        return stepFindElements(pageLabel, pagePath);
    }

    public TestUtils performClick(String pagePath, String pageLabel) {
        stepClick(pageLabel, pagePath);
        return this;
    }

    /*public TestUtils performClick(PageEnum page, String replacement) {
        String expected = env.getProperty(replacement);
        path = env.getProperty(replacement(page.getPath(), expected == null ? replacement : expected));
        stepClick(replacement(page.getLabel(), replacement));
        return this;
    }

     */

    public TestUtils performClick(String pagePath, String pageLabel, WebElement parentElement) {
        webElement = parentElement;
        stepClickParent(pageLabel, pagePath);
        return this;
    }

    public TestUtils performClear(String pagePath, String pageLabel) {
        stepClear(pageLabel, pagePath);
        return this;
    }

    public String performGetText(String pagePath, String pageLabel) {
        return stepGetText(pageLabel, pagePath);
    }

    public String performGetValue(String pagePath, String pageLabel) {
        return stepGetValue(pageLabel, pagePath);
    }

    public String performGetClass(String pagePath, String pageLabel) {
        return stepGetClass(pageLabel, pagePath);
    }

    public void performAndAssertIsEnabled(String pagePath, String pageLabel, boolean expected) {
        if (expected) {
            stepIsEnabledTrue(pageLabel, pagePath);
        } else {
            stepIsEnabledFalse(pageLabel, pagePath);
        }
    }

    public void performAndAssertIsSelected(String pagePath, String pageLabel, boolean expected) {
        if (expected) {
            stepIsSelectedTrue(pageLabel, pagePath);
        } else {
            stepIsSelectedFalse(pageLabel, pagePath);
        }
    }

    @Step("Вводим в поле \"{0}\" значение \"{2}\"")
    private void stepSendKeys(String label, String pagePath, CharSequence... value) {
        driverUtils.sendKeys(pagePath, value);
    }

    @Step("Вводим в поле \"{0}\" значение \"{2}\"")
    private void stepSendKeysParent(String label, String pagePath, CharSequence... value) {
        logger.info("Печатаем текст " + Arrays.toString(value) + ", элемент: " + pagePath);
        webElement.findElement(By.xpath(pagePath)).sendKeys(value);
    }

    @Step("Нажимаем на элемент \"{0}\"")
    private void stepClick(String label, String pagePath) {
        driverUtils.click(pagePath);
    }

    @Step("Нажимаем на элемент \"{0}\"")
    private void stepClickParent(String label, String pagePath) {
        logger.info("Нажимаем на элемент " + pagePath);
        webElement.findElement(By.xpath(pagePath)).click();
    }

    @Step("Ищем элемент \"{0}\"")
    private WebElement stepFind(String label, String pagePath) {
        return driverUtils.findElement(pagePath);
    }

    @Step("Ищем элемент \"{0}\"")
    private List<WebElement> stepFindElements(String label, String pagePath) {
        return driverUtils.findElements(pagePath);
    }

    @Step("Очищаем форму \"{0}\"")
    private void stepClear(String label, String pagePath) {
        driverUtils.clear(pagePath);
    }

    @Step("Проверяем что элемент \"{0}\" активен")
    private void stepIsEnabledTrue(String label, String pagePath) {
        Assert.assertTrue("Элемент " + label + "не активен!"
                , driverUtils.findElement(pagePath).isEnabled());
    }

    @Step("Проверяем что элемент \"{0}\" не активен")
    private void stepIsEnabledFalse(String label, String pagePath) {
        Assert.assertFalse("Элемент " + label + "активен!"
                , driverUtils.findElement(pagePath).isEnabled());
    }

    @Step("Проверяем что checkbox \"{0}\" выбран")
    private void stepIsSelectedTrue(String label, String pagePath) {
        Assert.assertTrue("Элемент " + label + "не выбран!"
                , driverUtils.findElement(pagePath).isSelected());
    }

    @Step("Проверяем что checkbox \"{0}\" не выбран")
    private void stepIsSelectedFalse(String label, String pagePath) {
        Assert.assertFalse("Элемент " + label + "не выбран!"
                , driverUtils.findElement(pagePath).isSelected());
    }

    @Step("Считывам текст из поля \"{0}\"")
    private String stepGetText(String label, String pagePath) {
        logger.info("Считывам текст из поля: " + label);
        return attachmentString(driverUtils.findElement(pagePath).getText());
    }

    @Step("Считывам значение атрибута \"value\" из поля \"{0}\"")
    private String stepGetValue(String label, String pagePath) {
        logger.info("Считывам значение атрибута \"value\" из поля " + label);
        String temp;
        return attachmentString((temp = driverUtils.findElement(pagePath).getAttribute("value")) == null ? "" : temp);
    }

    @Step("Считывам значение атрибута \"class\" из поля \"{0}\"")
    private String stepGetClass(String label, String pagePath) {
        logger.info("Считывам значение атрибута \"class\" из поля " + label);
        String temp;
        return attachmentString((temp = driverUtils.findElement(pagePath).getAttribute("class")) == null ? "" : temp);
    }

    @Step(value = "Текст считанного значения: \"{0}\"")
    public String attachmentString(String attachmentString) {
        logger.info("attachmentString " + attachmentString);
        return attachmentString;
    }
}