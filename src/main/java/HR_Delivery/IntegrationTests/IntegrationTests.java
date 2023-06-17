package HR_Delivery.IntegrationTests;

import HR_Delivery.HrDeliveryService;
import HR_Delivery.LicenseType;
import HumanResources.BusinessLayer.BranchModule.Branch;
import HumanResources.BusinessLayer.EmployeeModule.BankInformation;
import HumanResources.BusinessLayer.EmployeeModule.EmploymentContract;
import HumanResources.BusinessLayer.EmployeeModule.Role;
import HumanResources.BusinessLayer.ShiftModule.Shift;
import HumanResources.BusinessLayer.ShiftModule.ShiftType;
import HumanResources.DataAcessLayer.BranchDAL.BranchDAO;
import HumanResources.DataAcessLayer.EmployeeDAL.*;
import HumanResources.DataAcessLayer.ShiftDAL.*;
import HumanResources.ServiceLayer.Response;
import HumanResources.ServiceLayer.ServiceFactory;
import delivery.backend.businessLayer.TruckDriver.Truck;
import delivery.backend.businessLayer.TruckDriver.TruckController;
import delivery.backend.businessLayer.TruckDriver.TruckDriverController;
import delivery.backend.businessLayer.TruckDriver.TruckDriverPair;
import delivery.backend.businessLayer.destination.Destination;
import delivery.backend.businessLayer.destination.DestinationController;
import delivery.backend.businessLayer.destination.ShipmentArea;
import delivery.backend.businessLayer.destination.Store;
import delivery.backend.businessLayer.item.Item;
import delivery.backend.businessLayer.item.ItemController;
import delivery.backend.businessLayer.shipment.Shipment;
import delivery.backend.businessLayer.shipment.ShipmentFactory;

import delivery.backend.dal.Repository;
import delivery.backend.serviceLayer.FactoryService;
import delivery.frontend.Main;
import org.junit.jupiter.api.*;

import java.lang.reflect.Field;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import delivery.backend.tests.DeliveryBackendTests.*;

public class IntegrationTests {

    private TruckDriverController truckDriverController;

    @AfterEach
    @BeforeEach
    public void init() {
        clearDB();
        ServiceFactory.getInstance().reset();
        FactoryService.getInstance().reset();

        truckDriverController = new TruckDriverController(getTruckController());
    }

    void clearDB(){
        new BranchDAO().deleteAll();
        new EmployeeDAO().deleteAll();
        new ShiftIdDAO().deleteAll();
        Repository.getInstance().truncateAll();
    }

