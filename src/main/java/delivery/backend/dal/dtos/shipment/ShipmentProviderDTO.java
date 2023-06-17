package delivery.backend.dal.dtos.shipment;

import delivery.backend.dal.dtos.DTO;

public class ShipmentProviderDTO extends DTO {
    private int shipmentId;
    private int providerId;

    private boolean visited;

    public ShipmentProviderDTO(int shipmentId, int providerId, boolean visited) {
        this.shipmentId = shipmentId;
        this.providerId = providerId;
        this.visited = visited;
    }

    public int getShipmentId() {
        return shipmentId;
    }

    public int getProviderId() {
        return providerId;
    }

    public boolean getVisited() {
        return visited;
    }
}
