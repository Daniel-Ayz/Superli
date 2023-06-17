package delivery.backend.businessLayer.shipment;

import delivery.backend.businessLayer.destination.*;
import delivery.backend.businessLayer.TruckDriver.TruckDriverController;
import delivery.backend.businessLayer.TruckDriver.TruckDriverPair;
import delivery.backend.businessLayer.TruckDriver.Driver;
import delivery.backend.businessLayer.item.Item;
import delivery.backend.businessLayer.item.ItemController;
import delivery.backend.businessLayer.TruckDriver.Truck;
import delivery.backend.businessLayer.TruckDriver.TruckController;
import delivery.backend.dal.Repository;
import delivery.backend.dal.dtos.file.DocumentedFileDTO;
import delivery.backend.dal.dtos.file.FileItemDTO;
import delivery.backend.dal.dtos.shipment.ShipmentDTO;
import delivery.backend.dal.dtos.shipmentFactory.ShipmentFactoryDTO;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

public class ShipmentFactory {
    private Map<Integer, Shipment> shipments;

    private TruckDriverController truckDriverController;
    private DestinationController destinationController;
    private ItemController itemController;
    private int documentIdGenerator = 0;
    private Map<Integer, Map<Integer, Integer>> stockLeftoversId; //stores
    private Map<Integer, Map<Integer, Integer>> supplyLeftoversId; //providers

    public ShipmentFactory(TruckController truckController,
                           DestinationController destinationController, ItemController itemController) {
        shipments = new HashMap<>();
        this.truckDriverController = new TruckDriverController(truckController);
        this.destinationController = destinationController;
        this.itemController = itemController;
        this.stockLeftoversId = new HashMap<>();
        this.supplyLeftoversId = new HashMap<>();
    }

    private Map<Store, Map<Item, Integer>> createStockOrder(Map<Integer, Map<Integer, Integer>> stockOrderId) {
        Map<Store, Map<Item, Integer>> stockOrder = new HashMap<>();

        // copy stock order
        for (Integer storeId : stockOrderId.keySet()) {
            Store store =  destinationController.getStore(storeId);
            stockOrder.put(store, new HashMap<>());
            for (Integer itemId : stockOrderId.get(storeId).keySet()) {
                Item item = itemController.getItem(itemId);
                stockOrder.get(store).put(item, stockOrderId.get(storeId).get(itemId));
            }
        }

        return stockOrder;
    }

    private Map<Provider, Map<Item, Integer>> createSupplyOrder(Map<Integer, Map<Integer, Integer>> supplyOrderId) {
        Map<Provider, Map<Item, Integer>> supplyOrder = new HashMap<>();

        // copy supply order
        for (Integer providerId : supplyOrderId.keySet()) {
            Provider provider = destinationController.getProvider(providerId);
            supplyOrder.put(provider, new HashMap<>());
            for (Integer itemId : supplyOrderId.get(providerId).keySet()) {
                Item item = itemController.getItem(itemId);
                supplyOrder.get(provider).put(item, supplyOrderId.get(providerId).get(itemId));
            }
        }

        return supplyOrder;
    }

    private Map<ShipmentArea, Map<Store, Map<Item, Integer>>> createStockOrderByArea(Map<Store, Map<Item, Integer>> stockOrder) {
        Map<ShipmentArea, Map<Store, Map<Item, Integer>>> stockOrderByArea = new HashMap<>();

        // sort stores by area
        for (Store store : stockOrder.keySet()) {
            ShipmentArea area = store.getShipmentArea();
            if (!stockOrderByArea.containsKey(area)) {
                stockOrderByArea.put(area, new HashMap<>());
            }
            stockOrderByArea.get(area).put(store, stockOrder.get(store));
        }

        return stockOrderByArea;
    }

    private List<DocumentedFile> createDocumentedFilesForArea(Map<ShipmentArea, Map<Store, Map<Item, Integer>>> stockOrderByArea, ShipmentArea area, int shipmentId) {
        // create documented files for each store
        List<DocumentedFile> documentedFilesByArea = new LinkedList<>();

        for (Store store : stockOrderByArea.get(area).keySet()) {
            Map<Item, Integer> items = stockOrderByArea.get(area).get(store);
            DocumentedFile file = DocumentedFile.createDocumentedFile(getNewDocumentedFileId(), store, items, shipmentId);
            documentedFilesByArea.add(file);
        }

        return documentedFilesByArea;
    }

