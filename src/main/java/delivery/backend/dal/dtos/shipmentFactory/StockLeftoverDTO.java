package delivery.backend.dal.dtos.shipmentFactory;

import delivery.backend.dal.dtos.DTO;

public class StockLeftoverDTO extends DTO {
    private int storeId;
    private int itemId;
    private int amount;

    public StockLeftoverDTO(int storeId, int itemId, int amount) {
        this.storeId = storeId;
        this.itemId = itemId;
        this.amount = amount;
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
