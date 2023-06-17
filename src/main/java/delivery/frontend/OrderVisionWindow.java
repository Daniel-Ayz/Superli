package delivery.frontend;

import delivery.tools.Response;
import delivery.backend.serviceLayer.FactoryService;

public class OrderVisionWindow extends Window{

        public OrderVisionWindow(FactoryService factoryService) {
            super(factoryService);
        }

        @Override
        public void open() {
            Response response = factoryService.getDeliveryHandlerService().getOrders();

            if(response.hasError())
                print(response.getMessage());
            else {
                print("Orders:");
                print(response.getData().toString());
            }


            this.close();
        }
}
