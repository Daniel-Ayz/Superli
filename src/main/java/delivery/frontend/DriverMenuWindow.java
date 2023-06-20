package delivery.frontend;

import delivery.backend.serviceLayer.FactoryService;

public class DriverMenuWindow extends Window {
    public DriverMenuWindow(FactoryService factoryService) {
        super(factoryService);
    }

    @Override
    public void open() {
        while (active) {
            print("""
                    Choose action:
                    10) set truck weight
                    13) show orders
                    14) show estimated arrival time for store
                    15) report traffic
                    0) exit

                    A legal choice must be between 0 and 15""");

            switch (getInput()) {
                case ("10") -> new WeightWindow(factoryService).open();
                case ("13") -> new OrderVisionWindow(factoryService).open();
                case ("14") -> new ShowExpectedTimeWindow(factoryService).open();
                case ("15") -> new ReportDistributionDelayWindow(factoryService).open();
                case ("0") -> {print("Thanks And Goodbye!"); close();}
                default -> print("Illegal choice");
            }

        }
    }


}
