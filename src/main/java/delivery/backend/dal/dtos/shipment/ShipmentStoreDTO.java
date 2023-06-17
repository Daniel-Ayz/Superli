package delivery.backend.dal.dtos.shipment;

import delivery.backend.dal.dtos.DTO;

public class ShipmentStoreDTO extends DTO {
    private int shipmentId;
    private int storeId;

    public ShipmentStoreDTO(int shipmentId, int storeId) {
        this.shipmentId = shipmentId;
        this.storeId = storeId;
    }

    public int getShipmentId() {
        return shipmentId;
    }

    public int getStoreId() {
        return storeId;
    }
}