    private int getNewDocumentedFileId() {
        int newId = documentIdGenerator;
        Repository.getInstance().getShipmentFactoryDAO().updateDocumentIdGenerator(documentIdGenerator + 1);
        documentIdGenerator++;
        return newId;
    }

    private Map<ShipmentArea, Map<Provider, Map<Item, Integer>>> createSupplyOrderByArea(Map<ShipmentArea, Map<Store, Map<Item, Integer>>> stockOrderByArea, Map<Provider, Map<Item, Integer>> supplyOrder) {
        Map<ShipmentArea, Map<Provider, Map<Item, Integer>>> supplyOrderByArea = new HashMap<>();

        for (ShipmentArea area : stockOrderByArea.keySet()) {
            Map<Store, Map<Item, Integer>> orders = stockOrderByArea.get(area);
            supplyOrderByArea.put(area, new HashMap<>());
            for (Store store : orders.keySet()) {
                Map<Item, Integer> items = orders.get(store);
                for (Item item : items.keySet()) {
                    List<Provider> relevantProviders = supplyOrder.entrySet().stream()
                            .filter(entry -> entry.getValue().containsKey(item) && entry.getValue().get(item) > 0)
                            .collect(LinkedList::new, (list, entry) -> list.add(entry.getKey()), LinkedList::addAll);

                    int amount = items.get(item);

                    while (amount > 0) {
                        if (relevantProviders.isEmpty()) {
                            throw new RuntimeException("No provider for item " + item.getName());
                        }

                        Provider relevantProvider = relevantProviders.remove(0);
                        int availableAmount = supplyOrder.get(relevantProvider).get(item);
                        int shipmentAmount = Math.min(amount, availableAmount);
                        supplyOrder.get(relevantProvider)
                                .put(item, supplyOrder.get(relevantProvider).get(item) - shipmentAmount);
                        amount -= shipmentAmount;

                        if (!supplyOrderByArea.get(area).containsKey(relevantProvider)) {
                            supplyOrderByArea.get(area).put(relevantProvider, new HashMap<>());
                            supplyOrderByArea.get(area).get(relevantProvider).put(item, shipmentAmount);
                        }
                        else if (supplyOrderByArea.get(area).get(relevantProvider).containsKey(item)) {
                            supplyOrderByArea.get(area).get(relevantProvider)
                                    .put(item, supplyOrderByArea.get(area).get(relevantProvider).get(item) + shipmentAmount);
                        }
                        else {
                            supplyOrderByArea.get(area).get(relevantProvider).put(item, shipmentAmount);
                        }


                    }
                }
            }
        }

        return supplyOrderByArea;
    }

