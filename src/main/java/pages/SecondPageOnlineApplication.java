package pages;

import org.springframework.stereotype.Component;

@Component
public class SecondPageOnlineApplication {

    private String givingDate = "//input[@name='givingDate']";

    private String departmentCode = "//input[@name='departmentCode']";

    private String departmentInfo = "//input[@name='departmentInfo']";

    private String birthPlace = "//input[@name='birthPlace']";

    public String getGivingDate() {
        return givingDate;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public String getDepartmentInfo() {
        return departmentInfo;
    }

    public String getBirthPlace() {
        return birthPlace;
    }
}
