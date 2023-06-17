package delivery.frontend;

import delivery.backend.serviceLayer.FactoryService;
import delivery.tools.Response;

public class ProviderWindow extends Window{
    public ProviderWindow(FactoryService factoryService) {
        super(factoryService);
    }

    @Override
    public void open() {
        print("Enter address:");
        String address = getInput();
        print("Enter area, must be in { NORTH, SOUTH, CENTER, JERUSALEM }:");
        String area = getInput();
        print("Enter phone:");
        String phone = getInput();
        print("Enter contact name:");
        String contactName = getInput();

        boolean validNorth = false;
        int northCoordination = -1;
        while(!validNorth) {
            try {
                print("Enter North Coordinate:");
                northCoordination = Integer.parseInt(getInput());
                validNorth = true;
            }
            catch(Exception e) { print("invalid input"); }
        }

        boolean validEast = false;
        int eastCoordination = -1;
        while(!validEast) {
            try {
                print("Enter East Coordinate:");
                eastCoordination = Integer.parseInt(getInput());
                validEast = true;
            }
            catch(Exception e) {print("invalid input");}
        }

        Response response = factoryService.getDestinationService().addDestination(address, area, phone, contactName, northCoordination, eastCoordination,  true);
        if(!response.hasError())
            print("Provider was added successfully");
        else
            print(response.getMessage());

        this.close();
    }
}