    /**
     * Create a new shipment
     * @param stockOrderId - a map of stores to a map of items to the amount of items to order
     * @param supplyOrderId - a map of providers to a map of items to the amount of items ordered from the provider
     * @return Map of shipment area to shipment id
     */
    public Map<ShipmentArea, Integer> createShipments(Map<Integer, Map<Integer, Integer>> stockOrderId,
                                Map<Integer, Map<Integer, Integer>> supplyOrderId) {
        Map<Store, Map<Item, Integer>> stockOrder = createStockOrder(stockOrderId);

        Map<Provider, Map<Item, Integer>> supplyOrder = createSupplyOrder(supplyOrderId);

        Map<ShipmentArea, Map<Store, Map<Item, Integer>>> stockOrderByArea = createStockOrderByArea(stockOrder);

        Map<ShipmentArea, Map<Provider, Map<Item, Integer>>> supplyOrderByArea = createSupplyOrderByArea(stockOrderByArea, supplyOrder);

        Map<ShipmentArea, Truck> trucksByArea = new HashMap<>();
        Map<ShipmentArea, Driver> driversByArea = new HashMap<>();
        // get a truck and a driver for each area
        for (ShipmentArea area : supplyOrderByArea.keySet()) {
            try {
                TruckDriverPair pair = truckDriverController.getAvailableTruckDriverPair(convertToDateViaInstant(getClosestSunday()));
                trucksByArea.put(area, pair.getTruck());
                driversByArea.put(area, pair.getDriver());
            }
            catch(Exception ex) { concatToStockShortage(stockOrderByArea.get(area)); concatToSupplyShortage(supplyOrderByArea.get(area)); }
        } //need to fix- what if no drivers are available?

        Map<ShipmentArea, Integer> shipmentIds = new HashMap<>();

        // create shipments for each area
        for (ShipmentArea area : trucksByArea.keySet()) {
            List<Provider> providers = supplyOrderByArea.get(area).keySet().stream().toList();

            int shipmentId = shipments.size();
            List<DocumentedFile> files = createDocumentedFilesForArea(stockOrderByArea, area, shipmentId);

            LocalDate nextSunday = getClosestSunday();
            //create for sunday:
            Shipment shipment = sundayShipment(shipmentId, nextSunday, trucksByArea.get(area).getLicenseNumber(),
                    driversByArea.get(area).getName(),providers.get(0), providers,
                    stockOrderByArea.get(area).keySet().stream().toList(),
                    files, stockOrderByArea.get(area), supplyOrderByArea.get(area), driversByArea.get(area).getId());

            shipments.put(shipmentId, shipment);
            shipmentIds.put(area, shipmentId);
        }

        return shipmentIds;
    }

