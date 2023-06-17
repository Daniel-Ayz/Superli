package delivery.backend.dal.dtos.shipment;

import delivery.backend.dal.dtos.DTO;

import java.time.LocalTime;

public class ShipmentEstimationsDTO extends DTO {
    private int shipmentId;
    private int storeId;
    private String estimation;

    public ShipmentEstimationsDTO(int shipmentId, int storeId, LocalTime estimation) {
        this.shipmentId = shipmentId;
        this.storeId = storeId;
        this.estimation = estimation.toString();
    }

    public int getShipmentId() {
        return shipmentId;
    }

    public int getStoreId() {
        return storeId;
    }

    public LocalTime getEstimation() {
        return LocalTime.parse(estimation);
    }
}
