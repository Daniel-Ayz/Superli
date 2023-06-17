package delivery.backend.businessLayer.destination;

public interface Store {
    void setShipmentArea(String area);
    ShipmentArea getShipmentArea();
    Point getCoordinates();
    int getId();
    String getAddress();
    String getPhone();
    String getContactName();

}
