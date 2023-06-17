package delivery.backend.businessLayer.TruckDriver;

import HR_Delivery.LicenseType;
import delivery.backend.dal.Repository;
import delivery.backend.dal.dtos.TruckDTO;

public class Truck {
    private String licenseNumber;
    private String model;
    private  int weight;
    private int maxWeight;
    private LicenseType licenseType;

    private Truck(String licenseNumber, String model, int weight, int maxWeight, String licenseType){
        this.licenseNumber = licenseNumber;
        this.model = model;
        this.weight = weight;
        this.maxWeight = maxWeight;
        setLicenseType(licenseType);
    }

    public Truck(TruckDTO truckDTO) {
        this(truckDTO.getLicenseNumber(), truckDTO.getModel(), truckDTO.getWeight(), truckDTO.getMaxWeight(), truckDTO.getLicenseType());
    }

    public String getModel() {
        return model;
    }

    public static Truck createTruck(String licenseNumber, String model, int weight, int maxWeight, boolean isAvailable, String licenseType) {
        Truck truck = new Truck(licenseNumber, model, weight, maxWeight, licenseType);
        Repository.getInstance().getTruckDAO().addTruck(new TruckDTO(truck, isAvailable));
        return  truck;
    }

    private void setLicenseType(String licenseType) {
        this.licenseType = LicenseType.valueOf(licenseType);
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    @Override
    public String toString() {
        return "Truck{" +
                "licenseNumber='" + licenseNumber + '\'' +
                ", model='" + model + '\'' +
                ", weight=" + weight +
                ", maxWeight=" + maxWeight +
                ", licenseType=" + licenseType +
                '}' +
                "\n";
    }

    public LicenseType getLicenseType() {
        return licenseType;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public int getWeight() {
        return weight;
    }
}
