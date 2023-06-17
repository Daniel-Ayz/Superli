package delivery.backend.dal.dtos.file;

import delivery.backend.dal.dtos.DTO;

public class FileItemDTO extends DTO {
    private int documentId;
    private int itemId;
    private int amount;

    public FileItemDTO(int documentId, int itemId, int amount) {
        this.documentId = documentId;
        this.itemId = itemId;
        this.amount = amount;
    }

    public int getDocumentId() {
        return documentId;
    }

    public int getItemId() {
        return itemId;
    }

    public int getAmount() {
        return amount;
    }
}
