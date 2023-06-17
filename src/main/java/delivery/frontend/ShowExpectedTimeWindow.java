package delivery.frontend;

import delivery.backend.serviceLayer.FactoryService;
import delivery.tools.Response;

public class ShowExpectedTimeWindow extends  Window{
    public ShowExpectedTimeWindow(FactoryService factoryService) {
        super(factoryService);
    }

    @Override
    public void open() {
        boolean validId = false;
        int shipmentId = -1;
        while(!validId) {
            try {
                print("Enter Shipment Id:");
                shipmentId = Integer.parseInt(getInput());
                validId = true;
            }
            catch(Exception e) { print("invalid input"); }
        }

        int storeId = -1;
        validId = false;
        while(!validId) {
            try {
                print("Enter Store Id:");
                storeId = Integer.parseInt(getInput());
                validId = true;
            }
            catch(Exception e) { print("invalid input"); }
        }

        Response response = factoryService.getDeliveryHandlerService().getExpectedTime(shipmentId, storeId);
        if(response.hasError())
            print(response.getMessage());
        else {
            print("Expected arrival time: " + response.getData().toString());
        }

        this.close();
    }


}
