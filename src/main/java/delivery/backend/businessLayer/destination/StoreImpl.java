package delivery.backend.businessLayer.destination;

import delivery.backend.dal.Repository;
import delivery.backend.dal.dtos.destination.StoreDTO;

public class StoreImpl extends Destination implements Store {
    public StoreImpl(int id, String address, String area, String phone, String contactName, int northCoordinate, int eastCoordinate) {
        super(id, address, area, phone, contactName, northCoordinate, eastCoordinate);
    }

    public StoreImpl(StoreDTO storeDTO) {
        super(storeDTO.getId(), storeDTO.getAddress(), storeDTO.getShipmentArea(), storeDTO.getPhone(), storeDTO.getContactName(), storeDTO.getNorthCoordinate(), storeDTO.getEastCoordinate());
    }

    public static StoreImpl createStore(int size, String address, String area, String phone, String contactName, int northCoordinate, int eastCoordinate) {
        StoreImpl store = new StoreImpl(size, address, area, phone, contactName, northCoordinate, eastCoordinate);
        Repository.getInstance().getStoreDAO().addStore(new StoreDTO(store));
        return store;
    }
}
