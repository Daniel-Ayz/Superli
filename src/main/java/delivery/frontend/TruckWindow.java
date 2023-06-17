package delivery.frontend;

import delivery.backend.serviceLayer.FactoryService;
import delivery.tools.Response;

public class TruckWindow extends Window{

    public TruckWindow(FactoryService factoryService) {
        super(factoryService);
    }

    @Override
    public void open() {
        print("Enter license number, must be unique:");
        String licenseNumber = getInput();
        print("Enter model:");
        String model = getInput();
        print("Enter weight:");
        int weight = Integer.parseInt(getInput());
        print("Enter max weight:");
        int maxWeight = Integer.parseInt(getInput());
        print("Enter license type, must be in {REGULAR_LIGHT, REGULAR_HEAVY, COLD_LIGHT, COLD_HEAVY}:");
        String licenseType = getInput();
        Response response = factoryService.getTruckService().addTruck(licenseNumber, model, weight, maxWeight, licenseType);
        if(!response.hasError())
            print("Truck added successfully");
        else
            print(response.getMessage());

        this.close();
    }
}
