package delivery.backend.serviceLayer;

import delivery.backend.businessLayer.destination.DestinationController;
import delivery.backend.businessLayer.item.ItemController;
import delivery.backend.businessLayer.shipment.ShipmentFactory;
import delivery.backend.businessLayer.TruckDriver.TruckController;
import delivery.backend.dal.Repository;

import java.util.Map;

public class FactoryService {
    private static FactoryService instance;
    private DestinationService destinationService;
    private ItemService itemService;
    private TruckService truckService;
    private DeliveryHandlerService deliveryHandlerService;


    private DestinationController destinationController;
    private ItemController itemController;
    private TruckController truckController;
    private ShipmentFactory shipmentFactory;

    private FactoryService() {
        this.destinationController = new DestinationController();
        this.itemController = new ItemController();
        this.truckController = new TruckController();
        this.shipmentFactory = new ShipmentFactory(truckController, destinationController, itemController);

        this.destinationService = new DestinationService(destinationController);
        this.itemService = new ItemService(itemController);
        this.truckService = new TruckService(truckController);
        this.deliveryHandlerService = new DeliveryHandlerService(shipmentFactory);

        destinationService.loadDestinations();
        itemService.loadItems();
        truckService.loadTrucks();
        deliveryHandlerService.loadShipmentFactory();
    }

    public static FactoryService getInstance()  {
        if (instance == null) {
            instance = new FactoryService();
        }
        return instance;
    }

    public void reset() {
        instance = new FactoryService();
    }

    /**
     * random initialization of the system
     * @return FactoryService
     */
    public FactoryService randomInit() {
        truckService.addTruck("111-11-111", "Mercedes truck", 1000, 1200, "REGULAR_LIGHT");
        truckService.addTruck("222-22-222", "Ford truck", 1200, 1500, "REGULAR_HEAVY");
        truckService.addTruck("333-33-333", "Toyota truck", 2000, 2400, "COLD_LIGHT");
        truckService.addTruck("444-44-444", "Dodge truck", 2500, 3000, "COLD_HEAVY");
        truckService.addTruck("555-55-555", "Ferrari truck", 2700, 3200, "COLD_HEAVY");
        truckService.addTruck("666-66-666", "Miri truck", 3000, 3500, "COLD_HEAVY");

        itemService.addItem("chocolate");
        itemService.addItem("milk");
        itemService.addItem("bread");
        itemService.addItem("eggs");
        itemService.addItem("cheese");
        itemService.addItem("meat");
        itemService.addItem("fish");
        itemService.addItem("vegetables");
        itemService.addItem("Shibutzim");

        destinationService.addDestination("Heil Hashpritzim 13", "NORTH", "052-1111111", "Ishay Botzim", 11, -22,  false);
        destinationService.addDestination("Psagot 78", "NORTH", "052-2222222", "Hana Tzirlin", 33, 55, false);
        destinationService.addDestination("Ben Gurion 32", "CENTER", "052-3333333", "Michael Adar", 0, -90, false);
        destinationService.addDestination("Miri 21", "SOUTH", "052-4444444", "Danel Boikis", -18, -86, false);
        destinationService.addDestination("Rager 148", "NORTH", "052-5555555", "Itai Pemper", 44, 62, false);

        destinationService.addDestination("Nituz 33", "SOUTH", "052-6666666", "Heil Hamaim", 21, 9,  true);
        destinationService.addDestination("Ze-Ahla 5", "SOUTH", "052-7777777", "Golani", 37, 71, true);
        destinationService.addDestination("Kurs 45", "SOUTH", "052-8888888", "Guy Shani", 15, -12, true);

        Map<Integer, Map<Integer, Integer>> stockOrder = Map.of(1, Map.of(0, 3,
                                                                            3,20,
                                                                            1,5),
                                                                2, Map.of(0, 7,
                                                                        7,10,
                                                                        1,4));

        Map<Integer, Map<Integer, Integer>> supplyOrder = Map.of(6, Map.of(3, 20,
                                                                            1,7),
                                                                7, Map.of(0, 10,
                                                                        7,9,
                                                                        1,1),
                                                                8, Map.of(7, 1,
                                                                            1,1));

        deliveryHandlerService.handleOrder(stockOrder, supplyOrder);


        return this;
    }

    public DeliveryHandlerService getDeliveryHandlerService() {
        return deliveryHandlerService;
    }

    public DestinationService getDestinationService() {
        return destinationService;
    }

    public ItemService getItemService() {
        return itemService;
    }

    public TruckService getTruckService() {
        return truckService;
    }

    public static void initDelivery() {
        //Trucks:
        FactoryService.getInstance().getTruckService().addTruck("111-11-111", "Mercedes truck", 1000, 1200, "REGULAR_LIGHT");
        FactoryService.getInstance().getTruckService().addTruck("222-22-222", "Ford truck", 1200, 1500, "REGULAR_HEAVY");
        FactoryService.getInstance().getTruckService().addTruck("333-33-333", "Toyota truck", 2000, 2400, "COLD_LIGHT");
        FactoryService.getInstance().getTruckService().addTruck("444-44-444", "Dodge truck", 2500, 3000, "COLD_HEAVY");
        FactoryService.getInstance().getTruckService().addTruck("555-55-555", "Ferrari truck", 2700, 3200, "COLD_HEAVY");
        FactoryService.getInstance().getTruckService().addTruck("666-66-666", "Miri truck", 3000, 3500, "COLD_HEAVY");

        //Items:
        FactoryService.getInstance().getItemService().addItem("chocolate");
        FactoryService.getInstance().getItemService().addItem("milk");
        FactoryService.getInstance().getItemService().addItem("bread");
        FactoryService.getInstance().getItemService().addItem("eggs");
        FactoryService.getInstance().getItemService().addItem("cheese");
        FactoryService.getInstance().getItemService().addItem("meat");
        FactoryService.getInstance().getItemService().addItem("fish");
        FactoryService.getInstance().getItemService().addItem("vegetables");
        FactoryService.getInstance().getItemService().addItem("Shibutzim");

        //Providers:
        FactoryService.getInstance().getDestinationService().addDestination("Nituz 33", "SOUTH", "052-6666666", "Heil Hamaim", -15, 9,  true);
        FactoryService.getInstance().getDestinationService().addDestination("Ze-Ahla 5", "SOUTH", "052-7777777", "Golani", -13, 14, true);
        FactoryService.getInstance().getDestinationService().addDestination("Kurs 45", "SOUTH", "052-8888888", "Guy Shani", -19, -7, true);
    }

    public static void clearDeliveryDB() {
        Repository.getInstance().truncateAll();
        FactoryService.getInstance().reset();
    }
}
