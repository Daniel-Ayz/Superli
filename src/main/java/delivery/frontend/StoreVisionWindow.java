package delivery.frontend;

import delivery.tools.Response;
import delivery.backend.serviceLayer.FactoryService;

public class StoreVisionWindow extends Window{
    public StoreVisionWindow(FactoryService factoryService) {
        super(factoryService);
    }

    @Override
    public void open() {
        Response response = factoryService.getDestinationService().getStores();
        if(response.hasError())
            print(response.getMessage());
        else {
            print("Stores:");
            print(response.getData().toString());
        }


        this.close();
    }
}
