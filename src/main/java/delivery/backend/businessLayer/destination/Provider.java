package delivery.backend.businessLayer.destination;

import delivery.backend.dal.Repository;
import delivery.backend.dal.dtos.destination.ProviderDTO;

public class Provider extends Destination {
    public Provider(int id, String address, String area, String phone, String contactName, int northCoordinate, int eastCoordinate) {
        super(id, address, area, phone, contactName, northCoordinate, eastCoordinate);
    }

    public Provider(ProviderDTO providerDTO) {
        super(providerDTO.getId(), providerDTO.getAddress(), providerDTO.getShipmentArea(), providerDTO.getPhone(), providerDTO.getContactName(), providerDTO.getNorthCoordinate(), providerDTO.getEastCoordinate());
    }

    public static Provider createProvider(int size, String address, String area, String phone, String contactName, int northCoordinate, int eastCoordinate) {
        Provider provider = new Provider(size, address, area, phone, contactName, northCoordinate, eastCoordinate);
        Repository.getInstance().getProviderDAO().addProvider(new ProviderDTO(provider));
        return provider;
    }
}
