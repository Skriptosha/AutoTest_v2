package pagesEFR;

import lombok.Getter;
import pagesOnlineApp.PageEnum;

public enum AuthorizationPage implements PageEnum {

    DOMEN("//div[@class='Select-control']", "Домен"),
    GO("GO", "GO"),
    LOGIN("//input[@data-id='field-login']", "Имя пользователя"),
    PASSWORD( "//input[@data-id='field-password']", "Пароль"),
    ENTER("//button[@data-id='button-save']", "Войти"),
    FIO_CLIENT("//div[@title='ФИО']", "ФИО")

    ;

    @Getter
    private final String path;

    @Getter
    private final String label;

    AuthorizationPage(String path, String label) {
        this.path = path;
        this.label = label;
    }
}
