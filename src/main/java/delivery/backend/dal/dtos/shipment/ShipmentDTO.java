package delivery.backend.dal.dtos.shipment;

import delivery.backend.businessLayer.shipment.Shipment;
import delivery.backend.dal.dtos.DTO;

import java.time.LocalDate;
import java.time.LocalTime;

public class ShipmentDTO extends DTO {
    private int id;
    private LocalDate date;
    private LocalTime timeOfExit;
    private String truckLicenseNumber;
    private String driverName;
    private int sourceId;
    private int weight;
    private boolean isActive;
    private boolean overWeight;
    private String treatment;

    public ShipmentDTO(int id, LocalDate date, LocalTime timeOfExit, String truckLicenseNumber, String driverName, int sourceId, int weight, boolean isActive, boolean overWeight, String treatment) {
        this.id = id;
        this.date = date;
        this.timeOfExit = timeOfExit;
        this.truckLicenseNumber = truckLicenseNumber;
        this.driverName = driverName;
        this.sourceId = sourceId;
        this.weight = weight;
        this.isActive = isActive;
        this.overWeight = overWeight;
        this.treatment = treatment;
    }

    public ShipmentDTO(Shipment shipment) {
        this.id = shipment.getId();
        this.date = shipment.getDate();
        this.timeOfExit = shipment.getTimeOfExit();
        this.truckLicenseNumber = shipment.getTruckLicenseNumber();
        this.driverName = shipment.getDriverName();
        this.sourceId = shipment.getSource().getId();
        this.weight = shipment.getWeight();
        this.isActive = shipment.isActive();
        this.overWeight = shipment.isOverWeight();
        this.treatment = shipment.getTreatment().toString();
    }

    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTimeOfExit() {
        return timeOfExit;
    }

    public String getTruckLicenseNumber() {
        return truckLicenseNumber;
    }

    public String getDriverName() {
        return driverName;
    }

    public int getSourceId() {
        return sourceId;
    }

    public int getWeight() {
        return weight;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isOverWeight() {
        return overWeight;
    }

    public String getTreatment() {
        return treatment;
    }
}
