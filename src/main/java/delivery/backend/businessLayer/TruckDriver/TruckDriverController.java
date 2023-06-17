package delivery.backend.businessLayer.TruckDriver;

import HumanResources.BusinessLayer.ShiftModule.ShiftType;
import HumanResources.ServiceLayer.Response;
import HumanResources.ServiceLayer.ServiceFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TruckDriverController {
    private TruckController truckController;

    public TruckDriverController(TruckController truckController) {
        this.truckController = truckController;
    }

    /**
     * returns available truck-driver pair and deactivates the truck
     * @return - TruckDriverPair
     */

    public TruckDriverPair getAvailableTruckDriverPair(Date date) {
        List<Truck> trucks = truckController.getAvailableTrucks();
        for(Truck truck : trucks) {
            List<HumanResources.BusinessLayer.EmployeeModule.Driver> drivers = getDriversFromResponse(ServiceFactory.getInstance().getShiftServiceManagement().getDrivers(truck.getLicenseType(), date, ShiftType.MORNING));
            if(!drivers.isEmpty()) {
                truckController.activateTruck(truck.getLicenseNumber());
                HumanResources.BusinessLayer.EmployeeModule.Driver driver = drivers.get(0);
                Driver ourDriver = Driver.toShipmentDriver(driver);
                // mark driver as occupied
                ServiceFactory.getInstance().getShiftServiceManagement().approveDriver(driver, date);
                return new TruckDriverPair(truck, ourDriver);
            }
        }
        throw new RuntimeException("No available Truck-Driver pairs!");
    }

    private List<HumanResources.BusinessLayer.EmployeeModule.Driver> getDriversFromResponse(Response drivers) {
        if(!drivers.isSuccess())
            throw new RuntimeException((String) drivers.getData());

        Object obj = drivers.getData();
        List<HumanResources.BusinessLayer.EmployeeModule.Driver> driverList = new ArrayList<>();
        if (obj instanceof List) {
            List<?> objectList = (List<?>) obj;
            for (Object item : objectList) {
                if (item instanceof HumanResources.BusinessLayer.EmployeeModule.Driver) {
                    driverList.add((HumanResources.BusinessLayer.EmployeeModule.Driver) item);
                } else {
                    throw new RuntimeException("asked for Drivers From Employees, got: " + item.getClass());
                }
            }
        } else {
            throw new RuntimeException("asked for a List of Drivers From Employees, got: " + obj.getClass());
        }

        return driverList;
    }


    /**
     * returns available truck-driver pair that can carry more weight and deactivates an old truck
     * @param truckLicenseNumber - the license number of the old truck
     * @return - TruckDriverPair
     */
    public TruckDriverPair switchTruck(String truckLicenseNumber, Date date) {
        List<Truck> betterTrucks = truckController.getBetterTrucks(truckLicenseNumber);

        if (betterTrucks.isEmpty())
            throw new IllegalStateException("No better truck available");

        for (Truck truck : betterTrucks) {
            List<HumanResources.BusinessLayer.EmployeeModule.Driver> drivers = getDriversFromResponse(ServiceFactory.getInstance().getShiftServiceManagement().getDrivers(truck.getLicenseType(), date, ShiftType.MORNING));
            if (!drivers.isEmpty()) {
                switchTruck(truckLicenseNumber, truck.getLicenseNumber());
                HumanResources.BusinessLayer.EmployeeModule.Driver driver = drivers.get(0);
                Driver ourDriver = Driver.toShipmentDriver(driver);
                //mark driver as occupied
                ServiceFactory.getInstance().getShiftServiceManagement().approveDriver(driver, date);
                return new TruckDriverPair(truck, ourDriver);
            }
        }
        throw new IllegalStateException("No better truck-driver pair available");
    }

    /**
     * deactivates an old truck and activates a new truck
     * @param oldTruckNumber - the license number of the old truck
     * @param newTruckNumber - the license number of the new truck
     */
    private void switchTruck(String oldTruckNumber, String newTruckNumber) {
        truckController.deactivateTruck(oldTruckNumber);
        truckController.activateTruck(newTruckNumber);
    }

    /**
     * returns truck by license number
     * @param truckLicenseNumber - the license number of the truck
     * @return - Truck
     */
    public Truck getTruck(String truckLicenseNumber) {
        return truckController.getTruckByLicenseNumber(truckLicenseNumber);
    }

    /**
     * deactivates a truck
     * @param truckLicenseNumber - the license number of the truck
     */
    public void deactivateTruck(String truckLicenseNumber) {
        truckController.deactivateTruck(truckLicenseNumber);
    }

    public TruckDriverPair getMockAvailableTruckDriverPair(Date convertToDateViaInstant) {
        List<Truck> trucks = truckController.getAvailableTrucks();
        Truck truck = trucks.get(0);
        Driver driver = new Driver("1234567", "michael", "COLD_HEAVY");

        return new TruckDriverPair(truck, driver);
    }
}
