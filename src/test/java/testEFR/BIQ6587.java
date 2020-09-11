package testEFR;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pagesEFR.BaseClass;
import pagesEFR.MainPage;
import pagesEFR.PopulationCitiesPage;
import utils.*;
import utils.annotations.AfterHack;

import java.util.logging.Logger;

@Component("BIQ6587")
@DisplayName("BIQ 6587 (5566)")
public class BIQ6587 extends TestUtils {

    @Rule
    public JUnitUtils jUnitUtils;

    private DriverUtils driverUtils;

    private DataProvider env;

    private Logger logger;

    private CommonUtils commonUtils;

    private BaseClass baseClass;

    private BaseTest baseTest;

    private String[] names;

    @Autowired
    public void setJUnitUtils(JUnitUtils jUnitUtils) { this.jUnitUtils = jUnitUtils; }

    @Autowired
    public void setDriverUtils(DriverUtils driverUtils) {
        this.driverUtils = driverUtils;
    }

    @Autowired
    public void setEnv(DataProvider env) {
        this.env = env;
    }

    @Autowired
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    @Autowired
    public void setCommonUtils(CommonUtils commonUtils) {
        this.commonUtils = commonUtils;
    }

    @Autowired
    public void setBaseClass(BaseClass baseClass) {
        this.baseClass = baseClass;
    }

    @Autowired
    public void setBaseTest(BaseTest baseTest) {
        this.baseTest = baseTest;
    }

    @Before
    public void beginTest() {
        driverUtils.getURL(env.getProperty("urlEFR"));
    }

