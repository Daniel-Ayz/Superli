package delivery.backend.dal.dtos.shipment;

import delivery.backend.dal.dtos.DTO;

public class ShipmentProviderItemDTO extends DTO {
    private int shipmentId;
    private int providerId;
    private int itemId;
    private int amount;

    public ShipmentProviderItemDTO(int shipmentId, int providerId, int itemId, int amount) {
        this.shipmentId = shipmentId;
        this.providerId = providerId;
        this.itemId = itemId;
        this.amount = amount;
    }

    public int getShipmentId() {
        return shipmentId;
    }

    public int getProviderId() {
        return providerId;
    }

    public int getItemId() {
        return itemId;
    }

    public int getAmount() {
        return amount;
    }
}
