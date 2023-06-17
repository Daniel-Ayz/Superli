package delivery.backend.businessLayer.TruckDriver;

import delivery.backend.dal.Repository;
import delivery.backend.dal.dtos.TruckDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TruckController {

    private Map<Truck, Boolean> availableTrucks;

    public TruckController() {
        availableTrucks = new HashMap<>();
    }

    /**
     * Add a new truck to the system
     * @param licenseNumber - truck license number
     * @param model - truck model
     * @param weight - truck weight
     * @param maxWeight - truck max weight
     * @param licenseType - truck license type (LicenseType)
     */
    public void addTruck(String licenseNumber, String model, int weight, int maxWeight, String licenseType) {
        availableTrucks.put(Truck.createTruck(licenseNumber, model, weight, maxWeight, true, licenseType), true);
    }

    /**
     * Returns a list of all available trucks
     * @return List<Truck>
     */
    public List<Truck> getAvailableTrucks() {
        return availableTrucks.keySet().stream().filter(availableTrucks::get).toList();
    }

    /**
     * returns an available truck
     * @return Truck
     */
    public Truck getAvailableTruck() {
        Truck truck =  availableTrucks.keySet().stream().filter(availableTrucks::get).findFirst().orElse(null);
        availableTrucks.put(truck, false);

        return truck;
    }

    /**
     * Returns a truck by license number
     * @param truckLicenseNumber - truck license number
     * @return Truck
     */
    public Truck getTruckByLicenseNumber(String truckLicenseNumber) {
        return availableTrucks.keySet().stream().filter(truck -> truck.getLicenseNumber().equals(truckLicenseNumber)).findFirst().orElse(null);
    }

    /**
     * Returns a list of all available trucks that can carry more weight than the truck with the given license number
     * @param truckLicenseNumber - truck license number
     * @return List<Truck>
     */
    public List<Truck> getBetterTrucks(String truckLicenseNumber) {
        Truck toSwitch = getTruckByLicenseNumber(truckLicenseNumber);

        return getAvailableTrucks().stream()
                .filter(truck -> truck.getMaxWeight() - truck.getWeight() > toSwitch.getMaxWeight() - toSwitch.getWeight()).toList();
    }

    /**
     * deactivate a truck
     * @param oldTruck
     */
    public void deactivateTruck(String oldTruck) {
        Repository.getInstance().getTruckDAO().setAvailable(oldTruck, true);
        availableTrucks.put(getTruckByLicenseNumber(oldTruck), true);
    }

    /**
     * activate a truck
     * @param newTruck
     */
    public void activateTruck(String newTruck) {
        Repository.getInstance().getTruckDAO().setAvailable(newTruck, false);
        availableTrucks.put(getTruckByLicenseNumber(newTruck), false);
    }

    public void loadTrucks() {
        try {
            List<TruckDTO> truckDTOs = Repository.getInstance().getTruckDAO().selectAll().stream().map((dto) -> (TruckDTO) dto).toList();
            for (TruckDTO truckDTO : truckDTOs) {
                Truck truck = new Truck(truckDTO);
                availableTrucks.put(truck, truckDTO.isAvailable());
            }
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to load trucks");
        }

    }
}
