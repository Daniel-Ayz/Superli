package delivery.backend.businessLayer.TruckDriver;

import HR_Delivery.LicenseType;

public class Driver {
    private String id;
    private String name;
    private LicenseType licenseType;

    public Driver(String id, String name, String licenseType) {
        this.id = id;
        this.name = name;
        setLicenseType(licenseType);
    }


    private void setLicenseType(String licenseType) {
        this.licenseType = LicenseType.valueOf(licenseType);
    }

    public String getName() {
        return name;
    }

    public LicenseType getLicenseType() {
        return licenseType;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", licenseType=" + licenseType +
                '}' +
                "\n";
    }

    public String getId() { return this.id; }

    public static Driver toShipmentDriver(HumanResources.BusinessLayer.EmployeeModule.Driver driver) {
        return new Driver(driver.getId(), driver.getName(), driver.getDriverLicense().toString());
    }
}
