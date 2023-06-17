package delivery.backend.businessLayer.item;

import delivery.backend.dal.Repository;
import delivery.backend.dal.dtos.ItemDTO;

public class Item {

    private int id;
    private String name;

    private Item(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Item(ItemDTO itemDTO) {
        this(itemDTO.getId(), itemDTO.getName());
    }

    public static Item createItem(int id, String name) {
        Item item = new Item(id, name);
        Repository.getInstance().getItemDAO().addItem(new ItemDTO(item));
        return item;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}' +
                "\n";
    }
}
