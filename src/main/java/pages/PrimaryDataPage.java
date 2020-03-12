package pages;

import org.springframework.stereotype.Component;

/**
 * Описание элементов страницы
 */
@Component
public class PrimaryDataPage {

    private String checkboxAccept = "//span[@class='checkbox-style']\n";
    public String getCheckboxAccept() {
        return checkboxAccept;
    }


    private String accept = "//div[@class='buttons button-border']\n";
    public String getAccept() {
        return accept;
    }


    private String smsVerification = "//div[@class='lt-retail-panel lt-tool']//input[@class='input-component']\n";
    public String getSmsVerification() {
        return smsVerification;
    }

}

