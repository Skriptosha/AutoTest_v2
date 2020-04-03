package pages;

import org.springframework.stereotype.Component;

@Component
public class FirstPageOnlineApplication {

    private String fio = "//input[@name='fio']";

    private String birthDate = "//input[@name='birthdate']";

    private String passport = "//input[@name='pd']";

    private String phone = "//input[@name='phone']";

    private String male = "//div[@value='male']";

    private String female = "//div[@value='female']";

    private String email = "//input[@name='email']";

    private String conditionsAccept = "//span[contains(text(), 'Я согласен с')]/..//input";

    private String justCallAccept = "//span[contains(text(), 'перезвоните мне')]/..//input";

    private String smsInput = "//button[text()='Подтвердить данные']";

    private String acceptData = "//input[@name='sms_code']";

    private String send = "//button[text()='Отправить']";

    public String getFio() {
        return fio;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getPassport() {
        return passport;
    }

    public String getPhone() {
        return phone;
    }

    public String getMale() {
        return male;
    }

    public String getFemale() {
        return female;
    }

    public String getEmail() {
        return email;
    }

    public String getConditionsAccept() {
        return conditionsAccept;
    }

    public String getJustCallAccept() {
        return justCallAccept;
    }

    public String getSmsInput() {
        return smsInput;
    }

    public String getAcceptData() {
        return acceptData;
    }

    public String getSend() {
        return send;
    }
}
