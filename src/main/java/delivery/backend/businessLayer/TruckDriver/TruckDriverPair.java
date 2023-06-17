package delivery.backend.businessLayer.TruckDriver;

public class TruckDriverPair {
    private Truck truck;
    private Driver driver;

    public TruckDriverPair(Truck truck, Driver driver) {
        this.truck = truck;
        this.driver = driver;
    }

    public Truck getTruck() {
        return truck;
    }

    public Driver getDriver() {
        return driver;
    }


}
