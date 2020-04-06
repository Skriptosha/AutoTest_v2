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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.logging.Logger;

@Component("testUtils")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConf.class})
public class TestUtils {

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
    public static void begin() {
    }

    @AfterClass
    public static void tearDown() {
        DriverUtils driverUtils = SpringConf.getBean(DriverUtils.class);
        driverUtils.quit(SpringConf.getBean(WebDriver.class));
        driverUtils.setEnvironment();
    }

    public String removeSymbols(@NonNull String exp) {
        return exp.replaceAll(Objects.requireNonNull(environment.getProperty("regExp")), "");
    }

    /**
     * Если необходимо сравнить исключительно буквы, без учета пробелов, тире и слешей.
     *
     * @param expectedKey ожидаемый Обьект
     * @param actual      Обьект, полученный на самом деле
     */
    public void assertEquals(Object expectedKey, Object actual) {
        if (expectedKey instanceof String && actual instanceof String) {
            logger.info("actualKey: " + actual);
            String expected = environment.getProperty(String.valueOf(expectedKey));
            if (expected == null) stepAssertEquals(expectedKey, actual);
            else {
                if (expected.equals(actual)) stepAssertEquals(expected, actual);
                else Assert.assertEquals(removeSymbols(expected), removeSymbols(String.valueOf(actual)));
            }
        } else {
            stepAssertEquals(expectedKey, actual);
        }
    }

    @Step("Сравниваем значения")
    private void stepAssertEquals(Object expected, Object actual){
        Assert.assertEquals(expected, actual);
    }

    private String path;

    public TestUtils performSendKeys(PageEnum page, String key) {
        path = page.getPath();
        stepSendKeys(page.getLabel(), environment.getProperty(key));
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
        ;
        return stepGetValue(page.getLabel());
    }

    public boolean performIsEnabled(PageEnum page) {
        this.path = page.getPath();
        ;
        return stepIsEnabled(page.getLabel());
    }

    @Step("Вводим в поле \"{0}\" значение \"{1}\"")
    private void stepSendKeys(String label, String value) {
        driverUtils.sendKeys(path, value);
    }

    @Step("Нажимаем на элемент \"{0}\"")
    private void stepClick(String label) {
        driverUtils.click(path);
    }

    @Step("Очищаем форму \"{0}\"")
    private void stepClear(String label) {
        driverUtils.clear(path);
    }

    @Step("Проверяем активен элемент \"{0}\" или не активен")
    private boolean stepIsEnabled(String label) {
        return driverUtils.findElement(path).isEnabled();
    }

    @Step("Считывам текст из поля \"{0}\"")
    private String stepGetText(String label) {
        logger.info("Считывам текст из поля: " + label);
        return attachmentString(driverUtils.findElement(path).getText());
    }

    @Step("Считывам значение атрибута \"value\" поля \"{0}\"")
    private String stepGetValue(String label) {
        logger.info("Считывам значение атрибута \"value\" поля: " + label);
        return attachmentString(driverUtils.findElement(path).getAttribute("value"));
    }

    @Attachment(value = "Текст поля:", type = "text/plain")
    public String attachmentString(String attachmentString) {
        return attachmentString;
    }

    public String getConsent(String key) {
        try {
            return new String(Files.readAllBytes(Paths.get(environment.getProperty(key))), StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.severe(e.getMessage());
            e.printStackTrace();
        }
        return "";
    }
}