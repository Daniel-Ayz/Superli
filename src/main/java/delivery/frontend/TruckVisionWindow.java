package delivery.frontend;

import delivery.tools.Response;
import delivery.backend.serviceLayer.FactoryService;

public class TruckVisionWindow extends Window{
    public TruckVisionWindow(FactoryService factoryService) {
        super(factoryService);
    }

    @Override
    public void open() {
        Response response = factoryService.getTruckService().getAvailableTrucks();
        if(response.hasError())
            print(response.getMessage());
        else{
            print("Available Trucks:");
            print(response.getData().toString());
        }


        this.close();
    }
}
