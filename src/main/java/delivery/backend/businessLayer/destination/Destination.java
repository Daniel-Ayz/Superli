package delivery.backend.businessLayer.destination;

public class Destination  {
    protected int id;
    protected String address;
    protected ShipmentArea area;
    protected String phone;
    protected String contactName;
    protected Point coordinates;

    protected Destination(int id, String address, String area, String phone, String contactName, int northCoordinate, int eastCoordinate) {
        this.id = id;
        this. address = address;
        setShipmentArea(area);
        this.phone = phone;
        this.contactName = contactName;
        this.coordinates = new Point(northCoordinate, eastCoordinate);
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getContactName() {
        return contactName;
    }

    public void setShipmentArea(String area) {
        this.area = ShipmentArea.valueOf(area);
    }

    public ShipmentArea getShipmentArea() {
        return area;
    }

    public int getId() {
        return id;
    }

    public Point getCoordinates() { return coordinates; }

    @Override
    public String toString() {
        return "Destination{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", area=" + area +
                ", phone='" + phone + '\'' +
                ", contactName='" + contactName + '\'' +
                ", Location=(" + coordinates.getNorthCoordinate() + ", " + coordinates.getEastCoordinate() + ")" +
                '}' +
                "\n";
    }
}
