package delivery.backend.businessLayer.destination;

import delivery.backend.dal.Repository;
import delivery.backend.dal.dtos.destination.ProviderDTO;
import delivery.backend.dal.dtos.destination.StoreDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DestinationController {
    private List<Destination> destinations;

    public DestinationController(){
        this.destinations = new ArrayList<>();

        addLogisticCenter();
    }

    public void addLogisticCenter() {
        LogisticCenter logisticCenter = new LogisticCenter(0, "Logistic Center", "CENTER",
                "03-1234567", "Logistic Center", 0, 0);

        destinations.add(logisticCenter);
        try {
            if (Repository.getInstance().getProviderDAO().selectAll().stream().map(providerDTO -> (ProviderDTO) providerDTO).noneMatch(providerDTO -> providerDTO.getId() == 0)) {
                Provider.createProvider(0, "Logistic Center", "CENTER", "03-1234567",
                        "Logistic Center", 0, 0);
                StoreImpl.createStore(0, "Logistic Center", "CENTER", "03-1234567",
                        "Logistic Center", 0, 0);
            }
        }
        catch(Exception e) {
            System.out.println("Failed to load for logistic center: " + e.getMessage());
        }
    }

    /**
     * Adds a destination
     * @param address - address of the destination
     * @param area - area of the destination (ShipmentArea)
     * @param phone - phone number of the destination
     * @param contactName - contact name of the phone number
     * @param isProvider provider or store
     */
    public void addDestination(String address, String area, String phone, String contactName, int northCoordinate, int eastCoordinate,boolean isProvider) {
        if(isProvider) {
            destinations.add(Provider.createProvider(destinations.size(), address, area, phone, contactName, northCoordinate, eastCoordinate));
        }
        else {
            destinations.add(StoreImpl.createStore(destinations.size(), address, area, phone, contactName, northCoordinate, eastCoordinate));
        }
    }

    /**
     * Returns a list of all stores
     * @return List<IStore>
     */
    public List<Store> getStores() {
        return destinations.stream().filter(d -> d instanceof Store).toList().stream().map(d -> (Store) d).toList();
    }

    /**
     * Returns a list of all providers
     * @return List<Provider>
     */
    public List<Provider> getProviders() {
        return destinations.stream().filter(d -> d instanceof Provider).toList().stream().map(d -> (Provider) d).toList();
    }

    /**
     * Returns a Store by id
     * @param storeId
     * @return Store
     */
    public Store getStore(Integer storeId) {
        Destination destination = destinations.stream().filter(d -> d.getId() == storeId).findFirst().orElse(null);
        if(destination instanceof Store) {
            return (Store) destination;
        }
        throw new IllegalArgumentException("no such store");
    }

    /**
     * Returns a Provider by id
     * @param providerId
     * @return Provider
     */
    public Provider getProvider(Integer providerId) {
        Destination destination = destinations.stream().filter(d -> d.getId() == providerId).findFirst().orElse(null);
        if(destination instanceof Provider) {
            return (Provider) destination;
        }
        throw new IllegalArgumentException("no such provider");
    }

    public void loadProviders() {
        try {
            Repository.getInstance().getProviderDAO().selectAll().stream().map((dto) -> (ProviderDTO) dto).forEach((providerDTO) -> {
                if (providerDTO.getId() != 0) {
                    destinations.add(new Provider(providerDTO));
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to load Providers");
        }
    }

    public void loadStores() {
        try {
            Repository.getInstance().getStoreDAO().selectAll().stream().map((dto) -> (StoreDTO) dto).forEach((storeDTO) -> {
                if (storeDTO.getId() != 0) {
                    destinations.add(new StoreImpl(storeDTO));
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to load Providers");
        }
    }

    public boolean doesStoreExist(int id) {
        return destinations.stream().filter(destination -> destination.getId() == id).collect(Collectors.toList()).size() > 0;
    }
}
