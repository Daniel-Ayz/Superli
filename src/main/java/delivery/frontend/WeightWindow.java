package delivery.frontend;

import delivery.tools.Response;
import delivery.backend.serviceLayer.FactoryService;

public class WeightWindow extends Window{
    public WeightWindow(FactoryService factoryService) {
        super(factoryService);
    }

    @Override
    public void open() {
        print("Enter Shipment ID:");
        int shipmentId = Integer.parseInt(getInput());
        print("Enter Supplier ID:");
        int supplierId = Integer.parseInt(getInput());
        print("Enter weight:");
        int weight = Integer.parseInt(getInput());
        Response response = factoryService.getDeliveryHandlerService().setWeight(shipmentId, supplierId, weight);

        response = factoryService.getDeliveryHandlerService().isOverWeighted(shipmentId);

        if (response.hasError())
            print(response.getMessage());
        else if((boolean) response.getData()) {
            print("Truck is  in overweight");
            new TreatmentWindow(factoryService, supplierId, shipmentId).open();
        }
        else
            print("Truck Weight of order " + shipmentId + " at supplier " + supplierId + " was set successfully");

        this.close();
    }
}
