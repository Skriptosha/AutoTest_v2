import io.qameta.allure.junit4.DisplayName;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import pages.FirstPageOnlineApplication;
import pages.SecondPageOnlineApplication;
import utils.DriverUtils;

@Component
public class OnlineApplicationTest extends DriverUtils {

    private static String url = "http://10.7.118.5:7015/efr/iclaim/";

    @Autowired
    private DriverUtils driverUtils;

    @Autowired
    WebDriver webDriver;

    @Autowired
    private Environment environment;

    @Autowired
    private FirstPageOnlineApplication firstPageOnlineApplication;

    @Autowired
    private SecondPageOnlineApplication secondPageOnlineApplication;

    @Before
    public void beginTest() {
        driverUtils.getURL(url);
    }

    @Test
    @DisplayName("Прохождение первой страницы")
    public void firstPage() {
        driverUtils
                .sendKeys(firstPageOnlineApplication.getFio(), "Иванов Иван Иванович")
                .sendKeys(firstPageOnlineApplication.getBirthDate(), "01011990")
                .sendKeys(firstPageOnlineApplication.getPassport(), "1234567890")
                .click(firstPageOnlineApplication.getFemale())
                .sendKeys(firstPageOnlineApplication.getPhone(), "9161000000")
                .sendKeys(firstPageOnlineApplication.getEmail(), "qwerty@mail.ru")
                .click(firstPageOnlineApplication.getConditionsAccept())
                .click(firstPageOnlineApplication.getSmsInput())
                .sendKeys(firstPageOnlineApplication.getAcceptData(), "12345")
                .click(firstPageOnlineApplication.getSend());
    }

    @Test
    @DisplayName("Тестирование второй страницы")
    public void SecondPage() {
        firstPage();
        driverUtils
                .sendKeys(secondPageOnlineApplication.getGivingDate(), "12222001")
                .sendKeys(secondPageOnlineApplication.getDepartmentCode(), "020005")
                .sendKeys(secondPageOnlineApplication.getDepartmentInfo(), "Тест")
                .sendKeys(secondPageOnlineApplication.getBirthPlace(), "Ufa");
        Assert.assertEquals(driverUtils.findElement(secondPageOnlineApplication.getDepartmentCode()).getAttribute("value")
                , "020-005");
        Assert.assertEquals(driverUtils.findElement(secondPageOnlineApplication.getDepartmentInfo()).getAttribute("value")
                , "Тест");
    }
}
