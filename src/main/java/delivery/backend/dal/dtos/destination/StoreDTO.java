package delivery.backend.dal.dtos.destination;

import delivery.backend.businessLayer.destination.Store;
import delivery.backend.dal.dtos.DTO;

public class StoreDTO extends DTO {
    private int id;
    private String contactName;
    private String phone;
    private String address;
    private String shipmentArea;
    private int northCoordinate;
    private int eastCoordinate;

    public StoreDTO(int id, String address, String shipmentArea, String phone, String contactName, int northCoordinate, int eastCoordinate) {
        this.id = id;
        this.contactName = contactName;
        this.phone = phone;
        this.address = address;
        this.shipmentArea = shipmentArea;
        this.northCoordinate = northCoordinate;
        this.eastCoordinate = eastCoordinate;
    }

    public StoreDTO(Store store) {
        this.id = store.getId();
        this.contactName = store.getContactName();
        this.phone = store.getPhone();
        this.address = store.getAddress();
        this.shipmentArea = store.getShipmentArea().toString();
        this.northCoordinate = store.getCoordinates().getNorthCoordinate();
        this.eastCoordinate = store.getCoordinates().getEastCoordinate();
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
