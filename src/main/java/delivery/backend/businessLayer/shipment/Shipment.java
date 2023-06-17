package delivery.backend.businessLayer.shipment;

import delivery.backend.businessLayer.destination.Destination;
import delivery.backend.businessLayer.destination.Point;
import delivery.backend.businessLayer.destination.Provider;
import delivery.backend.businessLayer.destination.Store;
import delivery.backend.businessLayer.item.Item;
import delivery.backend.dal.Repository;
import delivery.backend.dal.dtos.shipment.ShipmentDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Shipment {

    private int id;
    private LocalDate date; //date of delivery
    private LocalTime timeOfExit; //time of exit
    private String truckLicenseNumber; //active truck license number
    private String driverName; //active driver name
    private Provider source; //source of delivery
    private Map<Provider, Boolean> providers; // true if the provider was visited
    private List<Store> stores; //list of stores to go through
    private int weight; //delivery weight
    private Treatment treatment; //treatment that was applied
    private List<DocumentedFile> documentedFiles; //list of all documented files
    private boolean isActive; //if delivery is active

    private boolean overWeight; //if delivery is over weight

    private Map<Store, Map<Item, Integer>> itemsToDeliver; //items left to deliver

    private Map<Provider, Map<Item, Integer>> itemsToReceive; //items left to collect

    private Map<Store, LocalTime> estimatedArrivalTimes; //arrival times per destination in shipment
    private final int PROVIDER_TIME_ESTIMATION_FACTOR = 4; //used to determine estimated time of arrival to each store

    private final double STORE_TIME_ESTIMATION_FACTOR = 0.5;


    private Shipment(int id, LocalDate date, LocalTime time, String truckLicenseNumber, String driverName, Provider source, List<DocumentedFile> documentedFiles) {

        this.id = id;
        this.date = date;
        this.timeOfExit = time;
        this.truckLicenseNumber = truckLicenseNumber;
        this.driverName = driverName;
        this.source = source;
        // this.providers = providers.stream().collect(Collectors.toMap(provider -> provider, provider -> false));
        //this.stores = stores;
        this.weight = -1;
        this.treatment = Treatment.NONE;
        this.documentedFiles = documentedFiles;
        this.isActive = true;
        this.overWeight = false;
        // this.itemsToDeliver = itemsToDeliver;
        // this.itemsToReceive = itemsToReceive;
        this.estimatedArrivalTimes = new HashMap<>();
        //setEstimations(source.getCoordinates());
    }

    public Shipment(ShipmentDTO shipmentDTO, Provider source, List<Store> stores, Map<Provider, Boolean> providers, List<DocumentedFile> documentedFiles, Map<Store, Map<Item, Integer>> itemsToDeliver, Map<Provider, Map<Item, Integer>> itemsToReceive, Map<Store, LocalTime> estimatedArrivalTimes) {
        this(shipmentDTO.getId(), shipmentDTO.getDate(), shipmentDTO.getTimeOfExit(), shipmentDTO.getTruckLicenseNumber(), shipmentDTO.getDriverName(), source, documentedFiles);
        this.stores = stores;
        this.providers = providers;
        this.itemsToDeliver = itemsToDeliver;
        this.itemsToReceive = itemsToReceive;
        this.estimatedArrivalTimes = estimatedArrivalTimes;
    }

    public static Shipment loadShipment(ShipmentDTO shipmentDTO, Provider source, List<Store> stores, List<Provider> providersLst, List<DocumentedFile> documentedFiles, Map<Store, Map<Item, Integer>> itemsToDeliver, Map<Provider, Map<Item, Integer>> itemsToReceive) {
        Map<Provider, Boolean> providers = providersLst.stream().collect(Collectors.toMap(provider -> provider, provider -> Repository.getInstance().getProviderPassedThrough(shipmentDTO.getId(), provider.getId())));
        Map<Store, LocalTime> estimatedArrivalTimes = stores.stream().collect(Collectors.toMap(store -> store, store -> Repository.getInstance().getEstimatedArrivalTime(shipmentDTO.getId(), store.getId())));
        Shipment shipment = new Shipment(shipmentDTO, source, stores, providers, documentedFiles, itemsToDeliver, itemsToReceive, estimatedArrivalTimes);

        return shipment;
    }

    private void setProviders(List<Provider> providers) {
        this.providers = providers.stream().collect(Collectors.toMap(provider -> provider, provider -> false));
        Repository.getInstance().updateProviders(id, this.providers);
    }

    private void setStores(List<Store> stores) {
        this.stores = stores;
        Repository.getInstance().updateStores(id, stores);
    }

    private void setItemsToDeliver(Map<Store, Map<Item, Integer>> itemsToDeliver) {
        this.itemsToDeliver = itemsToDeliver;
        Repository.getInstance().updateItemsToDeliver(id, itemsToDeliver);
    }

    private void setItemsToReceive(Map<Provider, Map<Item, Integer>> itemsToReceive) {
        this.itemsToReceive = itemsToReceive;
        Repository.getInstance().updateItemsToReceive(id, itemsToReceive);
    }

    public void setWeight(int weight) {
        Repository.getInstance().getShipmentDAO().setWeight(id, weight);
        this.weight = weight;
    }

    public void setOverWeight(boolean overWeight) {
        Repository.getInstance().getShipmentDAO().setOverWeight(id, overWeight);
        this.overWeight = overWeight;
    }

    public boolean isOverWeight() {
        return overWeight;
    }

    public void setTreatment(Treatment treatment) {
        Repository.getInstance().getShipmentDAO().setTreatment(id, treatment);
        this.treatment = treatment;
    }

    /**
     * this function is called after the truck driver finished passing through all the Providers
     * it updates the providers list and updates the finalDocumentedFiles
     */
    public void finishCollection(Point coordinates) {
        this.isActive = false;
        //update current fields
        providers = providers.entrySet().stream().filter(Map.Entry::getValue)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Repository.getInstance().truncateShipmentProvider(id);
        Repository.getInstance().updateProviders(id, this.providers);

        stores = documentedFiles.stream().map(DocumentedFile::getStore).toList();
        Repository.getInstance().truncateShipmentStores(id);
        Repository.getInstance().updateStores(id, stores);

        setEstimations(coordinates);
    }

    public void setTruckLicenseNumber(String truckLicenseNumber) {
        Repository.getInstance().getShipmentDAO().setTruckLicenseNumber(id, truckLicenseNumber);
        this.truckLicenseNumber = truckLicenseNumber;
    }

    private void setEstimations(Point coordinates) {
        //Clear current estimations:
        estimatedArrivalTimes.clear();

        //Time estimation to finish collection from providers (will be 0 if all passed):
        int driveTimeProviders = (providers.size() - providers.entrySet().stream().filter(Map.Entry::getValue)
                .collect(Collectors.toList()).size()) * PROVIDER_TIME_ESTIMATION_FACTOR;

        //Sort stores:
        stores = stores.stream().sorted(Comparator.comparingInt(Store::getId)).collect(Collectors.toList());
        //Estimations:
        LocalTime accTime = LocalTime.now().plusMinutes(driveTimeProviders);
        for(Store store : stores) {
            int driveTime = (int) (coordinates.euclideanDistance(store.getCoordinates()) * STORE_TIME_ESTIMATION_FACTOR);
            accTime = accTime.plusMinutes(driveTime);
            estimatedArrivalTimes.put(store, accTime);
            coordinates = store.getCoordinates();
        }

        Repository.getInstance().setEstimatedArrivalTimes(id, estimatedArrivalTimes);
    }

    public void setDriverName(String driverName) {
        Repository.getInstance().getShipmentDAO().setDriverName(id, driverName);
        this.driverName = driverName;
    }

    public String getTruckLicenseNumber() {
        return this.truckLicenseNumber;
    }

    /**
     * called when passed a Provider
     * @param supplierId the id of the Provider
     */
    public void goThroughSupplier(int supplierId) {
        Provider provider = providers.keySet().stream().filter(p -> p.getId() == supplierId).findFirst().orElse(null);
        if(provider != null) {
            Repository.getInstance().getShipmentProviderDAO().setVisited(id, supplierId, true);
            providers.put(provider, true);
        }

        //drop?
        setIsActive(!providers.values().stream().allMatch(visited -> visited));
    }

    private void setIsActive(boolean isActive) {
        Repository.getInstance().getShipmentDAO().setIsActive(id, isActive);
        this.isActive = isActive;
    }

    /**
     * returns the shortage in the shipment, the items that were not picked up (when the truck is over weight)
     * @return a map of the shortage - Map<ProviderId, Map<ItemId, amount>>
     */
    public Map<Provider, Map<Item, Integer>> getSupplierShortage() {
        List<Provider> removedProviders = getRemovedProviders();
        Map<Provider, Map<Item, Integer>> output = getSupplierShortageNonInt(removedProviders);
        return output;
    }

    private List<Provider> getRemovedProviders() {
        return providers.entrySet().stream().filter(entry -> !entry.getValue())
                .map(Map.Entry::getKey).toList();
    }

    private Map<Provider, Map<Item, Integer>> getSupplierShortageNonInt(List<Provider> removedProviders) {
        return itemsToReceive.entrySet().stream()
                .filter(x -> removedProviders.contains(x.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map<Integer, Map<Integer, Integer>> getSupplierShortageByIds(Map<Provider, Map<Item, Integer>> providerMap) {
        Map<Integer, Map<Integer, Integer>> output = new HashMap<>();
        for (Provider provider : providerMap.keySet()) {
            Map<Integer, Integer> itemToAmount = new HashMap<>();
            for (Item item : providerMap.get(provider).keySet()) {
                itemToAmount.put(item.getId(), providerMap.get(provider).get(item));
            }

            output.put(provider.getId(), itemToAmount);
        }
        return output;
    }

    /**
     * updates the documented files
     * @param idGenerator the id of the first documented file
     * @return - the new idGenerator
     */
    public int updateDocumentedFiles(int idGenerator) {
        Map<Store, Map<Item, Integer>> distributions = distributeItems();
        //int idGenerator = 0;
        clearDocumentedFiles();
        for (Store store : distributions.keySet()) {
            documentedFiles.add(DocumentedFile.createDocumentedFile(idGenerator, store, distributions.get(store), id));
            idGenerator++;
        }

        return idGenerator;
    }

    private void clearDocumentedFiles() {
        Repository.getInstance().getDocumentedFileDAO().truncate(this.id);
        documentedFiles.clear();
    }

    /**
     * @return distribution of items collected to the stores
     */
    private Map<Store, Map<Item, Integer>> distributeItems() {
        //get all providers we went through
        List<Provider> goodProviders = providers.entrySet().stream().filter(Map.Entry::getValue)
                .map(Map.Entry::getKey).toList();

        Map<Provider, Map<Item, Integer>> distributors = new HashMap<>();
        for (Provider provider : goodProviders) {
            distributors.put(provider, itemsToReceive.get(provider));
        }

        Map<Store, Map<Item, Integer>> output = new HashMap<>();

        //build wanted to be output:
        for (Provider provider : distributors.keySet()) {
            Map<Item, Integer> items = distributors.get(provider);
            for (Item item : items.keySet()) {

                //the amount of this item from this provider
                int amount = items.get(item);

                //all stores that ordered that item
                List<Store> relevantStores = itemsToDeliver.entrySet().stream()
                        .filter(entry -> entry.getValue().containsKey(item) && entry.getValue().get(item) > 0)
                        .collect(LinkedList::new, (list, entry) -> list.add(entry.getKey()), LinkedList::addAll);

                //distribute item between the relevant stores
                while (amount > 0) {
                    if (relevantStores.isEmpty()) {
                        throw new RuntimeException("No Store that ordered item " + item.getName());
                    }

                    Store store = relevantStores.remove(0);
                    int requestedAmount = itemsToDeliver.get(store).get(item);
                    int shipmentAmount = Math.min(amount, requestedAmount);
                    itemsToDeliver.get(store)
                            .put(item, requestedAmount - shipmentAmount);

                    if (itemsToDeliver.get(store).get(item) == 0)
                        itemsToDeliver.get(store).remove(item);

                    if (itemsToDeliver.get(store).isEmpty())
                        itemsToDeliver.remove(store);

                    amount -= shipmentAmount;
                    if (!output.containsKey(store)) {
                        output.put(store, new HashMap<>());
                        output.get(store).put(item, shipmentAmount);
                    }
                    else if (!output.get(store).containsKey(item)) {
                        output.get(store).put(item, shipmentAmount);
                    }
                    else
                        output.get(store).put(item, output.get(store).get(item) + shipmentAmount);

                }
            }
        }

        Repository.getInstance().truncateItemsToDeliver(this.id);
        Repository.getInstance().updateItemsToDeliver(this.id, itemsToDeliver);

        return output;
    }

    /**
     * returns the shortage in the shipment, the items that would not be provided (when the truck is over weight)
     * @return a map of the shortage - Map<StoreId, Map<ItemId, amount>>
     */
    public Map<Store, Map<Item, Integer>> getStoreShortage() {
        return itemsToDeliver;
    }

    @Override
    public String toString() {
        return "Shipment{" +
                "date=" + date + "\n" +
                ", timeOfExit=" + timeOfExit + "\n" +
                ", truckLicenseNumber='" + truckLicenseNumber + '\'' + "\n" +
                ", driverName='" + driverName + '\'' + "\n" +
                ", source=" + source.getId() + "\n" +
                ", providers=" + providers.keySet().stream().map(Destination::getId).toList() + "\n" +
                ", stores=" + stores.stream().map(Store::getId).toList() + "\n" +
                ", weight=" + weight + "\n" +
                ", treatment=" + treatment + "\n" +
                '}' + "\n";
    }

    public boolean getIsActive() {
        return isActive;
    }

    public LocalTime getExpectedTime(Store store) {
        return estimatedArrivalTimes.get(store);
    }

    public void delayDistribution(int storeId, int delayTime) {
        int index = IntStream.range(0, stores.size())
                .filter(i -> stores.get(i).getId() == storeId)
                .findFirst()
                .orElse(-1);
        if(index == -1) {
            throw new IllegalArgumentException("Store Id: " + storeId + " is not a part of the shipment: "+ id);
        }

        List<Store> relevant = stores.subList(index, stores.size());
        relevant.forEach(store -> setEstimationArrivalTime(store, estimatedArrivalTimes.get(store).plusMinutes(delayTime)));
    }

    private void setEstimationArrivalTime(Store store, LocalTime time) {
        Repository.getInstance().getShipmentEstimationsDAO().setEstimation(id, store.getId(), time);
        this.estimatedArrivalTimes.put(store, time);
    }

    public static Shipment createShipment(int id, LocalDate date, LocalTime time, String truckLicenseNumber, String driverName, Provider source, List<Provider> providers,
                                          List<Store> stores, List<DocumentedFile> documentedFiles,
                                          Map<Store, Map<Item, Integer>> itemsToDeliver, Map<Provider, Map<Item, Integer>> itemsToReceive) {
        Shipment shipment = new Shipment(id, date, time, truckLicenseNumber, driverName, source, documentedFiles);
        Repository.getInstance().getShipmentDAO().addShipment(new ShipmentDTO(shipment));
        shipment.setProviders(providers);
        shipment.setStores(stores);
        shipment.setItemsToReceive(itemsToReceive);
        shipment.setItemsToDeliver(itemsToDeliver);
        shipment.setEstimations(source.getCoordinates());
        return shipment;
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

    public String getDriverName() {
        return driverName;
    }

    public int getWeight() {
        return weight;
    }

    public boolean isActive() {
        return isActive;
    }

    public Treatment getTreatment() {
        return treatment;
    }

    public Provider getSource() {
        return source;
    }
}