    @Test
    @Epic("Тестирование UI")
    @Feature("Справочник городов")
    @Stories({@Story("Справочник городов")})
    @DisplayName("Добавление новой записи в справочник городов")
    @Description("Добавление нового города в справочник + " + "Проверка на незаполненность полей + " +
            "проверка длины полей + " + "Проверка уникальности поля \"Код ФИАС\"")
    public void testPopulationCitiesCreate() {
        String temp;
        baseTest.authorization("loginEmpAdmin", "passwordEmpAdmin");
        performClick(MainPage.DICTIONARIES.getPath(), MainPage.DICTIONARIES.getLabel());
        performClick(MainPage.POPULATION_CITIES.getPath(), MainPage.POPULATION_CITIES.getLabel());
        performClick(PopulationCitiesPage.ADD.getPath(), PopulationCitiesPage.ADD.getLabel());

        performClick(PopulationCitiesPage.SAVE_WHEN_NEW.getPath(), PopulationCitiesPage.SAVE_WHEN_NEW.getLabel());
        String text = performGetText(PopulationCitiesPage.TOWNINFO.getPath(), PopulationCitiesPage.TOWNINFO.getLabel());
        logger.info(text);
        temp = "Не появился валидатор - ";
        assertTrueContains(temp, text, env.getProperty("fiasValidator"));
        assertTrueContains(temp, text, env.getProperty("townValidator"));
        assertTrueContains(temp, text, env.getProperty("regionValidator"));
        assertTrueContains(temp, text, env.getProperty("signNBSMValidator"));

        baseClass.inputLengthValidator(PopulationCitiesPage.FIAS, 40);
        performSendKeys(PopulationCitiesPage.FIAS.getPath(), PopulationCitiesPage.FIAS.getLabel(), "fias2exists");

        baseClass.inputLengthValidator(PopulationCitiesPage.TOWN, 50);
        performSendKeys(PopulationCitiesPage.TOWN.getPath(), PopulationCitiesPage.TOWN.getLabel(), "town1");

        performSendKeys(PopulationCitiesPage.REGION.getPath(), PopulationCitiesPage.REGION.getLabel(), "regionGNIWrong1");
        assertEquals(""
                , performGetValue(PopulationCitiesPage.REGION.getPath(), PopulationCitiesPage.REGION.getLabel()));
        baseClass.inputLengthValidator(PopulationCitiesPage.REGION, 2);
        performSendKeys(PopulationCitiesPage.REGION.getPath(), PopulationCitiesPage.REGION.getLabel(), "regionGNI1");

        performSendKeys(PopulationCitiesPage.SIGN_NBSM.getPath()
                , PopulationCitiesPage.SIGN_NBSM.getLabel(), "signNBSMWrong1");
        assertEquals("", performGetValue(PopulationCitiesPage.SIGN_NBSM.getPath()
                , PopulationCitiesPage.SIGN_NBSM.getLabel()));
        baseClass.inputLengthValidator(PopulationCitiesPage.SIGN_NBSM, 2);
        performSendKeys(PopulationCitiesPage.SIGN_NBSM.getPath()
                , PopulationCitiesPage.SIGN_NBSM.getLabel(), "signNBSM1");

        performClick(PopulationCitiesPage.SAVE_WHEN_NEW.getPath(), PopulationCitiesPage.SAVE_WHEN_NEW.getLabel());
        assertEquals("fiasAlredyExistsMessage"
                , performGetText(PopulationCitiesPage.POPUP.getPath(), PopulationCitiesPage.POPUP.getLabel()));
        performClick(PopulationCitiesPage.CLOSE_POPUP.getPath(), PopulationCitiesPage.CLOSE_POPUP.getLabel());

        performSendKeys(PopulationCitiesPage.FIAS.getPath(), PopulationCitiesPage.FIAS.getLabel(), "fias1");
        performClick(PopulationCitiesPage.SAVE_WHEN_NEW.getPath(), PopulationCitiesPage.SAVE_WHEN_NEW.getLabel());


        performSendKeys(PopulationCitiesPage.REGION_SEARCH.getPath()
                , PopulationCitiesPage.REGION_SEARCH.getLabel(), "regionGNI1");
        performClick(PopulationCitiesPage.SHOW.getPath(), PopulationCitiesPage.SHOW.getLabel());

        text = performFind(replacement(PopulationCitiesPage.ROW.getPath(), "town1")
                , PopulationCitiesPage.ROW.getLabel()).getText();
        //mainPage.getRow(PopulationCitiesPage.ROW, "town1").getText();

        temp = "Строка не содержит нужного текста - ";
        assertTrueContains(temp, text, env.getProperty("fias1"));
        assertTrueContains(temp, text, env.getProperty("signNBSM1"));

        // удаляем запись
        performClick(PopulationCitiesPage.DELETE.getPath(), PopulationCitiesPage.DELETE.getLabel()
                , performFind(replacement(PopulationCitiesPage.ROW.getPath(), "town1")
                , PopulationCitiesPage.ROW.getLabel()));
        performClick(PopulationCitiesPage.DELETE_YES.getPath(), PopulationCitiesPage.DELETE_YES.getLabel());

        // проверяем что удалилась
        performSendKeys(PopulationCitiesPage.REGION_SEARCH.getPath()
                , PopulationCitiesPage.REGION_SEARCH.getLabel(), "regionGNI1");
        performClick(PopulationCitiesPage.SHOW.getPath(), PopulationCitiesPage.SHOW.getLabel());
        Assert.assertFalse("Строка не была удалена!"
                , performGetText(PopulationCitiesPage.TABLE.getPath(), PopulationCitiesPage.TABLE.getLabel())
                        .contains(env.getProperty("fias1")));
    }

