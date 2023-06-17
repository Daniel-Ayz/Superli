package delivery.backend.dal.dtos.shipment;

import delivery.backend.dal.dtos.DTO;

public class ShipmentStoreItemDTO extends DTO {
    private int shipmentId;
    private int storeId;
    private int itemId;
    private int amount;

    public ShipmentStoreItemDTO(int shipmentId, int storeId, int itemId, int amount) {
        this.shipmentId = shipmentId;
        this.storeId = storeId;
        this.itemId = itemId;
        this.amount = amount;
    }

    public int getShipmentId() {
        return shipmentId;
    }

    public int getStoreId() {
        return storeId;
    }

    public int getItemId() {
        return itemId;
    }

    public int getAmount() {
        return amount;
    }
}
