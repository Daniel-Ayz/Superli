package HumanResources.BusinessLayer.EmployeeModule;

import java.util.List;
import HR_Delivery.LicenseType;
public class Driver extends Employee{

    private LicenseType driverLicense;

    public Driver(String name, String id, String personalInfo, BankInformation bankInformation, EmploymentContract employmentContract, List<Role> role, List<Salary> salaries, double baseSalary, LicenseType driverLicense) {
        super(name, id, personalInfo, bankInformation, employmentContract, role, salaries, baseSalary);
        this.driverLicense = driverLicense;
    }

    public LicenseType getDriverLicense() {
        return driverLicense;
    }

    public void setDriverLicense(LicenseType driverLicense) {
        this.driverLicense = driverLicense;
    }


    public LicenseType getLicenseType() {
        return driverLicense;
    }
}
