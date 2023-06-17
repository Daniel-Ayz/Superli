package delivery.backend.serviceLayer;

import delivery.backend.businessLayer.destination.DestinationController;
import delivery.tools.Response;

public class DestinationService {
    private DestinationController destinationController;
    public DestinationService(DestinationController destinationController){
        this.destinationController = destinationController;
    }

    /**
     * @param address - the address of the destination
     * @param area - the area of the destination (ShipmentArea)
     * @param phone - the phone number of the destination
     * @param contactName - the contact name of the phone number
     * @param isProvider - provider or store
     * @return - a response object with a message and a status
     */
    public Response addDestination(String address, String area, String phone, String contactName, int northCoordinate, int eastCoordinate,boolean isProvider) {
        try {
            destinationController.addDestination(address, area, phone, contactName,northCoordinate, eastCoordinate, isProvider);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    /**
     * @return - a response object with a message, a status and a list of stores
     */
    public Response getStores() {
        try {
            return new Response(destinationController.getStores());
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    /**
     * @return - a response object with a message, a status and a list of providers
     */
    public Response getProviders() {
        try {
            return new Response(destinationController.getProviders());
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    /**
     * load information about all destinations from the database
     * @return - a response object with a message
     */
    public Response loadDestinations() {
        try {
            destinationController.loadStores();
            destinationController.loadProviders();
            return new Response();
        }
        catch(Exception e) { return new Response(e.getMessage()); }
    }

    public Response doesStoreExist(int id) {
        try {
            return new Response(destinationController.doesStoreExist(id));
        }
        catch(Exception ex) { return new Response(ex.getMessage()); }
    }
}
