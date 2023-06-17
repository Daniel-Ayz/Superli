package delivery.backend.dal.dtos.destination;

import delivery.backend.businessLayer.destination.Provider;
import delivery.backend.dal.dtos.DTO;

public class ProviderDTO extends DTO {
    private int id;
    private String contactName;
    private String phone;
    private String address;
    private String shipmentArea;
    private int northCoordinate;
    private int eastCoordinate;

    public ProviderDTO(int id, String address, String shipmentArea, String phone, String contactName, int northCoordinate, int eastCoordinate) {
        this.id = id;
        this.contactName = contactName;
        this.phone = phone;
        this.address = address;
        this.shipmentArea = shipmentArea;
        this.northCoordinate = northCoordinate;
        this.eastCoordinate = eastCoordinate;
    }

    public ProviderDTO(Provider provider) {
        this.id = provider.getId();
        this.contactName = provider.getContactName();
        this.phone = provider.getPhone();
        this.address = provider.getAddress();
        this.shipmentArea = provider.getShipmentArea().toString();
        this.northCoordinate = provider.getCoordinates().getNorthCoordinate();
        this.eastCoordinate = provider.getCoordinates().getEastCoordinate();
    }

    public int getId() {
        return id;
    }

    public String getContactName() {
        return contactName;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getShipmentArea() {
        return shipmentArea;
    }

    public int getNorthCoordinate() {
        return northCoordinate;
    }

    public int getEastCoordinate() {
        return eastCoordinate;
    }


}
