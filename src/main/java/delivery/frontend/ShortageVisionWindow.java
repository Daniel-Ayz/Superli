package delivery.frontend;

import delivery.tools.Response;
import delivery.backend.serviceLayer.FactoryService;

public class ShortageVisionWindow extends Window{
    public ShortageVisionWindow(FactoryService factoryService) { super(factoryService); }

    @Override
    public void open() {
        Response response = factoryService.getDeliveryHandlerService().viewStoreShortage();
        if(response.hasError())
            print(response.getMessage());
        else{
            print("Store's Shortages:");
            print(response.getData().toString());
        }

        response = factoryService.getDeliveryHandlerService().viewProvidersLeftovers();
        if(response.hasError())
            print(response.getMessage());
        else{
            print("Provider's Leftovers:");
            print(response.getData().toString());
        }

        this.close();
    }


}
