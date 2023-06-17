package delivery.frontend;

import delivery.tools.Response;
import delivery.backend.serviceLayer.FactoryService;

public class ShortageWindow extends Window{
    public ShortageWindow(FactoryService factoryService) { super(factoryService); }

    @Override
    public void open() {
        Response response = factoryService.getDeliveryHandlerService().handleShortage();
        if(response.hasError())
            print(response.getMessage());

        else {
            print("Order created successfully:");
            print(response.getData().toString());
        }

        this.close();
    }


}
