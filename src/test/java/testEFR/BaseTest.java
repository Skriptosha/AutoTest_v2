package testEFR;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pagesEFR.AuthorizationPage;
import pagesEFR.BaseClass;
import pagesEFR.ClientsPage;
import pagesEFR.MainPage;
import utils.*;
import utils.annotations.AfterHack;

import java.util.logging.Logger;

@Component("BaseTest")
@DisplayName("Набор базовых тестов для ЕФР")
public class BaseTest extends TestUtils {

    //@Rule
    public JUnitUtils jUnitUtils;

    private DriverUtils driverUtils;

    private DataProvider env;

    private Logger logger;

    private CommonUtils commonUtils;

    private BaseClass baseClass;

   // @Autowired
   // public void setJUnitUtils(JUnitUtils jUnitUtils) { this.jUnitUtils = jUnitUtils; }

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

    @Before
    public void beginTest() {
        driverUtils.getURL(env.getProperty("urlEFR"));
    }

    public void authorization(String login, String password){
        baseClass.choseSelect(AuthorizationPage.DOMEN, AuthorizationPage.GO.getPath());
        performClear(AuthorizationPage.LOGIN.getPath(),AuthorizationPage.LOGIN.getLabel());
        performSendKeys(AuthorizationPage.LOGIN.getPath(),AuthorizationPage.LOGIN.getLabel(), login);
        performSendKeys(AuthorizationPage.PASSWORD.getPath(), AuthorizationPage.PASSWORD.getLabel(), password);
        performClick(AuthorizationPage.ENTER.getPath(),AuthorizationPage.ENTER.getLabel());
    }

    public void findClient(String passSeries, String passNumber){
        performClick(MainPage.CLIENTS.getPath(), MainPage.CLIENTS.getLabel());
        performSendKeys(ClientsPage.PASSPORT_SERIES.getPath(), ClientsPage.PASSPORT_SERIES.getLabel(), passSeries);
        performSendKeys(ClientsPage.PASSPORT_NUMBER.getPath(), ClientsPage.PASSPORT_NUMBER.getLabel(), passNumber);
        performClick(ClientsPage.SEARCH.getPath(), ClientsPage.SEARCH.getLabel());
    }

    @AfterHack
    public void after(){
        performClick(MainPage.EXIT.getPath(), MainPage.EXIT.getLabel());
        logger.info("Call AfterHack");
    }

    @Test
    @Epic("Тестирование UI")
    @Feature("Базовые тесты")
    @Stories({@Story("Авторизация")})
    @DisplayName("Авторизация в системе ЕФР")
    @Description("Авторизация в системе ЕФР")
    public void authorizationTest(){
        authorization("loginEmp", "passwordEmp");
    }


    @Test
    @Epic("Тестирование UI")
    @Feature("Базовые тесты")
    @Stories({@Story("Авторизация"), @Story("Поиск клиента")})
    @DisplayName("Авторизация в системе ЕФР + Поиск клиента в ЕФР")
    @Description("Авторизация в системе ЕФР + Поиск клиента в ЕФР")
    public void findClientTest(){
        authorization("loginEmp", "passwordEmp");
        findClient("passSeriesBase", "passNumberBase");
        assertEquals("fioBase"
                , performGetText(AuthorizationPage.FIO_CLIENT.getPath(), AuthorizationPage.FIO_CLIENT.getLabel()));
    }
}
