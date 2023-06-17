package delivery.frontend;

import delivery.tools.Response;
import delivery.backend.serviceLayer.FactoryService;

public class ProviderVisionWindow extends Window{
    public ProviderVisionWindow(FactoryService factoryService) {
        super(factoryService);
    }

    @Override
    public void open() {
        Response response = factoryService.getDestinationService().getProviders();
        if(response.hasError())
            print(response.getMessage());
        else {
            print("Providers:");
            print(response.getData().toString());
        }


        this.close();
    }
}
