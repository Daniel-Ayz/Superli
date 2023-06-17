package delivery.backend.dal.dtos.file;

import delivery.backend.businessLayer.shipment.DocumentedFile;
import delivery.backend.dal.dtos.DTO;

public class DocumentedFileDTO extends DTO {
    private int documentId;
    private int shipmentId;
    private int storeId;

    public DocumentedFileDTO(int documentId, int shipmentId, int storeId) {
        this.documentId = documentId;
        this.shipmentId = shipmentId;
        this.storeId = storeId;
    }

    public DocumentedFileDTO(DocumentedFile documentedFile, int shipmentId) {
        this.documentId = documentedFile.getDocumentId();
        this.shipmentId = shipmentId;
        this.storeId = documentedFile.getStore().getId();
    }

    public int getDocumentId() {
        return documentId;
    }

    public int getShipmentId() {
        return shipmentId;
    }

    public int getStoreId() {
        return storeId;
    }
}
