package delivery.frontend;

import delivery.tools.Response;
import delivery.backend.serviceLayer.FactoryService;

public class ItemVisionWindow extends Window{
    public ItemVisionWindow(FactoryService factoryService) {
        super(factoryService);
    }

    @Override
    public void open() {
        Response response = factoryService.getItemService().getItems();
        if(response.hasError())
            print(response.getMessage());
        else {
            print("Items:");
            print(response.getData().toString());
        }


        this.close();
    }
}