    private DestinationController getDestinationController() {
        Class<?> deliveryServiceClass = FactoryService.getInstance().getClass();
        Field[] fields = deliveryServiceClass.getDeclaredFields();
        DestinationController destinationController = null;

        for (Field field : fields) {
            if (field.getName().equals("destinationController")) {
                field.setAccessible(true);
                try {
                    destinationController = (DestinationController) field.get(FactoryService.getInstance());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return destinationController;
    }

    private ItemController getItemController() {
        Class<?> deliveryServiceClass = FactoryService.getInstance().getClass();
        Field[] fields = deliveryServiceClass.getDeclaredFields();
        ItemController itemController = null;

        for (Field field : fields) {
            if (field.getName().equals("itemController")) {
                field.setAccessible(true);
                try {
                    itemController = (ItemController) field.get(FactoryService.getInstance());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return itemController;
    }

    private TruckController getTruckController() {
        Class<?> deliveryServiceClass = FactoryService.getInstance().getClass();
        Field[] fields = deliveryServiceClass.getDeclaredFields();
        TruckController truckController = null;

        for (Field field : fields) {
            if (field.getName().equals("truckController")) {
                field.setAccessible(true);
                try {
                    truckController = (TruckController) field.get(FactoryService.getInstance());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return truckController;
    }

    private ShipmentFactory getShipmentFactory() {
        Class<?> deliveryServiceClass = FactoryService.getInstance().getClass();
        Field[] fields = deliveryServiceClass.getDeclaredFields();
        ShipmentFactory shipmentFactory = null;

        for (Field field : fields) {
            if (field.getName().equals("shipmentFactory")) {
                field.setAccessible(true);
                try {
                    shipmentFactory = (ShipmentFactory) field.get(FactoryService.getInstance());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return shipmentFactory;
    }

    @Test // 1 integration test
    public void createShipmentsTest() {
        Map<ShipmentArea, Integer> out = createShipments();
        Assertions.assertTrue(out.size() == 2);
    }

    private Map<ShipmentArea, Integer> createShipments() {
        addTrucks();
        addStores();
        getDestinationController().addDestination("1", "NORTH", "1", "Golani", 90, 11, true);
        getDestinationController().addDestination("2", "NORTH", "2", "Heil Hamaim", -333, -90, true);
        addItems();
        addDrivers();

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

        Map<ShipmentArea, Integer> out = getShipmentFactory().createShipments(stockOrder, supplyOrder);
        return out;
    }

    private void addItems() {
        getItemController().addItem("1");

        getItemController().addItem("2");
    }

    private void addStores()
{
        getDestinationController().addDestination("1", "NORTH", "1", "Ishay Botzim", 16, 9, false);

        getDestinationController().addDestination("2", "SOUTH", "2", "Hana Tzirlin", 45, -19,  false);
    }

    private void addTrucks() {
        getTruckController().addTruck("1", "1", 100, 200, "REGULAR_LIGHT");

        getTruckController().addTruck("2", "2", 50, 250, "REGULAR_HEAVY");
    }

    @Test // 2 integration test
    public void getTruckDriverPairFailTest() {
        addTrucks();
        Assertions.assertThrows(Exception.class, () -> truckDriverController.getAvailableTruckDriverPair(convertToDateViaInstant(getClosestMonday())));
    }

    @Test // 3 integration test
    public void getTruckDriverPairTest() {
        addTrucks();
        addDrivers();

        TruckDriverPair truckDriverPair =  truckDriverController.getAvailableTruckDriverPair(convertToDateViaInstant(getClosestSunday()));
        Assertions.assertNotNull(truckDriverPair);
        Assertions.assertTrue(truckDriverPair.getDriver().getLicenseType()
                .compareTo(truckDriverPair.getTruck().getLicenseType()) >= 0);
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

    @Test // 4 integration test
    public void toShipmentDriverTest() {
        HumanResources.BusinessLayer.EmployeeModule.Driver hrDriver = new HumanResources.BusinessLayer.EmployeeModule.Driver("654", "654", "hr driver", null, null, null, null, 10000, LicenseType.COLD_HEAVY);

        delivery.backend.businessLayer.TruckDriver.Driver deliveryDriver = delivery.backend.businessLayer.TruckDriver.Driver.toShipmentDriver(hrDriver);

        Assertions.assertEquals(deliveryDriver.getId(), hrDriver.getId());
        Assertions.assertEquals(deliveryDriver.getName(), hrDriver.getName());
        Assertions.assertEquals(deliveryDriver.getLicenseType(), hrDriver.getLicenseType());
    }

    @Test // 5
//    public void getExpectedTimeTest() {
//        createShipments();
//        Assertions.assertDoesNotThrow(() -> getShipmentFactory().getExpectedTime(0, 1));
//    }

    private void addDrivers() {
        List<Role> roles = new ArrayList<>();
        String driverId = "1992";
        roles.add(Role.DRIVER);
        ServiceFactory.getInstance().getEmployeeService().addEmployee("Tomi", driverId, "1", "1", "1", new Date(), "1", roles,3000, LicenseType.COLD_HEAVY);
        ServiceFactory.getInstance().getEmployeeService().addEmployee("ronmi", "81", "8181", "8200", "81", new Date(), "18", roles,81000, LicenseType.COLD_HEAVY);
        ServiceFactory.getInstance().getShiftServiceManagement().addShift(convertToDateViaInstant(getClosestSunday()), ShiftType.MORNING, 0);
        List<Shift> shifts= (List<Shift>)ServiceFactory.getInstance().getShiftServiceManagement().getUpcomingShifts().getData();
        for(Shift shift: shifts){
            if(shift.getDate().equals(convertToDateViaInstant(getClosestSunday())) && shift.getShiftType().equals(ShiftType.MORNING) &&  shift.getBranchId()==0){
                ServiceFactory.getInstance().getShiftServiceManagement().addRequiredRoleToShift(shift.getShiftId(),Role.DRIVER,2);
                ServiceFactory.getInstance().getShiftServiceEmployee().requestShiftByEmployee(driverId,shift.getShiftId());
                ServiceFactory.getInstance().getShiftServiceEmployee().requestShiftByEmployee("81",shift.getShiftId());
            }
        }
    }

    @Test
    public void addBranch(){
        Map<Integer, Branch> branches= (Map<Integer, Branch>)ServiceFactory.getInstance().getBranchService().getAllBranches().getData();
        List<Store> stores = (List<Store>)FactoryService.getInstance().getDestinationService().getStores().getData();
        int branchSize = branches.size()+1;
        int storeSize = stores.size()+1;
        new HrDeliveryService().addBranch(11,"6:00","13:00","13:00","21:00","kfar","NORTH","1123","Roni",1,1);
        branches= (Map<Integer, Branch>)ServiceFactory.getInstance().getBranchService().getAllBranches().getData();
        stores = (List<Store>)FactoryService.getInstance().getDestinationService().getStores().getData();
        Assertions.assertEquals(branches.size(),branchSize);
        Assertions.assertEquals(stores.size(),storeSize);
    }

    //new HR test
    @Test
    void addRequiredRoleToShift(){
        Date date = new Date (2023,3,3);
        ServiceFactory.getInstance().getShiftServiceManagement().addShift(date,ShiftType.MORNING,0);
        int shiftId = 0;
        List<Shift> shifts= (List<Shift>)ServiceFactory.getInstance().getShiftServiceManagement().getUpcomingShifts().getData();
        for(Shift shift: shifts){
            if(shift.getDate().equals(convertToDateViaInstant(getClosestSunday())) && shift.getShiftType().equals(ShiftType.MORNING) &&  shift.getBranchId()==0){
                shiftId = shift.getShiftId();
            }
        }
        Response r = ServiceFactory.getInstance().getShiftServiceManagement().addRequiredRoleToShift(shiftId,Role.DRIVER,1);
        assert r.isSuccess();
    }
    @Test
    void removeRequiredRoleFromShift(){
        Date date = new Date (2023,3,3);
        ServiceFactory.getInstance().getShiftServiceManagement().addShift(date,ShiftType.MORNING,0);
        int shiftId = 0;
        List<Shift> shifts= (List<Shift>)ServiceFactory.getInstance().getShiftServiceManagement().getUpcomingShifts().getData();
        for(Shift shift: shifts){
            if(shift.getDate().equals(convertToDateViaInstant(getClosestSunday())) && shift.getShiftType().equals(ShiftType.MORNING) &&  shift.getBranchId()==0){
                shiftId = shift.getShiftId();
            }
        }
        Response r0 = ServiceFactory.getInstance().getShiftServiceManagement().addRequiredRoleToShift(shiftId,Role.DRIVER,1);
        assert r0.isSuccess();
        Response r1 = ServiceFactory.getInstance().getShiftServiceManagement().removeRequiredRoleFromShift(shiftId,Role.DRIVER);
        assert r1.isSuccess();
    }
    //fix
    @Test
    void addShiftOnSunday(){
        List<Shift> shifts = (List<Shift>)ServiceFactory.getInstance().getShiftServiceManagement().getUpcomingShifts().getData();
        int shiftsSize = shifts.size()+1;
        ServiceFactory.getInstance().getShiftServiceManagement().addShift(convertToDateViaInstant(getClosestSunday()), ShiftType.MORNING, 0);
        shifts = (List<Shift>)ServiceFactory.getInstance().getShiftServiceManagement().getUpcomingShifts().getData();
        Assertions.assertEquals(shifts.size(),shiftsSize);
    }

    @Test
    void requestShiftForNonexistentRole(){
        Date date = new Date (2024,3,3);
        ServiceFactory.getInstance().getShiftServiceManagement().addShift(date,ShiftType.MORNING,0);
        int shiftId = 0;
        List<Shift> shifts= (List<Shift>)ServiceFactory.getInstance().getShiftServiceManagement().getUpcomingShifts().getData();
        for(Shift shift: shifts){
            if(shift.getDate().equals(convertToDateViaInstant(getClosestSunday())) && shift.getShiftType().equals(ShiftType.MORNING) &&  shift.getBranchId()==0){
                shiftId = shift.getShiftId();
            }
        }
        Response r0 = ServiceFactory.getInstance().getShiftServiceManagement().addRequiredRoleToShift(shiftId,Role.DRIVER,1);
        assert r0.isSuccess();
        List<Role> roles = new ArrayList<>();
        roles.add(Role.CASHIER);
        ServiceFactory.getInstance().getEmployeeService().addEmployee("Tomi", "654", "1", "1", "1", new Date(), "1", roles,3000, LicenseType.COLD_HEAVY);
        Response r1 = ServiceFactory.getInstance().getShiftServiceEmployee().getAllShiftsAvailableFromDate(new Date(),Role.CASHIER.toString());
        assert !r1.isSuccess();
    }

}

