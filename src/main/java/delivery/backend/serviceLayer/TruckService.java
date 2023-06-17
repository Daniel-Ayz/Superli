package delivery.backend.serviceLayer;

import delivery.tools.Response;
import delivery.backend.businessLayer.TruckDriver.TruckController;

public class TruckService {

    private TruckController truckController;

    public TruckService(TruckController truckController){
        this.truckController = truckController;
    }

    /**
     * @param licenseNumber - truck license number
     * @param model - truck model
     * @param weight - truck weight
     * @param maxWeight - truck max weight
     * @param licenseType - truck license type (LicenseType)
     * @return - a response object with a message and a status
     */
    public Response addTruck(String licenseNumber, String model, int weight, int maxWeight, String licenseType){
        try {
            truckController.addTruck(licenseNumber, model, weight, maxWeight, licenseType);
            return new Response();
        }
        catch(Exception e) { return new Response(e.getMessage()); }
    }

    /**
     * @return - a response object with a message, a status and a list of available trucks
     */
    public Response getAvailableTrucks(){
        try {
            return new Response(truckController.getAvailableTrucks());
        }
        catch(Exception e) { return new Response(e.getMessage()); }
    }

    /**
     * load information about all trucks from the database
     * @return - a response object with a message
     */
    public Response loadTrucks() {
        try {
            truckController.loadTrucks();
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

}
