package pages;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import utils.DriverUtils;
import utils.TestUtils;

import java.util.logging.Logger;

@Component("onlineAppFirstPageTest")
@DisplayName("Первая страница")
public class OnlineAppFirstPageTest extends TestUtils {

    @Value("${urlIApp}")
    private String url;

    @Autowired
    private DriverUtils driverUtils;

    @Autowired
    private TestUtils testUtils;

    @Autowired
    private Environment environment;

    @Autowired
    private Logger logger;

    @Before
    public void beginTest() {
        driverUtils.getURL(url);
    }

    @Test
    @Epic("Тестирование UI")
    @Feature("Первая страница")
    @Stories({@Story("Валидаторы")})
    @DisplayName("Поле \"Фамилия Имя Отчество\"")
    @Description("Валидаторы для поля \"Фамилия Имя Отчество\" :\n" +
            "- Латиница \n" +
            "- Вводим корректное значение")
    public void testFio(){
        performSendKeys(FirstPageOnlineApp.FIO, "fio_wrong");
        performSendKeys(FirstPageOnlineApp.FIO, "fio");
        assertEquals("fio", performGetValue(FirstPageOnlineApp.FIO));
        performClear(FirstPageOnlineApp.FIO);
    }

    @Test
    @Epic("Тестирование UI")
    @Feature("Первая страница")
    @Stories({@Story("Валидаторы")})
    @DisplayName("Поле \"Дата рождения\"")
    public void testBirthDate() {
        performSendKeys(FirstPageOnlineApp.BIRTH_DATE, "birthDate_wrong");
        performSendKeys(FirstPageOnlineApp.BIRTH_DATE, "birthDate");
        assertEquals("birthDate",performGetValue(FirstPageOnlineApp.BIRTH_DATE));
        performClear(FirstPageOnlineApp.FIO);
    }

    @Test
    @Epic("Тестирование UI")
    @Feature("Первая страница")
    @Stories({@Story("Валидаторы")})
    @DisplayName("Поле \"Серия и номер паспорта\"")
    public void testPassport() {
        performSendKeys(FirstPageOnlineApp.PASSPORT, "passport_wrong");
        performSendKeys(FirstPageOnlineApp.PASSPORT, "passport");
        assertEquals("passport", performGetValue(FirstPageOnlineApp.PASSPORT));
        performClear(FirstPageOnlineApp.FIO);
    }

    @Test
    @Epic("Тестирование UI")
    @Feature("Первая страница")
    @Stories({@Story("Валидаторы")})
    @DisplayName("Поле \"Телефон\"")
    public void testPhone() {
        performSendKeys(FirstPageOnlineApp.PHONE, "phone_wrong");
        performSendKeys(FirstPageOnlineApp.PHONE, "phone");
        assertEquals("phone_check", performGetValue(FirstPageOnlineApp.PHONE));
        performClear(FirstPageOnlineApp.FIO);
    }

    @Test
    @Epic("Тестирование UI")
    @Feature("Первая страница")
    @Stories({@Story("Валидаторы")})
    @DisplayName("Поле \"Электронная почта\"")
    public void testEmail() {
        performSendKeys(FirstPageOnlineApp.EMAIL, "email_wrong");
        performSendKeys(FirstPageOnlineApp.EMAIL, "email");
        assertEquals("email", performGetValue(FirstPageOnlineApp.EMAIL));
        performClear(FirstPageOnlineApp.FIO);
    }

    @Test
    @Epic("Тестирование UI")
    @Feature("Первая страница")
    @Stories({@Story("Валидаторы")})
    @DisplayName("Поле \"Я согласен с условиями\"")
    public void testAgreeTerms() {
        performClick(FirstPageOnlineApp.AGREE_TERMS_ACCEPT);
        performClick(FirstPageOnlineApp.AGREE_TERMS_CONSENT);
        logger.info(getConsent("consent_personalData"));
        assertEquals(getConsent("consent_personalData"), performGetText(FirstPageOnlineApp.AGREE_TERMS_CONSENT_TEXT));
        performClick(FirstPageOnlineApp.AGREE_TERMS_ACCEPT);
        Assert.assertFalse(environment.getProperty("errorMessage")
                , performIsEnabled(FirstPageOnlineApp.CONFIRM_DATA));
    }

    @Test
    @Epic("Тестирование UI")
    @Feature("Первая страница")
    @Stories({@Story("Валидаторы")})
    @DisplayName("Поле \"Код из СМС\"")
    public void testSmsCode() {
        performClick(FirstPageOnlineApp.AGREE_TERMS_ACCEPT);
        performClick(FirstPageOnlineApp.CONFIRM_DATA);
        performSendKeys(FirstPageOnlineApp.SMS_CODE, "sms_wrong");
        performSendKeys(FirstPageOnlineApp.SMS_CODE, "sms");
        assertEquals("sms", performGetValue(FirstPageOnlineApp.SMS_CODE));
        performClear(FirstPageOnlineApp.SMS_CODE);
    }

    @Test
    @Epic("Тестирование UI")
    @Feature("Первая страница")
    @Stories({@Story("Валидаторы")})
    @DisplayName("Другие тесты")
    public void testOther() {

    }

    @Test
    @Epic("Тестирование UI")
    @Feature("Первая страница")
    @Stories({@Story("Проход первой страницы")})
    @DisplayName("Корректное заполнение первой страницы")
    public void testPageFull() {
        performSendKeys(FirstPageOnlineApp.FIO, "fio");
        performSendKeys(FirstPageOnlineApp.BIRTH_DATE, "birthDate");
        performSendKeys(FirstPageOnlineApp.PASSPORT, "passport");
        performClick(FirstPageOnlineApp.FEMALE);
        performSendKeys(FirstPageOnlineApp.PHONE, "phone");
        performSendKeys(FirstPageOnlineApp.EMAIL, "email");
        performClick(FirstPageOnlineApp.AGREE_TERMS_ACCEPT);
        performClick(FirstPageOnlineApp.CONFIRM_DATA);
        performSendKeys(FirstPageOnlineApp.SMS_CODE, "sms");
        performClick(FirstPageOnlineApp.SEND);
    }
}