package delivery.backend.businessLayer.destination;

public class LogisticCenter extends Provider implements Store {
    private Store delegator; // delegator pattern

    public LogisticCenter(int id, String address, String area, String phone, String contactName, int northCoordinate, int eastCoordinate) {
        super(id, address, area, phone, contactName, northCoordinate, eastCoordinate);
        this.delegator = new StoreImpl(id, address, area, phone, contactName, northCoordinate, eastCoordinate);
    }

}
