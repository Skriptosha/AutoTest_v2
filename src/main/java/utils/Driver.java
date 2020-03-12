package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Component
public class Driver {

    private static final String DRIVER_PATH = "./drivers/";
    private static int timeout;

    @Value("${browser}")
    private WebDrivers webDrivers;

    @Autowired
    private Environment environment;

    @Autowired
    private Logger logger;

    @Bean
    public WebDriver initializationWebDriver() {
        WebDriver webDriver = null;
        logger.info("Драйвер выбран: " + webDrivers);
        switch (webDrivers) {
            case opera:
                OperaOptions operaOptions = new OperaOptions();
                operaOptions.setBinary(Objects.requireNonNull(environment.getProperty("opera.binary")));
                System.setProperty("webdriver.opera.driver", DRIVER_PATH + environment.getProperty("opera.driver"));
                webDriver = new OperaDriver(operaOptions);
                break;
            case chrome:
                System.setProperty("webdriver.chrome.driver", DRIVER_PATH + environment.getProperty("chrome.driver"));
                webDriver = new ChromeDriver();
                break;
            default:
                logger.warning("Такой драйвер не установлен!");
        }
        logger.info("Драйвер успешно создан");
        timeout = Integer.parseInt(Objects.requireNonNull(environment.getProperty("timeout")));
        webDriver.manage().window().maximize();
        webDriver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
        return webDriver;
    }

    @Bean
    public WebDriverWait getWebDriverWait(WebDriver webDriver) {
        return new WebDriverWait(webDriver, timeout);
    }
}