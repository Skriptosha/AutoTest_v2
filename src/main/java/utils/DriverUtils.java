package utils;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.Inet4Address;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
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

    @Step("Закрываем вебдрайвер")
    void quit(WebDriver webDriver){
        if (webDriver != null) {
            webDriver.quit();
            logger.info("ВебДрайвер успешно уничтожен");
        } else
        {
            logger.info("webDriver == null");
        }
    }

    public void setEnvironment() {
        logger.info("Записываем информацию Environment для отчета Allure 2");
        Capabilities capabilities = ((RemoteWebDriver) webDriver).getCapabilities();
        try {
            Properties properties = new Properties();
            properties.setProperty("Браузер", capabilities.getBrowserName());
            properties.setProperty("Версия Браузера", capabilities.getVersion());
            properties.setProperty("IPv4", Inet4Address.getLocalHost().getHostAddress());
            properties.setProperty("Операционная система", capabilities.getPlatform().name() + " "
                    + capabilities.getPlatform().getMajorVersion());

            Path path = Paths.get(environment.getProperty("allure_result")
                    + environment.getProperty("property_file"));
            logger.info("path: " + path);

            Files.deleteIfExists(path);

            Files.createDirectories(Paths.get(Objects.requireNonNull(environment.getProperty("allure_result"))));

            properties.store(Files.newBufferedWriter(Paths.get(environment.getProperty("allure_result")
                            + environment.getProperty("property_file")), StandardCharsets.US_ASCII)
                    , "Environment для отчета Allure 2");

            logger.fine("Файл environment.properties успешно сохранен");
        } catch (IOException e) {
            logger.severe("Ошибка при записи файла " + environment.getProperty("allure_result")
                    + environment.getProperty("property_file"));
            e.printStackTrace();
        }
    }
}