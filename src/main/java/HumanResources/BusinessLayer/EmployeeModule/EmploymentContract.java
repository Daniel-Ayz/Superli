package HumanResources.BusinessLayer.EmployeeModule;

import java.util.Date;

public class EmploymentContract {
    private Date startDate;
    private String termsOfEmployment;

    public EmploymentContract(Date startDate, String termsOfEmployment) {
        this.startDate = startDate;
        this.termsOfEmployment = termsOfEmployment;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getTermsOfEmployment() {
        return termsOfEmployment;
    }

    public void setTermsOfEmployment(String termsOfEmployment) {
        this.termsOfEmployment = termsOfEmployment;
    }

    public String toString() {
        return "Start Date: " + startDate + ", Terms of Employment: " + termsOfEmployment;
    }

    public boolean equals(EmploymentContract employmentContract) {
        if (this.startDate.equals(employmentContract.startDate) && this.termsOfEmployment.equals(employmentContract.termsOfEmployment)) {
            return true;
        }
        return false;
    }
}
