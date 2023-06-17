package delivery.backend.serviceLayer;

import delivery.backend.businessLayer.shipment.ShipmentFactory;
import delivery.backend.businessLayer.shipment.Treatment;
import delivery.tools.Response;

import java.time.LocalTime;
import java.util.Map;

public class DeliveryHandlerService {

    private ShipmentFactory shipmentFactory;

    public DeliveryHandlerService(ShipmentFactory shipmentFactory){
        this.shipmentFactory = shipmentFactory;
    }

    /**
     * @param stockOrder - a map of stores to a map of items to the amount of items to order
     * @param supplyOrder - a map of providers to a map of items to the amount of items ordered from the provider
     * @return - a response object with a message and a status and a map of the shipment area to the shipment id
     */
    public Response handleOrder(Map<Integer, Map<Integer, Integer>> stockOrder, Map<Integer, Map<Integer, Integer>> supplyOrder) {
        try {
            return new Response(shipmentFactory.createShipments(stockOrder, supplyOrder));
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    /**
     * @param shipmentId - the id of the shipment
     * @param treatment - a treatment to apply to the order
     *                  1 - DROPPING_OR_SWITCHING_DESTINATION
     *                  2 - SWITCHING_TRUCK
     *                  3 - DROPPING_ITEMS
     * @param supplierId - the id of the supplier
     * @return - a response object with a message and a status
     */
    public Response handleError(int shipmentId, int treatment, int supplierId) {
        try {
            switch (treatment) {
                case 1:
                    shipmentFactory.applyTreatment(shipmentId, Treatment.DROPPING_OR_SWITCHING_DESTINATION, supplierId);
                    break;
                case 2:
                    shipmentFactory.applyTreatment(shipmentId, Treatment.SWITCHING_TRUCK, supplierId);
                    break;
                case 3:
                    shipmentFactory.applyTreatment(shipmentId, Treatment.DROPPING_ITEMS, supplierId);
                    break;
                default:
                    return new Response("Invalid treatment");
            }

            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    /**
     * set the weight of a truck after it was measured
     * @param shipmentId - the id of the shipment
     * @param weight - the weight of the truck
     * @return - a response object with a message and a status
     */
    public Response setWeight(int shipmentId, int supplierId, int weight) {
        try {
            shipmentFactory.setWeight(shipmentId, supplierId, weight);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    /**
     * get the shortage of items in the stores
     * @return - a response object with a message and a status and a map of the store id to a map of the item id to the
     * amount of items (shortage)
     */
    public Response viewStoreShortage() {
        try {
            return new Response(shipmentFactory.getStoreShortages());
        }
        catch(Exception ex) { return new Response(ex.getMessage()); }
    }

    /**
     * get the shortage of items in the providers
     * @return - a response object with a message and a status and a map of the provider id to a map of the item id to the
     * amount of items (shortage)
     */
    public Response viewProvidersLeftovers() {
        try {
            return new Response(shipmentFactory.getProvidersLeftovers());
        }
        catch(Exception ex) { return new Response(ex.getMessage()); }
    }

    /**
     * fill the shortages by creating new shipments
     * @return - a response object with a message and a status and a map of the shipment area to the shipment id (the new shipments)
     */
    public Response handleShortage() {
        try {
            return new Response(shipmentFactory.fillShortages());
        }
        catch(Exception ex) { return new Response(ex.getMessage()); }
    }

    /**
     * get the orders of the system
     * @return - a response object with a message and a status and a map of the shipment id to the shipment
     */
    public Response getOrders() {
        try {
            return new Response(shipmentFactory.getOrders());
        }
        catch(Exception ex) { return new Response(ex.getMessage()); }
    }

    /**
     * check if a shipment is over weighted
     * @param shipmentId - the id of the shipment
     * @return - a response object with a message and a status and a boolean value that indicates if the shipment is over weighted
     */
    public Response isOverWeighted(int shipmentId) {
        try {
            return new Response(shipmentFactory.isOverWeighted(shipmentId));
        }
        catch(Exception ex) { return new Response(ex.getMessage()); }
    }

    /**
     * get the expected time of a shipment to a store
     * @param shipmentId - the id of the shipment
     * @param storeId - the id of the store
     * @return - a response object with a message and a local time object that indicates the expected time
     */
    public Response getExpectedTime(int shipmentId, int storeId) {
        try {
            return new Response(shipmentFactory.getExpectedTime(shipmentId, storeId));
        }
        catch(Exception ex) {return new Response(ex.getMessage());}
    }

    /**
     * delay arrival of shipment to a store (and all stores after it)
     * @param shipmentId - the id of the shipment
     * @param storeId - the id of the store
     * @param delayTime - the time to delay
     * @return - a response object with a message and a status
     */
    public Response delayDistribution(int shipmentId, int storeId, int delayTime) {
        try {
            shipmentFactory.delayDistribution(shipmentId, storeId, delayTime);
            return new Response();
        }
        catch(Exception ex) { return new Response(ex.getMessage()); }
    }

    /**
     * load all the shipments from the database
     * @return - a response object with a message and a status
     */
    public Response loadShipmentFactory() {
        try {
            shipmentFactory.load();
            return new Response();
        }
        catch(Exception ex) { return new Response(ex.getMessage()); }
    }
}
