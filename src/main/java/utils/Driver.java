package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Component
public class Driver {

    private static final String DRIVER_PATH = "./drivers/";
    private static int timeout;

    @Autowired
    private Environment environment;

    @Autowired
    private Logger logger;

    @Autowired
    private CommonUtils commonUtils;

    @Bean
    public WebDriver initializationWebDriver() {
        WebDriver webDriver = null;
        WebDrivers webDrivers = WebDrivers.valueOf(commonUtils.getParameter("browser").toLowerCase());
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
            case firefox:
                System.setProperty("webdriver.gecko.driver", DRIVER_PATH + environment.getProperty("firefox.driver"));
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setBinary(environment.getProperty("firefox.binary"));
                firefoxOptions.addPreference("browser.download.folderList", 2);
                firefoxOptions.addPreference("browser.helperApps.neverAsk.saveToDisk",
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, text/plain," +
                                " application/pdf, text/csv," +
                                " application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                firefoxOptions.addPreference("browser.download.manager.showWhenStarting", false);
                firefoxOptions.addPreference("browser.download.manager.showAlertOnComplete", false);
                firefoxOptions.addPreference("browser.download.manager.closeWhenDone", true);
                webDriver = new FirefoxDriver(firefoxOptions);
                break;
            case internetExplorer:
                System.setProperty("webdriver.ie.driver", DRIVER_PATH + environment.getProperty("internetExplorer.driver"));
                InternetExplorerOptions internetExplorerOptions = new InternetExplorerOptions();
                webDriver = new InternetExplorerDriver(internetExplorerOptions);
                break;
            case remote:
                DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
                try {
                    webDriver = new RemoteWebDriver(new URL(Objects.requireNonNull(environment.getProperty("remoteURL")))
                            , desiredCapabilities);
                } catch (MalformedURLException e) {
                    logger.severe(e.getMessage());
                }
            default:
                logger.warning("Такой драйвер не установлен!");
        }
        logger.info("Драйвер успешно создан");
        assert webDriver != null;
        timeout = Integer.parseInt(Objects.requireNonNull(commonUtils.getParameter("timeoutCurr")
                .equalsIgnoreCase("High") ? environment.getProperty("timeoutHigh")
                : environment.getProperty("timeoutLow")));
        webDriver.manage().window().maximize();
        webDriver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
        logger.info("Таймаут установлен на " + timeout);
        return webDriver;
    }

    @Bean
    public WebDriverWait getWebDriverWait(WebDriver webDriver) {
        return new WebDriverWait(webDriver, timeout);
    }
}