package delivery.backend.businessLayer.item;

import delivery.backend.dal.Repository;
import delivery.backend.dal.dtos.ItemDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemController {
    private Map<Integer, Item> items;

    public ItemController() {
        this.items = new HashMap<>();
    }

    /**
     * Add a new item to the system
     * @param name - item name
     */
    public void addItem(String name) {
        int id = items.size();
        items.put(id, Item.createItem(id, name));
    }

    /**
     * Returns a list of all items
     * @return List<Item>
     */
    public List<Item> getItems() {
        return items.values().stream().toList();
    }

    /**
     * Returns an item by id
     * @param itemId - item id
     * @return Item
     */
    public Item getItem(Integer itemId) {
        if (items.containsKey(itemId)) {
            return items.get(itemId);
        }
        throw new IllegalArgumentException("no such item: " + itemId);
    }

    public void loadItems() {
        try {
            List<Item> items = Repository.getInstance().getItemDAO().selectAll().stream().map((itemDTO) -> (ItemDTO) itemDTO).map(Item::new).toList();
            for (Item item : items) {
                this.items.put(item.getId(), item);
            }
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to load items from database");
        }
    }
}
