package pages;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import lombok.Getter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import utils.DriverUtils;
import utils.TestUtils;

@Component("onlineAppSecondPageTest")
@DisplayName("Вторая страница")
public class OnlineAppSecondPageTest extends TestUtils {

    @Getter
    @Value("${urlIApp}")
    private String url;

    @Autowired
    private DriverUtils driverUtils;

    @Autowired
    private TestUtils testUtils;

    @Autowired
    private Environment environment;

    @Autowired
    @Qualifier("onlineAppFirstPageTest")
    private OnlineAppFirstPageTest onlineAppFirstPageTest;

    @Before
    public void beginTest() {
        driverUtils.getURL(url);
    }

    @Test
    @Epic("Тестирование UI")
    @Feature("Вторая страница")
    @Stories({@Story("Валидаторы")})
    @DisplayName("Поле \"Серия и номер паспорта\"")
    @Description("Валидаторы для поля \"Серия и номер паспорта\" :\n" +
            "- Проверка значения с предыдущей страницы \n" +
           "- проверка что поле не доступно для редактирования")
    public void testPassport(){
        onlineAppFirstPageTest.testPageFull();
        assertEquals("passport", performGetValue(SecondPageOnlineApp.PASSPORT));
        Assert.assertTrue(environment.getProperty("errorMessage")
                , performIsEnabled(SecondPageOnlineApp.PASSPORT));
    }

    @Test
    @Epic("Тестирование UI")
    @Feature("Вторая страница")
    @Stories({@Story("Валидаторы")})
    @DisplayName("Поле \"Дата выдачи паспорта\"")
    public void testGivingDate() {
        performClick(SecondPageOnlineApp.SECOND_PAGE_TAB);
        performSendKeys(SecondPageOnlineApp.GIVING_DATE, "givingDate_wrong");
        performSendKeys(SecondPageOnlineApp.GIVING_DATE, "givingDate");
        assertEquals("givingDate", performGetValue(SecondPageOnlineApp.GIVING_DATE));
        performClear(SecondPageOnlineApp.GIVING_DATE);
    }

    @Test
    @Epic("Тестирование UI")
    @Feature("Вторая страница")
    @Stories({@Story("Валидаторы")})
    @DisplayName("Поле \"Код органа\"")
    public void testDepartmentCode() {
        performClick(SecondPageOnlineApp.SECOND_PAGE_TAB);
        performSendKeys(SecondPageOnlineApp.DEPARTMENT_CODE, "departmentCode_wrong");
        performSendKeys(SecondPageOnlineApp.DEPARTMENT_CODE, "departmentCode");
        assertEquals("departmentCode", performGetValue(SecondPageOnlineApp.DEPARTMENT_CODE));
        performClear(SecondPageOnlineApp.DEPARTMENT_CODE);
    }

    @Test
    @Epic("Тестирование UI")
    @Feature("Вторая страница")
    @Stories({@Story("Валидаторы")})
    @DisplayName("Поле \"Кем выдан паспорт\"")
    public void testDepartmentInfo() {
        performClick(SecondPageOnlineApp.SECOND_PAGE_TAB);
        performSendKeys(SecondPageOnlineApp.DEPARTMENT_INFO, "departmentInfo_wrong");
        performSendKeys(SecondPageOnlineApp.DEPARTMENT_INFO, "departmentInfo");
        assertEquals("departmentInfo", performGetValue(SecondPageOnlineApp.DEPARTMENT_INFO));
        performClear(SecondPageOnlineApp.DEPARTMENT_INFO);
    }

    @Test
    @Epic("Тестирование UI")
    @Feature("Вторая страница")
    @Stories({@Story("Валидаторы")})
    @DisplayName("Поле \"Место рождения\"")
    public void testBirthDate() {
        performClick(SecondPageOnlineApp.SECOND_PAGE_TAB);
        performSendKeys(SecondPageOnlineApp.BIRTH_PLACE, "birthDate_wrong");
        performSendKeys(SecondPageOnlineApp.BIRTH_PLACE, "birthDate");
        assertEquals("birthDate", performGetValue(SecondPageOnlineApp.BIRTH_PLACE));
        performClear(SecondPageOnlineApp.BIRTH_PLACE);
    }

    @Test
    @Epic("Тестирование UI")
    @Feature("Вторая страница")
    @Stories({@Story("Проход второй страницы")})
    @DisplayName("Корректное заполнение второй страницы - 1")
    public void testPageFull() {
        onlineAppFirstPageTest.testPageFull();
        performSendKeys(SecondPageOnlineApp.GIVING_DATE, "givingDate");
        performSendKeys(SecondPageOnlineApp.DEPARTMENT_CODE, "departmentCode");
        performSendKeys(SecondPageOnlineApp.DEPARTMENT_INFO, "departmentInfo");
        performSendKeys(SecondPageOnlineApp.BIRTH_PLACE, "birthDate");
    }
}
