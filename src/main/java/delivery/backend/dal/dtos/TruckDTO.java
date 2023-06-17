package delivery.backend.dal.dtos;

import delivery.backend.businessLayer.TruckDriver.Truck;

public class TruckDTO extends DTO {
    private String licenseNumber;
    private String model;
    private int weight;
    private int maxWeight;
    private boolean available;
    private String licenseType;

    public TruckDTO(String licenseNumber, String model, int weight, int maxWeight, boolean available, String licenseType) {
        this.licenseNumber = licenseNumber;
        this.model = model;
        this.weight = weight;
        this.maxWeight = maxWeight;
        this.available = available;
        this.licenseType = licenseType;
    }

    public TruckDTO(Truck truck, boolean isAvailable) {
        this.licenseNumber = truck.getLicenseNumber();
        this.model = truck.getModel();
        this.weight = truck.getWeight();
        this.maxWeight = truck.getMaxWeight();
        this.available = isAvailable;
        this.licenseType = truck.getLicenseType().toString();
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public String getModel() {
        return model;
    }

    public int getWeight() {
        return weight;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getLicenseType() {
        return licenseType;
    }
}
