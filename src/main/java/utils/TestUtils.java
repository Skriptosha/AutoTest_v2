package utils;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import lombok.NonNull;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pages.PageEnum;

import java.util.Objects;
import java.util.logging.Logger;

@Component("testUtils")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConf.class})
public class TestUtils {

    public static final boolean TRUE = true;
    public static final boolean FALSE = false;

    @Autowired
    private WebDriver webDriver;

    @Autowired
    private Logger logger;

    @Autowired
    private Environment environment;

    @Autowired
    private DriverUtils driverUtils;

    /**
     * Сделать скриншот
     *
     * @return скриншот в byte[]
     */
    @Attachment(value = "Скриншот страницы", type = "image/png")
    public byte[] takeScreenshot() {
        logger.info("Делаем скриншот");
        if (webDriver == null) {
            logger.severe("При попытке сделать скриншот webDriver == null");
            return null;
        } else return ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES);
    }

    @Rule
    public TestRule screenshotRule = new TestWatcher() {

        @Override
        protected void failed(Throwable e, Description description) {
            takeScreenshot();
        }

        @Override
        protected void starting(Description description) {
        }

        @Override
        protected void finished(Description description) {
            logger.info("Junit 4 finished");
        }
    };

    @BeforeClass
    public static void setUp() {
    }

    @AfterClass
    public static void tearDown() {
        WebDriver webDriver = SpringConf.getBean(WebDriver.class);
        SpringConf.getBean(DriverUtils.class).quit(webDriver);
        SpringConf.getBean(CommonUtils.class).setEnvironment(webDriver);
    }

    public String removeSymbols(@NonNull String exp) {
        return exp.replaceAll(Objects.requireNonNull(environment.getProperty("regExp")), "");
    }

    /**
     * Проверка Assert.assertEquals. параметр expectedKey пытается найти в проперт файле,
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
            String expected = environment.getProperty(String.valueOf(expectedKey));
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

    @Step("Сравниваем значения")
    private void stepAssertEquals(Object expected, Object actual) {
        Assert.assertEquals(expected, actual);
    }

    private String path;

    public TestUtils performSendKeys(PageEnum page, CharSequence... key) {
        path = page.getPath();
        logger.info("Размер массива key: " + key.length + "\n"
                + "Значение key[0]: " + key[0].toString());
        String value;
        if (key.length == 1) {
            value = environment.getProperty(key[0].toString());
            stepSendKeys(page.getLabel(), value == null ? key[0] : value);
        } else {
            stepSendKeys(page.getLabel(), key);
        }
        return this;
    }

    public TestUtils performClick(PageEnum page) {
        path = page.getPath();
        stepClick(page.getLabel());
        return this;
    }

    public TestUtils performClear(PageEnum page) {
        path = page.getPath();
        stepClear(page.getLabel());
        return this;
    }

    public String performGetText(PageEnum page) {
        path = page.getPath();
        return stepGetText(page.getLabel());
    }

    public String performGetValue(PageEnum page) {
        this.path = page.getPath();
        return stepGetValue(page.getLabel());
    }

    public void performAndAssertIsEnabled(PageEnum page, boolean expected) {
        this.path = page.getPath();
        if (expected) {
            stepIsEnabledTrue(page.getLabel());
        } else {
            stepIsEnabledFalse(page.getLabel());
        }
    }

    public void performAndAssertIsSelected(PageEnum page, boolean expected) {
        this.path = page.getPath();
        if (expected) {
            stepIsSelectedTrue(page.getLabel());
        } else {
            stepIsSelectedFalse(page.getLabel());
        }
    }

    @Step("Вводим в поле \"{0}\" значение \"{1}\"")
    private void stepSendKeys(String label, CharSequence... value) { driverUtils.sendKeys(path, value); }

    @Step("Нажимаем на элемент \"{0}\"")
    private void stepClick(String label) {
        driverUtils.click(path);
    }

    @Step("Очищаем форму \"{0}\"")
    private void stepClear(String label) {
        driverUtils.clear(path);
    }

    @Step("Проверяем что элемент \"{0}\" активен")
    private void stepIsEnabledTrue(String label) {
        Assert.assertTrue("Элемент " + label + "не активен!"
                , driverUtils.findElement(path).isEnabled());
    }

    @Step("Проверяем что элемент \"{0}\" не активен")
    private void stepIsEnabledFalse(String label) {
        Assert.assertFalse("Элемент " + label + "активен!"
                , driverUtils.findElement(path).isEnabled());
    }

    @Step("Проверяем что checkbox \"{0}\" выбран")
    private void stepIsSelectedTrue(String label) {
        Assert.assertTrue("Элемент " + label + "не выбран!"
                , driverUtils.findElement(path).isSelected());
    }

    @Step("Проверяем что checkbox \"{0}\" не выбран")
    private void stepIsSelectedFalse(String label) {
        Assert.assertFalse("Элемент " + label + "не выбран!"
                , driverUtils.findElement(path).isSelected());
    }

    @Step("Считывам текст из поля \"{0}\"")
    private String stepGetText(String label) {
        logger.info("Считывам текст из поля: " + label);
        return attachmentString(driverUtils.findElement(path).getText());
    }

    @Step("Считывам значение атрибута \"value\" из поля \"{0}\"")
    private String stepGetValue(String label) {
        logger.info("Считывам значение атрибута \"value\" из поля " + label);
        return attachmentString(driverUtils.findElement(path).getAttribute("value"));
    }

    @Attachment(value = "Текст считанного значения: \"{0}\"", type = "text/plain")
    public String attachmentString(String attachmentString) {
        return attachmentString;
    }
}