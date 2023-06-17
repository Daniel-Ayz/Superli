package delivery.backend.businessLayer.shipment;

import delivery.backend.businessLayer.destination.Store;
import delivery.backend.businessLayer.item.Item;
import delivery.backend.dal.Repository;
import delivery.backend.dal.dtos.file.DocumentedFileDTO;
import delivery.backend.dal.dtos.file.FileItemDTO;

import java.util.List;
import java.util.Map;

public class DocumentedFile {
    private int documentId;
    private Store store;
    private Map<Item, Integer> products;

    private DocumentedFile(int documentId, Store store, Map<Item, Integer> products) {
        this.documentId = documentId;
        this.store = store;
        this.products = products;
    }

    public DocumentedFile(DocumentedFileDTO documentedFileDTO, Store store, Map<Item, Integer> products) {
        this(documentedFileDTO.getDocumentId(), store, products);
    }

    public Map<Item, Integer> getProducts() {
        return products;
    }

    public Store getStore() {
        return store;
    }
    public int getDocumentId() {
        return documentId;
    }

    public static DocumentedFile createDocumentedFile(int documentId, Store store, Map<Item, Integer> products, int shipmentId) {
        DocumentedFile  documentedFile = new DocumentedFile(documentId, store, products);
        Repository.getInstance().getDocumentedFileDAO().addDocumentedFile(new DocumentedFileDTO(documentedFile, shipmentId));
        Repository.getInstance().putDocumentedFileProducts(documentId, products);
        return documentedFile;
    }
}
