package delivery.backend.serviceLayer;

import delivery.backend.businessLayer.item.ItemController;
import delivery.tools.Response;

public class ItemService {
    private ItemController itemController;

    public ItemService(ItemController itemController){
        this.itemController = itemController;
    }

    /**
     * @param name - item name
     * @return - a response object with a message and a status
     */
    public Response addItem(String name) {
        try {
            itemController.addItem(name);
            return new Response();
        }
        catch(Exception e) { return new Response(e.getMessage()); }
    }

    /**
     * @return - a response object with a message, a status and a list of items
     */
    public Response getItems(){
        try {
            return new Response(itemController.getItems());
        }
        catch(Exception e) { return new Response(e.getMessage()); }
    }

    /**
     * load information about all items from the database
     * @return - a response object with a message
     */
    public Response loadItems() {
        try {
            itemController.loadItems();
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }
}