    @Test
    @Epic("Тестирование UI")
    @Feature("Справочник городов")
    @Stories({@Story("Справочник городов")})
    @DisplayName("Редактирование текущей записи в справочнике городов")
    @Description("Редактирование текущего города в справочнике + " + "Проверка уникальности поля \"Код ФИАС\"")
    public void testPopulationCitiesEdit() {
        String temp;
        baseTest.authorization("loginEmpAdmin", "passwordEmpAdmin");
        performClick(MainPage.DICTIONARIES.getPath(), MainPage.DICTIONARIES.getLabel());
        performClick(MainPage.POPULATION_CITIES.getPath(), MainPage.POPULATION_CITIES.getLabel());
        performSendKeys(PopulationCitiesPage.REGION_SEARCH.getPath(), PopulationCitiesPage.REGION_SEARCH.getLabel(), "regionGNI3");
        performClick(PopulationCitiesPage.SHOW.getPath(), PopulationCitiesPage.SHOW.getLabel());

        //driverUtils.findElements(PopulationCitiesPage.TABLE.getPath()).get(0).click();

        performClick(PopulationCitiesPage.FIRST_ROW.getPath(), PopulationCitiesPage.FIRST_ROW.getLabel());

        names = new String[4];
        names[0] = performGetValue(PopulationCitiesPage.FIAS.getPath(), PopulationCitiesPage.FIAS.getLabel());
        names[1] = performGetValue(PopulationCitiesPage.TOWN.getPath(), PopulationCitiesPage.TOWN.getLabel());
        names[2] = performGetValue(PopulationCitiesPage.SIGN_NBSM.getPath(), PopulationCitiesPage.SIGN_NBSM.getLabel());
        names[3] = performGetValue(PopulationCitiesPage.REGION.getPath(), PopulationCitiesPage.REGION.getLabel());
        logger.info(names[0] + ";" + names[1] + ";" + names[2] + ";" + names[3]);

        performClear(PopulationCitiesPage.FIAS.getPath(), PopulationCitiesPage.FIAS.getLabel());
        performSendKeys(PopulationCitiesPage.FIAS.getPath()
                , PopulationCitiesPage.FIAS.getLabel(), "fias2exists");

        performClick(PopulationCitiesPage.SAVE_WHEN_EDIT.getPath(), PopulationCitiesPage.SAVE_WHEN_EDIT.getLabel());
        assertEquals("fiasAlredyExistsMessage"
                , performGetText(PopulationCitiesPage.POPUP.getPath(), PopulationCitiesPage.POPUP.getLabel()));
        performClick(PopulationCitiesPage.CLOSE_POPUP.getPath(), PopulationCitiesPage.CLOSE_POPUP.getLabel());

        performSendKeys(PopulationCitiesPage.FIAS.getPath(), PopulationCitiesPage.FIAS.getLabel(), "fias2");

        performSendKeys(PopulationCitiesPage.SIGN_NBSM.getPath()
                , PopulationCitiesPage.SIGN_NBSM.getLabel(), "signNBSM2");

        performSendKeys(PopulationCitiesPage.REGION.getPath(), PopulationCitiesPage.REGION.getLabel(), "regionGNI2");

        performClick(PopulationCitiesPage.SAVE_WHEN_EDIT.getPath(), PopulationCitiesPage.SAVE_WHEN_EDIT.getLabel());

        performSendKeys(PopulationCitiesPage.REGION_SEARCH.getPath()
                , PopulationCitiesPage.REGION_SEARCH.getLabel(), "regionGNI2");
        performClick(PopulationCitiesPage.SHOW.getPath(), PopulationCitiesPage.SHOW.getLabel());

        // фильтруем по городу
        String text = performFind(replacement(PopulationCitiesPage.ROW.getPath(), names[1])
                , PopulationCitiesPage.ROW.getLabel()).getText();
        temp = "Строка не содержит нужного текста - ";
        assertTrueContains(temp, text, env.getProperty("fias2"));
        assertTrueContains(temp, text, env.getProperty("signNBSM2"));
        assertTrueContains(temp, text, env.getProperty("regionGNI2"));


    }

    /**
     * Восстанавливаем данные после теста testPopulationCitiesEdit, данные храняться в массиве names[]
     */
    @AfterHack
    public void forTestPopulationCitiesEdit() {
        performSendKeys(PopulationCitiesPage.REGION_SEARCH.getPath(), PopulationCitiesPage.REGION_SEARCH.getLabel(), "regionGNI2");
        performClick(PopulationCitiesPage.SHOW.getPath(), PopulationCitiesPage.SHOW.getLabel());
        performClick(PopulationCitiesPage.FIRST_ROW.getPath(), PopulationCitiesPage.FIRST_ROW.getLabel());

        performSendKeys(PopulationCitiesPage.FIAS.getPath(), PopulationCitiesPage.FIAS.getLabel(), names[0]);

        performSendKeys(PopulationCitiesPage.SIGN_NBSM.getPath(), PopulationCitiesPage.SIGN_NBSM.getLabel(), names[2]);

        performSendKeys(PopulationCitiesPage.REGION.getPath(), PopulationCitiesPage.REGION.getLabel(), names[3]);

        performClick(PopulationCitiesPage.SAVE_WHEN_EDIT.getPath(), PopulationCitiesPage.SAVE_WHEN_EDIT.getLabel());
    }
}