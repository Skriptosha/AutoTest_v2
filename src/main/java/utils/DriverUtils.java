package utils;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
public class DriverUtils implements DisposableBean {

    @Autowired
    private WebDriver webDriver;

    @Autowired
    private Logger logger;

    @Autowired
    private Environment environment;

    @Autowired
    private WebDriverWait webDriverWait;

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
            quit(webDriver);
        }
    };

    @Step("Нажимаем на элемент {path}")
    public DriverUtils click(String path) {
        logger.info("Нажимаем на элемент " + path);
        webDriverWait.until(webDriver1 -> {
            try {
                webDriver1.findElement(By.xpath(path)).click();
            } catch (Exception e) {
                return false;
            }
            return true;
        });
        return this;
    }

    @Step("Нажимаем на элемент {path}")
    public DriverUtils clear(String path) {
        logger.info("очищаем форму: " + path);
        webDriverWait.until(webDriver1 -> {
            try {
                webDriver1.findElement(By.xpath(path)).clear();
            } catch (Exception e) {
                return false;
            }
            return true;
        });
        return this;
    }

    @Step("Печатаем текст... {charSequences}")
    public DriverUtils sendKeys(String path, CharSequence... charSequences) {
        logger.info("Печатаем текст " + Arrays.toString(charSequences));
        webDriverWait.until(webDriver1 -> {
            try {
                webDriver1.findElement(By.xpath(path)).sendKeys(charSequences);
            } catch (Exception e) {
                return false;
            }
            return true;
        });
        return this;
    }

    @Step("Ищем элемент: {by}")
    public WebElement findElement(String path) {
        logger.info("Ищем элемент: " + path);
        return webDriverWait.until(webDriver1 -> webDriver1.findElement(By.xpath(path)));
    }

    @Step("Ищем элементы: {by}")
    public List<WebElement> findElements(String path) {
        logger.info("Ищем элементы: " + path);
        return webDriverWait.until(webDriver1 -> webDriver1.findElements(By.xpath(path)));
    }

    @Step("Переходим на сайт")
    public DriverUtils getURL(String urlKey) {
        logger.info("Переход на сайт: " + urlKey);
        webDriver.navigate().to(urlKey);
        if (webDriver.getTitle().contains(Objects.requireNonNull(environment.getProperty("ieTittle")))) {
            logger.info("Сайт " + urlKey + " содержит ошибку сертификата");
            webDriver.navigate().to("javascript:document.getElementById('overridelink').click()");
        }
        return this;
    }

    public DriverUtils staleness(WebElement webElement) {
        try {
            webDriverWait.until(ExpectedConditions.stalenessOf(webElement));
            logger.info("Ждем изчезновения элемента: " + webElement.getText());
        } catch (StaleElementReferenceException s) {
            //
        }
        return this;
    }


    @Override
    public void destroy() {
        logger.info("Destroy() вызван");
//        quit(webDriver);
    }

    private void quit(WebDriver webDriver){
        if (webDriver != null) {
            webDriver.quit();
            logger.info("ВебДрайвер успешно уничтожен");
        } else
        {
            logger.info("webDriver == null");
        }
    }
}