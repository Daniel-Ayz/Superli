package delivery.frontend;

import delivery.backend.serviceLayer.FactoryService;
import delivery.tools.Response;

public class ItemWindow extends Window{
    public ItemWindow(FactoryService factoryService) {
        super(factoryService);
    }

    @Override
    public void open() {
        print("Enter name:");
        String name = getInput();
        Response response = factoryService.getItemService().addItem(name);
        if(!response.hasError())
            print("Item was added successfully");
        else
            print(response.getMessage());

        this.close();
    }
}
