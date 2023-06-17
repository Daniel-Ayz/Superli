package delivery.backend.tests;

import delivery.backend.businessLayer.TruckDriver.TruckDriverController;
import delivery.backend.businessLayer.TruckDriver.TruckDriverPair;
import delivery.backend.businessLayer.destination.Destination;
import delivery.backend.businessLayer.destination.DestinationController;
import delivery.backend.businessLayer.destination.Provider;
import delivery.backend.businessLayer.destination.Store;
import delivery.backend.businessLayer.item.Item;
import delivery.backend.businessLayer.item.ItemController;
import delivery.backend.businessLayer.shipment.Shipment;
import delivery.backend.businessLayer.shipment.ShipmentFactory;
import delivery.backend.businessLayer.TruckDriver.Truck;
import delivery.backend.businessLayer.TruckDriver.TruckController;
import delivery.backend.dal.Repository;
import org.junit.jupiter.api.*;

import java.lang.reflect.Field;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DeliveryBackendTests {
    private ShipmentFactory shipmentFactory;
    private DestinationController destinationController;
    private TruckController truckController;
    private ItemController itemController;
    private TruckDriverController truckDriverController;


    @BeforeEach
    public void init() {
        destinationController = new DestinationController();
        itemController = new ItemController();
        truckController = new TruckController();
        shipmentFactory = new ShipmentFactory(truckController, destinationController, itemController);
        truckDriverController = new TruckDriverController(truckController);
    }

    @AfterEach
    public void truncateAll() {
        Repository.getInstance().truncateAll();
    }

    @Test // 1
    public void destinationTest() {
        Assertions.assertEquals(getDestinations().size(), 1);
    }

    @Test // 2
    public void addStoreTest() {
        destinationController.addDestination("1", "NORTH", "1", "Ishay Botzim", 16, 9, false);
        Assertions.assertEquals(getDestinations().size(), 2);

        destinationController.addDestination("2", "SOUTH", "2", "Hana Tzirlin", 45, -19,  false);
        Assertions.assertEquals(getDestinations().size(), 3);

        for (Destination destination : getDestinations()) {
            Assertions.assertTrue(destination instanceof Store);
        }
    }

    private void addStores() {
        destinationController.addDestination("1", "NORTH", "1", "Ishay Botzim", 16, 9, false);
        destinationController.addDestination("2", "SOUTH", "2", "Hana Tzirlin", 45, -19,  false);
    }

    @Test // 3
    public void addProviderTest() {
        destinationController.addDestination("1", "NORTH", "1", "Golani", 77, 0, true);
        Assertions.assertEquals(getDestinations().size(), 2);

        destinationController.addDestination("2", "NORTH", "2", "Heil Hamaim",-10, 9,  true);
        Assertions.assertEquals(getDestinations().size(), 3);

        for (Destination destination : getDestinations()) {
            Assertions.assertTrue(destination instanceof Provider);
        }
    }

    private List<Destination> getDestinations() {
        Class<?> destinationControllerClass = destinationController.getClass();
        Field[] fields = destinationControllerClass.getDeclaredFields();
        List<Destination> destinations = null;

        for (Field field : fields) {
            if (field.getName().equals("destinations")) {
                field.setAccessible(true);
                try {
                    destinations = (List<Destination>) field.get(destinationController);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return destinations;
    }

    @Test // 4
    public void addTruckTest() {
        truckController.addTruck("1", "1", 100, 200, "REGULAR_LIGHT");
        Assertions.assertEquals(getTrucks().size(), 1);

        truckController.addTruck("2", "2", 50, 250, "REGULAR_HEAVY");
        Assertions.assertEquals(getTrucks().size(), 2);
    }

    private void addTrucks() {
        truckController.addTruck("1", "1", 100, 200, "REGULAR_LIGHT");
        truckController.addTruck("2", "2", 50, 250, "REGULAR_HEAVY");
    }

    private Map<Truck, Boolean> getTrucks() {
        Class<?> truckControllerClass = truckController.getClass();
        Field[] fields = truckControllerClass.getDeclaredFields();
        Map<Truck, Boolean> trucks = null;

        for (Field field : fields) {
            if (field.getName().equals("availableTrucks")) {
                field.setAccessible(true);
                try {
                    trucks = (Map<Truck, Boolean>) field.get(truckController);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return trucks;
    }

    @Test // 5
    public void addItemTest() {
        itemController.addItem("1");
        Assertions.assertEquals(getItems().size(), 1);

        itemController.addItem("2");
        Assertions.assertEquals(getItems().size(), 2);
    }

    private void addItems() {
        itemController.addItem("1");
        itemController.addItem("2");
    }

    private Map<Integer, Item> getItems() {
        Class<?> itemControllerClass = itemController.getClass();
        Field[] fields = itemControllerClass.getDeclaredFields();
        Map<Integer, Item> items = null;

        for (Field field : fields) {
            if (field.getName().equals("items")) {
                field.setAccessible(true);
                try {
                    items = (Map<Integer, Item>) field.get(itemController);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return items;
    }

    @Test // 6
    public void getTruckDriverPairTest() {
        addTruckTest();

        TruckDriverPair truckDriverPair =  truckDriverController.getMockAvailableTruckDriverPair(convertToDateViaInstant(getClosestSunday()));
        Assertions.assertNotNull(truckDriverPair);
        Assertions.assertTrue(truckDriverPair.getDriver().getLicenseType()
                .compareTo(truckDriverPair.getTruck().getLicenseType()) >= 0);
    }

    @Test // 7
    public void getTruckDriverPairFailTest() {
        Assertions.assertThrows(Exception.class, () -> truckDriverController.getMockAvailableTruckDriverPair(convertToDateViaInstant(getClosestMonday())));
    }

    private Date convertToDateViaInstant(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    private LocalDate getClosestSunday() {
        LocalDate now = LocalDate.now();
        LocalDate nextSunday = now.with(DayOfWeek.SUNDAY);
        if (now.isAfter(nextSunday)) {
            nextSunday = nextSunday.plusWeeks(1);
        }
        return nextSunday;
    }

    private LocalDate getClosestMonday() {
        LocalDate now = LocalDate.now();
        LocalDate nextMonday = now.with(DayOfWeek.MONDAY);
        if (now.isAfter(nextMonday)) {
            nextMonday = nextMonday.plusWeeks(1);
        }
        return nextMonday;
    }

    /*@Test // 8
    public void createShipmentsTest() {
        addTruckTest();
        addStoreTest();
        destinationController.addDestination("1", "NORTH", "1", "Golani", 90, 11, true);
        destinationController.addDestination("2", "NORTH", "2", "Heil Hamaim", -333, -90, true);
        addItemTest();

        Map<Integer, Map<Integer, Integer>> stockOrder = Map.of(
                1, Map.of(0, 3,
                        1, 5),
                2, Map.of(0, 7,
                        1, 4)
        );
        Map<Integer, Map<Integer, Integer>> supplyOrder = Map.of(
                3, Map.of(1, 6,
                        0, 8),
                4, Map.of(1, 3,
                        0, 2)
        );

        shipmentFactory.createShipments(stockOrder, supplyOrder);

        Assertions.assertEquals(getShipments().size(), 2);
    }*/

    @Test // 8
    public void createMockShipmentTest() {
        addTrucks();
        addStores();
        destinationController.addDestination("1", "NORTH", "1", "Golani", 90, 11, true);
        destinationController.addDestination("2", "NORTH", "2", "Heil Hamaim", -333, -90, true);
        addItems();

        Map<Integer, Map<Integer, Integer>> stockOrder = Map.of(
                1, Map.of(0, 3,
                        1, 5),
                2, Map.of(0, 7,
                        1, 4)
        );
        Map<Integer, Map<Integer, Integer>> supplyOrder = Map.of(
                3, Map.of(1, 6,
                        0, 8),
                4, Map.of(1, 3,
                        0, 2)
        );

        shipmentFactory.createMockShipments(stockOrder, supplyOrder);
    }

    private Map<Integer, Shipment> getShipments() {
        Class<?> shipmentFactoryClass = shipmentFactory.getClass();
        Field[] fields = shipmentFactoryClass.getDeclaredFields();
        Map<Integer, Shipment> shipments = null;

        for (Field field : fields) {
            if (field.getName().equals("shipments")) {
                field.setAccessible(true);
                try {
                    shipments = (Map<Integer, Shipment>) field.get(shipmentFactory);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return shipments;
    }

    @Test // 9
    public void setWeightTest() {
        createMockShipmentTest();

        shipmentFactory.setWeight(0, 3, 150);

        Shipment shipment = getShipments().get(0);
        int weight = 0;

        Class<?> shipmentClass = shipment.getClass();
        Field[] fields = shipmentClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals("weight")) {
                field.setAccessible(true);
                try {
                    weight = (int) field.get(shipment);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        Assertions.assertEquals(weight, 150);
    }

    @Test
    public void delayDistributionTest() {
        createMockShipmentTest();

        int delayTime = 10;


        Shipment shipment = getShipments().get(0);
        Map<Store, LocalTime> stores = null;

        Class<?> shipmentClass = shipment.getClass();
        Field[] fields = shipmentClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals("estimatedArrivalTimes")) {
                field.setAccessible(true);
                try {
                    stores = (Map<Store, LocalTime>) field.get(shipment);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        LocalTime storeTimeBefore = null;
        for (Map.Entry<Store, LocalTime> entry: stores.entrySet()) {
            if (entry.getKey().getId() == 1)
                storeTimeBefore = entry.getValue();
        }

        shipmentFactory.delayDistribution(0, 1, delayTime);

        LocalTime storeTimeAfter = null;
        for (Map.Entry<Store, LocalTime> entry: stores.entrySet()) {
            if (entry.getKey().getId() == 1)
                storeTimeAfter = entry.getValue();
        }

        Assertions.assertEquals(storeTimeBefore.plusMinutes(delayTime), storeTimeAfter);
    }
}
