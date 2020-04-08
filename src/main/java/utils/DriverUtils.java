package utils;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@Component
public class DriverUtils implements DisposableBean {

    @Autowired
    private WebDriver webDriver;

    @Autowired
    private Logger logger;

    @Autowired
    private Environment environment;

    @Autowired
    private WebDriverWait webDriverWait;

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

    public WebElement findElement(String path) {
        logger.info("Ищем элемент: " + path);
        return webDriverWait.until(webDriver1 -> webDriver1.findElement(By.xpath(path)));
    }

    public List<WebElement> findElements(String path) {
        logger.info("Ищем элементы: " + path);
        return webDriverWait.until(webDriver1 -> webDriver1.findElements(By.xpath(path)));
    }

    @Step("Переходим на сайт \"{0}\"")
    public DriverUtils getURL(String urlKey) {
        logger.info("Переход на сайт: " + urlKey);
        webDriver.navigate().to(urlKey);
        if (webDriver.getTitle().contains(Objects.requireNonNull(environment.getProperty("ieTittle")))) {
            logger.info("Сайт " + urlKey + " содержит ошибку сертификата");
            webDriver.navigate().to("javascript:document.getElementById('overridelink').click()");
        }
        return this;
    }

    @Step("Ждем исчезновения элемента {0}")
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
    }


    void quit(WebDriver webDriver){
        if (webDriver != null) {
            webDriver.quit();
            logger.info("ВебДрайвер успешно уничтожен");
        } else
        {
            logger.info("webDriver == null");
        }
    }


}