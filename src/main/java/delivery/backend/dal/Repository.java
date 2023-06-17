package delivery.backend.dal;

import delivery.backend.businessLayer.destination.Provider;
import delivery.backend.businessLayer.destination.Store;
import delivery.backend.businessLayer.item.Item;
import delivery.backend.dal.daos.ItemDAO;
import delivery.backend.dal.daos.TruckDAO;
import delivery.backend.dal.daos.destination.*;
import delivery.backend.dal.daos.file.*;
import delivery.backend.dal.daos.shipment.*;
import delivery.backend.dal.daos.shipmentFactory.*;
import delivery.backend.dal.dtos.file.DocumentedFileDTO;
import delivery.backend.dal.dtos.file.FileItemDTO;
import delivery.backend.dal.dtos.shipment.*;
import delivery.backend.dal.dtos.shipmentFactory.StockLeftoverDTO;
import delivery.backend.dal.dtos.shipmentFactory.SupplyLeftoverDTO;

import java.sql.*;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class Repository {
    private static Repository instance;
    private Connection conn;

    private ProviderDAO providerDAO;
    private StoreDAO storeDAO;
    private DocumentedFileDAO documentedFileDAO;
    private FileItemDAO fileItemDAO;
    private ShipmentDAO shipmentDAO;
    private ShipmentEstimationsDAO shipmentEstimationsDAO;
    private ShipmentProviderDAO shipmentProviderDAO;
    private ShipmentProviderItemDAO shipmentProviderItemDAO;
    private ShipmentStoreDAO shipmentStoreDAO;
    private ShipmentStoreItemDAO shipmentStoreItemDAO;
    private ShipmentFactoryDAO shipmentFactoryDAO;
    private StockLeftoverDAO shipmentStockLeftoversDAO;
    private SupplyLeftoverDAO shipmentSupplyLeftoversDAO;
    private ItemDAO itemDAO;
    private TruckDAO truckDAO;




    private Repository(Connection conn) {
        this.conn = conn;
        this.providerDAO = new ProviderDAO(conn);
        this.storeDAO = new StoreDAO(conn);
        this.documentedFileDAO = new DocumentedFileDAO(conn);
        this.fileItemDAO = new FileItemDAO(conn);
        this.shipmentDAO = new ShipmentDAO(conn);
        this.shipmentEstimationsDAO = new ShipmentEstimationsDAO(conn);
        this.shipmentProviderDAO = new ShipmentProviderDAO(conn);
        this.shipmentProviderItemDAO = new ShipmentProviderItemDAO(conn);
        this.shipmentStoreDAO = new ShipmentStoreDAO(conn);
        this.shipmentStoreItemDAO = new ShipmentStoreItemDAO(conn);
        this.shipmentFactoryDAO = new ShipmentFactoryDAO(conn);
        this.itemDAO = new ItemDAO(conn);
        this.truckDAO = new TruckDAO(conn);
        this.shipmentStockLeftoversDAO = new StockLeftoverDAO(conn);
        this.shipmentSupplyLeftoversDAO = new SupplyLeftoverDAO(conn);
    }

    public ShipmentEstimationsDAO getShipmentEstimationsDAO() {
        return shipmentEstimationsDAO;
    }

    public StockLeftoverDAO getShipmentStockLeftoversDAO() {
        return shipmentStockLeftoversDAO;
    }

    public SupplyLeftoverDAO getShipmentSupplyLeftoversDAO() {
        return shipmentSupplyLeftoversDAO;
    }

    public ShipmentProviderItemDAO getShipmentProviderItemDAO() {
        return shipmentProviderItemDAO;
    }

    private static Repository createRepository() throws SQLException {
        Connection conn = DataBase.connect();
        Repository repository = new Repository(conn);
        return repository;
    }

    public static Repository getInstance() {
        if (instance == null) {
            try {
                instance = createRepository();
                instance.createTables();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to connect to database");
            }
        }
        return instance;
    }

    public ProviderDAO getProviderDAO() {
        return providerDAO;
    }

    public StoreDAO getStoreDAO() {
        return storeDAO;
    }

    public DocumentedFileDAO getDocumentedFileDAO() {
        return documentedFileDAO;
    }

    public FileItemDAO getFileItemDAO() {
        return fileItemDAO;
    }

    public ShipmentDAO getShipmentDAO() {
        return shipmentDAO;
    }

    public ShipmentProviderDAO getShipmentProviderDAO() {
        return shipmentProviderDAO;
    }

    public ShipmentProviderItemDAO getSipmentProviderItemDAO() {
        return shipmentProviderItemDAO;
    }

    public ShipmentStoreDAO getShipmentStoreDAO() {
        return shipmentStoreDAO;
    }

    public ShipmentStoreItemDAO getShipmentStoreItemDAO() {
        return shipmentStoreItemDAO;
    }

    public ShipmentFactoryDAO getShipmentFactoryDAO() {
        return shipmentFactoryDAO;
    }

    public ItemDAO getItemDAO() {
        return itemDAO;
    }

    public TruckDAO getTruckDAO() {
        return truckDAO;
    }

    public void putDocumentedFileProducts(int documentId, Map<Item, Integer> products) {
        List<FileItemDTO> fileItemDTOs = products.entrySet().stream().map(entry -> new FileItemDTO(documentId, entry.getKey().getId(), entry.getValue())).toList();
        fileItemDTOs.forEach(fileItemDTO -> fileItemDAO.addFileItem(fileItemDTO));
    }

    public void updateItemsToDeliver(int shipmentId, Map<Store, Map<Item, Integer>> itemsToDeliver) {
        List<ShipmentStoreItemDTO> shipmentStoreItemDTOs = itemsToDeliver.entrySet().stream().flatMap(entry -> entry.getValue().entrySet().stream().map(entry2 -> new ShipmentStoreItemDTO(shipmentId, entry.getKey().getId(), entry2.getKey().getId(), entry2.getValue()))).toList();
        shipmentStoreItemDTOs.forEach(shipmentStoreItemDTO -> shipmentStoreItemDAO.addShipmentStoreItem(shipmentStoreItemDTO));
    }

    public void updateItemsToReceive(int shipmentId, Map<Provider, Map<Item, Integer>> itemsToReceive) {
        List<ShipmentProviderItemDTO> shipmentProviderItemDTOs = itemsToReceive.entrySet().stream().flatMap(entry -> entry.getValue().entrySet().stream().map(entry2 -> new ShipmentProviderItemDTO(shipmentId, entry.getKey().getId(), entry2.getKey().getId(), entry2.getValue()))).toList();
        shipmentProviderItemDTOs.forEach(shipmentProviderItemDTO -> shipmentProviderItemDAO.addShipmentProviderItem(shipmentProviderItemDTO));
    }

    public void setEstimatedArrivalTimes(int shipmentId, Map<Store, LocalTime> estimatedArrivalTimes) {
        List<ShipmentEstimationsDTO> shipmentEstimationsDTOS = estimatedArrivalTimes.entrySet().stream().map(entry -> new ShipmentEstimationsDTO(shipmentId, entry.getKey().getId(), entry.getValue())).toList();
        truncateShipmentEstimations(shipmentId);
        shipmentEstimationsDTOS.forEach(shipmentEstimationsDTO -> shipmentEstimationsDAO.addShipmentEstimations(shipmentEstimationsDTO));
    }

    public void updateProviders(int id, Map<Provider, Boolean> providers) {
        List<ShipmentProviderDTO> shipmentProviderDTOs = providers.entrySet().stream().map(entry -> new ShipmentProviderDTO(id, entry.getKey().getId(), entry.getValue())).toList();
        shipmentProviderDTOs.forEach(shipmentProviderDTO -> shipmentProviderDAO.addShipmentProvider(shipmentProviderDTO));
    }

    public void updateStores(int id, List<Store> stores) {
        List<ShipmentStoreDTO> shipmentStoreDTOs = stores.stream().map(store -> new ShipmentStoreDTO(id, store.getId())).toList();
        shipmentStoreDTOs.forEach(shipmentStoreDTO -> shipmentStoreDAO.addShipmentStore(shipmentStoreDTO));
    }

    public void truncateShipmentStores(int id) {
        shipmentStoreDAO.truncate(id);
    }

    public void truncateShipmentProvider(int id) {
        shipmentProviderDAO.truncate(id);
    }

    private void truncateShipmentEstimations(int id) {
        shipmentEstimationsDAO.truncate(id);
    }

    public void addStockShortage(int storeId, int itemId, int amount) {
        StockLeftoverDTO leftovers = new StockLeftoverDTO(storeId, itemId, amount);
        shipmentStockLeftoversDAO.addShortage(leftovers);
    }

    public void addSupplyShortage(int providerId, int itemId, int amount) {
        SupplyLeftoverDTO leftovers = new SupplyLeftoverDTO(providerId, itemId, amount);
        shipmentSupplyLeftoversDAO.addShortage(leftovers);
    }

    public void truncateShipmentStockLeftovers() {
        shipmentStockLeftoversDAO.truncate();
    }

    public void truncateShipmentSupplyLeftovers() {
        shipmentSupplyLeftoversDAO.truncate();
    }

    public void truncateItemsToDeliver(int shipmentId) {
        shipmentStoreItemDAO.truncate(shipmentId);
    }

    public List<Integer> getStoreIdsOfShipment(int id) {
        return shipmentStoreDAO.getStoreIdsOfShipment(id);
    }

    public List<Integer> getProviderIdsOfShipment(int id) {
        return shipmentProviderDAO.getProviderIdsOfShipment(id);
    }

    public Map<Integer, Map<Integer, Integer>> getItemsToDeliver(int id) {
        return shipmentStoreItemDAO.getByShipmentId(id).stream().collect(Collectors.groupingBy(ShipmentStoreItemDTO::getStoreId, Collectors.toMap(ShipmentStoreItemDTO::getItemId, ShipmentStoreItemDTO::getAmount)));
    }

    public Map<Integer, Map<Integer, Integer>> getItemsToReceive(int id) {
        return shipmentProviderItemDAO.getByShipmentId(id).stream().collect(Collectors.groupingBy(ShipmentProviderItemDTO::getProviderId, Collectors.toMap(ShipmentProviderItemDTO::getItemId, ShipmentProviderItemDTO::getAmount)));
    }

    public boolean getProviderPassedThrough(int shipmentId, int providerId) {
        return shipmentProviderDAO.getProviderPassedThrough(shipmentId, providerId);
    }

    public LocalTime getEstimatedArrivalTime(int shipmentId, int storeId) {
        return shipmentEstimationsDAO.getEstimatedArrivalTime(shipmentId, storeId);
    }

    public List<FileItemDTO> getFileItemsOfDocumentedFile(int documentId) {
        return fileItemDAO.getByDocumentId(documentId);
    }

    public List<DocumentedFileDTO> getDocumentedFilesOfShipment(int shipmentId) {
        return documentedFileDAO.getByShipmentId(shipmentId);
    }

    public void truncateAll() {
        this.providerDAO.truncate();
        this.storeDAO.truncate();
        this.documentedFileDAO.truncate();
        this.fileItemDAO.truncate();
        this.shipmentDAO.truncate();
        this.shipmentEstimationsDAO.truncate();
        this.shipmentProviderDAO.truncate();
        this.shipmentProviderItemDAO.truncate();
        this.shipmentStoreDAO.truncate();
        this.shipmentStoreItemDAO.truncate();
        this.shipmentFactoryDAO.truncate();
        this.itemDAO.truncate();
        this.truckDAO.truncate();
        this.shipmentStockLeftoversDAO.truncate();
        this.shipmentSupplyLeftoversDAO.truncate();
    }


    private void createTables() {
        this.providerDAO.createTable();
        this.storeDAO.createTable();
        this.documentedFileDAO.createTable();
        this.fileItemDAO.createTable();
        this.shipmentDAO.createTable();
        this.shipmentEstimationsDAO.createTable();
        this.shipmentProviderDAO.createTable();
        this.shipmentProviderItemDAO.createTable();
        this.shipmentStoreDAO.createTable();
        this.shipmentStoreItemDAO.createTable();
        this.shipmentFactoryDAO.createTable();
        this.itemDAO.createTable();
        this.truckDAO.createTable();
        this.shipmentStockLeftoversDAO.createTable();
        this.shipmentSupplyLeftoversDAO.createTable();
        System.out.println("Tables created");
    }
}