    private Date convertToDateViaInstant(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    private Shipment sundayShipment(int id, LocalDate nextSunday, String truckLicenseNumber, String driverName, Provider source, List<Provider> providers,
                                    List<Store> stores, List<DocumentedFile> documentedFiles,
                                    Map<Store, Map<Item, Integer>> itemsToDeliver, Map<Provider, Map<Item, Integer>> itemsToReceive, String driverId) {
        if(nextSunday.equals(LocalDate.now()))
            return Shipment.createShipment(id, nextSunday, LocalTime.now(), truckLicenseNumber,
                    driverName, source, providers,
                    stores, documentedFiles, itemsToDeliver, itemsToReceive);
        else
            return Shipment.createShipment(id, nextSunday, LocalTime.of(8,0), truckLicenseNumber,
                    driverName, source, providers,
                    stores, documentedFiles, itemsToDeliver, itemsToReceive);
    }

    /**
     * Apply treatment to a shipment
     * @param shipmentId - shipment id
     * @param treatment - treatment to be applied (Treatment)
     * @param supplierId - supplier id
     */
    public void applyTreatment(int shipmentId, Treatment treatment, int supplierId) {
        Shipment shipment = shipments.get(shipmentId);
        switch (treatment) {
            case DROPPING_OR_SWITCHING_DESTINATION, DROPPING_ITEMS -> {
                shipment.setTreatment(treatment);

                updateDocumentIdGenerator(shipment.updateDocumentedFiles(documentIdGenerator));
                concatToSupplyShortage(shipment.getSupplierShortage());
                concatToStockShortage(shipment.getStoreShortage());
                shipment.finishCollection(destinationController.getProvider(supplierId).getCoordinates());
            }
            case SWITCHING_TRUCK -> {
                shipment.setTreatment(treatment);
                switchTruck(shipmentId);
            }
            case NONE -> {
                // do nothing
            }
        }

        shipment.setOverWeight(false);
    }

    private void updateDocumentIdGenerator(int newId) {
        Repository.getInstance().getShipmentFactoryDAO().updateDocumentIdGenerator(newId);
        documentIdGenerator = newId;
    }

    /**
     * Switch the truck of a shipment
     * @param shipmentId - shipment id
     */
    private void switchTruck(int shipmentId) {
        Shipment shipment = shipments.get(shipmentId);
        TruckDriverPair truckDriverPair = truckDriverController.switchTruck(shipment.getTruckLicenseNumber(), convertToDateViaInstant(shipment.getDate()));

        Truck truck = truckDriverPair.getTruck();
        Driver driver = truckDriverPair.getDriver();

        shipment.setTruckLicenseNumber(truck.getLicenseNumber());
        shipment.setDriverName(driver.getName());
    }

    /**
     * Set the weight of a shipment after its weight was measured
     * @param shipmentId - shipment id
     * @param weight - shipment weight
     */
    public void setWeight(int shipmentId, int supplierId, int weight) {
        Shipment shipment = shipments.get(shipmentId);
        Truck truck = truckDriverController.getTruck(shipment.getTruckLicenseNumber());
        if (truck.getMaxWeight() >= weight){
            shipment.setWeight(weight);
            shipment.goThroughSupplier(supplierId);

            if (!shipment.getIsActive()) {
                truckDriverController.deactivateTruck(truck.getLicenseNumber());
            }
        }
        else
            shipment.setOverWeight(true);
    }

    private Map<Integer, Map<Integer, Integer>> getByIdsStores(Map<Store, Map<Item, Integer>> toCast) {
        Map<Integer, Map<Integer, Integer>> output = new HashMap<>();
        for(Store store : toCast.keySet()) {
            Map<Integer, Integer> itemsToAmount = new HashMap<>();
            for(Item item : toCast.get(store).keySet()) {
                itemsToAmount.put(item.getId(), toCast.get(store).get(item));
            }
            output.put(store.getId(), itemsToAmount);
        }
        return output;
    }

    private Map<Integer, Map<Integer, Integer>> getByIdsProviders(Map<Provider, Map<Item, Integer>> toCast) {
        Map<Integer, Map<Integer, Integer>> output = new HashMap<>();
        for(Provider provider : toCast.keySet()) {
            Map<Integer, Integer> itemsToAmount = new HashMap<>();
            for(Item item : toCast.get(provider).keySet()) {
                itemsToAmount.put(item.getId(), toCast.get(provider).get(item));
            }
            output.put(provider.getId(), itemsToAmount);
        }
        return output;
    }

    /**
     * concat a shortage to the stock shortage
     * @param toConcatNotCasted - shortage to concat, Map<store, Map<item, amount>>
     */
    private void concatToStockShortage(Map<Store, Map<Item, Integer>> toConcatNotCasted) {
        Map<Integer, Map<Integer, Integer>> toConcat = getByIdsStores(toConcatNotCasted);
        for(Integer storeId : toConcat.keySet()) {
            if(!stockLeftoversId.keySet().contains(storeId)) {
                stockLeftoversId.put(storeId, toConcat.get(storeId));
                continue;
            }

            Map<Integer, Integer> items = toConcat.get(storeId);
            Map<Integer, Integer> acc = stockLeftoversId.get(storeId);
            for(Integer itemId : items.keySet()) {
                if(acc.keySet().contains(itemId)) {
                    acc.put(itemId, acc.get(itemId) + items.get(itemId));
                }
                else {
                    acc.put(itemId, items.get(itemId));
                }
            }
            acc.keySet().stream().forEach(itemId -> persistStockShortage(storeId, itemId, acc.get(itemId)));
            stockLeftoversId.put(storeId, acc);
        }
    }

    private void persistStockShortage(int storeId, int itemId, int amount) {
        Repository.getInstance().addStockShortage(storeId, itemId, amount);
    }

    /**
     * concat a shortage to the supply shortage
     * @param toConcatNotCasted - shortage to concat, Map<provider, Map<item, amount>>
     */
    private void concatToSupplyShortage(Map<Provider, Map<Item, Integer>> toConcatNotCasted) {
        Map<Integer, Map<Integer, Integer>> toConcat = getByIdsProviders(toConcatNotCasted);
        for(Integer providerId : toConcat.keySet()) {
            if(!supplyLeftoversId.keySet().contains(providerId)) {
                supplyLeftoversId.put(providerId, toConcat.get(providerId));
                continue;
            }

            Map<Integer, Integer> items = toConcat.get(providerId);
            Map<Integer, Integer> acc = supplyLeftoversId.get(providerId);
            for(Integer itemId : items.keySet()) {
                if(acc.keySet().contains(itemId)) {
                    acc.put(itemId, acc.get(itemId) + items.get(itemId));
                }
                else {
                    acc.put(itemId, items.get(itemId));
                }
            }
            acc.keySet().stream().forEach(itemId -> persistSupplyShortage(providerId, itemId, acc.get(itemId)));
            supplyLeftoversId.put(providerId, acc);
        }
    }

    private void persistSupplyShortage(int providerId, int itemId, int amount) {
        Repository.getInstance().addSupplyShortage(providerId, itemId, amount);
    }

    public Map<Integer, Map<Integer, Integer>> getStoreShortages() {
        if (stockLeftoversId.isEmpty())
            throw new RuntimeException("there are no store shortages!");
        return stockLeftoversId;
    }

    public Map<Integer, Map<Integer, Integer>> getProvidersLeftovers() {
        if (supplyLeftoversId.isEmpty())
            throw new RuntimeException("there are no provider leftovers!");
        return supplyLeftoversId;
    }

    /**
     * fill the shortages by creating shipments
     * @return  the created shipments - Map<ShipmentArea, shipmentId>
     */
    public Map<ShipmentArea, Integer> fillShortages() {
        if(stockLeftoversId.isEmpty() && supplyLeftoversId.isEmpty())
            throw new RuntimeException("there are no shortages to fill!");

        Map<Integer, Map<Integer, Integer>> stockCopy = new HashMap<>(stockLeftoversId);
        Map<Integer, Map<Integer, Integer>> supplyCopy = new HashMap<>(supplyLeftoversId);

        clearStockLeftovers();
        clearSupplyLeftovers();

        Map<ShipmentArea, Integer> output =  createShipments(stockCopy, supplyCopy);
        return output;
    }

    private void clearStockLeftovers() {
        Repository.getInstance().truncateShipmentStockLeftovers();
        stockLeftoversId.clear();
    }

    private void clearSupplyLeftovers() {
        Repository.getInstance().truncateShipmentSupplyLeftovers();
        supplyLeftoversId.clear();
    }

    public Map<Integer, Shipment> getOrders() {
        return shipments;
    }

    /**
     * return true if the shipment is over weighted
     * @param shipmentId - shipment id
     * @return boolean
     */
    public boolean isOverWeighted(int shipmentId) {
        return shipments.get(shipmentId).isOverWeight();
    }

    public LocalTime getExpectedTime(int shipmentId, int storeId) {
        Shipment shipment = shipments.get(shipmentId);
        return shipment.getExpectedTime(destinationController.getStore(storeId));
    }

    public void delayDistribution(int shipmentId, int storeId, int delayTime) {
        Shipment shipment = shipments.get(shipmentId);
        shipment.delayDistribution(storeId, delayTime);
    }

    private LocalDate getClosestSunday() {
        LocalDate now = LocalDate.now();
        LocalDate nextSunday = now.with(DayOfWeek.SUNDAY);
        if (now.isAfter(nextSunday)) {
            nextSunday = nextSunday.plusWeeks(1);
        }
        return nextSunday;
    }

    public void load() {
        try {
            if (Repository.getInstance().getShipmentFactoryDAO().selectAll().stream().map(dto -> (ShipmentFactoryDTO) dto).count() == 0) {
                Repository.getInstance().getShipmentFactoryDAO().setDocumentIdGenerator();
                documentIdGenerator = 0;
            }
            else
                documentIdGenerator = Repository.getInstance().getShipmentFactoryDAO().getDocumentIdGenerator();
            Repository.getInstance().getShipmentDAO().selectAll().stream().map(dto -> (ShipmentDTO) dto)
                    .forEach(shipmentDTO -> {
                        List<Store> stores = Repository.getInstance().getStoreIdsOfShipment(shipmentDTO.getId()).stream().map(id -> destinationController.getStore(id)).toList();

                        List<Provider> providers = Repository.getInstance().getProviderIdsOfShipment(shipmentDTO.getId()).stream().map(id -> destinationController.getProvider(id)).toList();

                        List<DocumentedFileDTO> documentedFileDTOs = Repository.getInstance().getDocumentedFilesOfShipment(shipmentDTO.getId());
                        Map<Integer, List<FileItemDTO>> fileItemDTOs = new HashMap<>();
                        documentedFileDTOs.forEach(documentedFileDTO -> {
                            List<FileItemDTO> fileItemDTOList = Repository.getInstance().getFileItemsOfDocumentedFile(documentedFileDTO.getDocumentId());
                            fileItemDTOs.put(documentedFileDTO.getDocumentId(), fileItemDTOList);
                        });
                        List<DocumentedFile> documentedFiles = new ArrayList<>();
                        documentedFileDTOs
                                .forEach(documentedFileDTO -> {
                                    Map<Item, Integer> products = new HashMap<>();
                                    fileItemDTOs.get(documentedFileDTO.getDocumentId()).forEach(fileItemDTO -> products.put(itemController.getItem(fileItemDTO.getItemId()), fileItemDTO.getAmount()));
                                    documentedFiles.add(new DocumentedFile(documentedFileDTO, destinationController.getStore(documentedFileDTO.getStoreId()), products));
                                });

                        Map<Store, Map<Item, Integer>> itemsToDeliver = createStockOrder(Repository.getInstance().getItemsToDeliver(shipmentDTO.getId()));
                        Map<Provider, Map<Item, Integer>> itemsToReceive = createSupplyOrder(Repository.getInstance().getItemsToReceive(shipmentDTO.getId()));
                        shipments.put(shipmentDTO.getId(), Shipment.loadShipment(shipmentDTO, destinationController.getProvider(shipmentDTO.getSourceId()), stores, providers, documentedFiles, itemsToDeliver, itemsToReceive));
                    });
        }
        catch (Exception e) {
            throw new RuntimeException("failed to load shipments");
        }
    }


    public Map<ShipmentArea, Integer> createMockShipments(Map<Integer, Map<Integer, Integer>> stockOrderId,
                                                      Map<Integer, Map<Integer, Integer>> supplyOrderId) {
        Map<Store, Map<Item, Integer>> stockOrder = createStockOrder(stockOrderId);

        Map<Provider, Map<Item, Integer>> supplyOrder = createSupplyOrder(supplyOrderId);

        Map<ShipmentArea, Map<Store, Map<Item, Integer>>> stockOrderByArea = createStockOrderByArea(stockOrder);

        Map<ShipmentArea, Map<Provider, Map<Item, Integer>>> supplyOrderByArea = createSupplyOrderByArea(stockOrderByArea, supplyOrder);

        Map<ShipmentArea, Truck> trucksByArea = new HashMap<>();
        Map<ShipmentArea, Driver> driversByArea = new HashMap<>();
        // get a truck and a driver for each area
        for (ShipmentArea area : supplyOrderByArea.keySet()) {
            try {
                TruckDriverPair pair = truckDriverController.getMockAvailableTruckDriverPair(convertToDateViaInstant(getClosestSunday()));
                trucksByArea.put(area, pair.getTruck());
                driversByArea.put(area, pair.getDriver());
            }
            catch(Exception ex) { concatToStockShortage(stockOrderByArea.get(area)); concatToSupplyShortage(supplyOrderByArea.get(area)); }
        } //need to fix- what if no drivers are available?

        Map<ShipmentArea, Integer> shipmentIds = new HashMap<>();

        // create shipments for each area
        for (ShipmentArea area : trucksByArea.keySet()) {
            List<Provider> providers = supplyOrderByArea.get(area).keySet().stream().toList();

            int shipmentId = shipments.size();
            List<DocumentedFile> files = createDocumentedFilesForArea(stockOrderByArea, area, shipmentId);

            LocalDate nextSunday = getClosestSunday();
            //create for sunday:
            Shipment shipment = sundayShipment(shipmentId, nextSunday, trucksByArea.get(area).getLicenseNumber(),
                    driversByArea.get(area).getName(),providers.get(0), providers,
                    stockOrderByArea.get(area).keySet().stream().toList(),
                    files, stockOrderByArea.get(area), supplyOrderByArea.get(area), driversByArea.get(area).getId());

            shipments.put(shipmentId, shipment);
            shipmentIds.put(area, shipmentId);
        }

        return shipmentIds;
    }
}
