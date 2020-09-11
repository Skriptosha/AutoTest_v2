package pagesOnlineApp;

import lombok.Getter;

public enum FirstPageOnlineApp implements PageEnum {

    FIO("//input[@name='fio']", "Фамилия Имя Отчество"),
    BIRTH_DATE("//input[@name='birthdate']", "Дата рождения"),
    PASSPORT("//input[@name='pd']", "Серия и номер паспорта"),
    PHONE( "//input[@name='phone']", "Телефон"),
    MALE("//div[@value='male']", "Мужской"),
    FEMALE("//div[@value='female']", "Женский"),
    EMAIL("//input[@name='email']", "Электронная почта"),
    AGREE_TERMS_ACCEPT("//span[contains(text(), 'Я согласен с')]/..//input"
            , "Я согласен с условиями"),
    AGREE_TERMS_CONSENT("//a[text()='условиями']", "Условия"),
    AGREE_TERMS_CONSENT_TEXT("//a[text()='условиями']/../..//div[7]", "Текст согласия"),
    JUST_CALL("//span[contains(text(), 'перезвоните мне')]/..//input"
            , "Я ничего не хочу заполнять, просто перезвоните мне"),
    CONFIRM_DATA("//button[text()='Подтвердить данные']", "Подтвердить данные"),
    SMS_CODE("//input[@name='sms_code']", "Код из СМС"),
    SEND("//button[text()='Отправить']", "Отправить");

    @Getter
    private final String path;

    @Getter
    private final String label;

    FirstPageOnlineApp(String path, String label) {
        this.path = path;
        this.label = label;
    }
}
