package utils;

import io.qameta.allure.Attachment;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utils.annotations.AfterHack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

@Component("JUnitUtils")
public class JUnitUtils extends TestWatcher {

    private WebDriver webDriver;

    private Logger logger;

    @Autowired
    public JUnitUtils(WebDriver webDriver, Logger logger) {
        this.webDriver = webDriver;
        this.logger = logger;
    }

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

    @Override
    protected void failed(Throwable e, Description description) {
        takeScreenshot();
    }

    @Override
    protected void starting(Description description) { }

    @Override
    protected void finished(Description description) {
        invokeAfterHackMethods(description.getClassName());
        //testUtils.performClick(MainPage.EXIT.getPath(), MainPage.EXIT.getLabel());
    }

    public void invokeAfterHackMethods(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            for (Method declaredMethod : clazz.getDeclaredMethods()) {
                if (declaredMethod.isAnnotationPresent(AfterHack.class)) declaredMethod.invoke(SpringConf.getBean(clazz));
            }
        } catch (IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
