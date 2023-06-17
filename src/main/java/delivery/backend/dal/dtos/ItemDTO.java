package delivery.backend.dal.dtos;

import delivery.backend.businessLayer.item.Item;

public class ItemDTO extends DTO {
    private int id;
    private String name;

    public ItemDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public ItemDTO(Item item) {
        this.id = item.getId();
        this.name = item.getName();
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}
