package delivery.backend.dal.dtos.shipmentFactory;

import delivery.backend.dal.dtos.DTO;

public class ShipmentFactoryDTO extends DTO {
    private int documentIdGenerator;

    public ShipmentFactoryDTO(int documentIdGenerator) {
        this.documentIdGenerator = documentIdGenerator;
    }
}
