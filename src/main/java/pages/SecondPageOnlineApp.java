package pages;

import lombok.Getter;

public enum SecondPageOnlineApp implements PageEnum {

    SECOND_PAGE_TAB("//button[text()='Вторая страница']", "Вторая страница"),
    PASSPORT("//input[@name='pd']", "Серия и номер паспорта"),
    GIVING_DATE("//input[@name='givingDate']", "Дата выдачи паспорта"),
    DEPARTMENT_CODE("//input[@name='departmentCode']", "Код органа"),
    DEPARTMENT_INFO("//input[@name='departmentInfo']", "Кем выдан паспорт"),
    BIRTH_PLACE("//input[@name='birthPlace']", "Место рождения"),
    ;

    @Getter
    private final String path;

    @Getter
    private final String label;

    SecondPageOnlineApp(String path, String label) {
        this.path = path;
        this.label = label;
    }
}
