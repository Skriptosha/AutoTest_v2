package utils;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.Inet4Address;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;

@Component
public class CommonUtils {

    @Autowired
    Environment environment;

    @Autowired
    private Logger logger;

    public String getParameter(String name) {
        logger.info("Имя параметра: " + name);
        return System.getProperty(name) == null ? environment.getProperty(name)
                : System.getProperty(name);
    }

    public void setEnvironment(WebDriver webDriver) {
        logger.info("Записываем информацию Environment для отчета Allure 2");
        Capabilities capabilities = ((RemoteWebDriver) webDriver).getCapabilities();
        try {
            Properties properties = new Properties();
            properties.setProperty("Browser", capabilities.getBrowserName());
            properties.setProperty("Browser Version", capabilities.getVersion());
            properties.setProperty("IPv4", Inet4Address.getLocalHost().getHostAddress());
            properties.setProperty("PlatForm", capabilities.getPlatform().name());

            Path path = Paths.get(environment.getProperty("allure_result")
                    + environment.getProperty("property_file"));
            logger.info("path: " + path);

            Files.deleteIfExists(path);

            Files.createDirectories(Paths.get(Objects.requireNonNull(environment.getProperty("allure_result"))));
            int count = 0;
            while (count < 4) {
                try {
                    properties.store(Files.newBufferedWriter(path, StandardCharsets.UTF_8)
                            , "Environment для отчета Allure 2");
                } catch (IOException e) {
                    count++;
                }
                if (Files.exists(path)) {
                    logger.fine("Файл environment.properties успешно сохранен");
                    break;
                }
            }
        } catch (IOException e) {
            logger.severe("Ошибка при записи файла " + environment.getProperty("allure_result")
                    + environment.getProperty("property_file"));
            e.printStackTrace();
        }
    }

    public String getTextFromFile(String key) {
        try {
            return new String(Files.readAllBytes(Paths.get(Objects.requireNonNull(environment.getProperty(key))))
                    , StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.severe(e.getMessage());
            e.printStackTrace();
        }
        return "";
    }
}
