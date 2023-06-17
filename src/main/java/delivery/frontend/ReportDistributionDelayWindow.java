package delivery.frontend;

import delivery.backend.serviceLayer.FactoryService;
import delivery.tools.Response;

public class ReportDistributionDelayWindow extends Window{
    public ReportDistributionDelayWindow(FactoryService factoryService) {
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
                print("Enter Destination Store Id:");
                storeId = Integer.parseInt(getInput());
                validId = true;
            }
            catch(Exception e) { print("invalid input"); }
        }

        int delayTime = -1;
        validId = false;
        while(!validId) {
            try {
                print("Enter Delay(in minutes):");
                delayTime = Integer.parseInt(getInput());
                validId = true;
            }
            catch(Exception e) { print("invalid input"); }
        }

        Response response = factoryService.getDeliveryHandlerService().delayDistribution(shipmentId, storeId, delayTime);
        if(response.hasError())
            print(response.getMessage());
        else {
            print("Shipment Distribution Was Delayed By " + delayTime + " Minutes.");
        }

        this.close();
    }
}
