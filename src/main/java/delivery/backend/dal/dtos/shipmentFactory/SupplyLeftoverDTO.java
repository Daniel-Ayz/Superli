package delivery.backend.dal.dtos.shipmentFactory;

import delivery.backend.dal.dtos.DTO;

public class SupplyLeftoverDTO extends DTO {
    private int providerId;
    private int itemId;
    private int amount;

    public SupplyLeftoverDTO(int providerId, int itemId, int amount) {
        this.providerId = providerId;
        this.itemId = itemId;
        this.amount = amount;
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
