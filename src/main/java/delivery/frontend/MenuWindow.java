package delivery.frontend;

import HumanResources.CLI.LoginScreenWindow;
import delivery.backend.serviceLayer.FactoryService;

public class MenuWindow extends Window{
    public MenuWindow(FactoryService factoryService) {
        super(factoryService);
    }
    @Override
    public void open() {
        while (active) {
            print("""
                    Choose action:
                    1) add truck
                    2) add item
                    3) add store
                    4) add provider
                    5) show available trucks
                    6) show items
                    7) show stores
                    8) show providers
                    9) make order
                    10) set truck weight
                    11) handle shortage
                    12) show shortages
                    13) show orders
                    14) show estimated arrival time for store
                    15) report traffic
                    0) exit

                    A legal choice must be between 0 and 15""");

            switch (getInput()) {
                case ("1") -> new TruckWindow(factoryService).open();
                case ("2") -> new ItemWindow(factoryService).open();
                case ("3") -> new StoreWindow(factoryService).open();
                case ("4") -> new ProviderWindow(factoryService).open();
                case ("5") -> new TruckVisionWindow(factoryService).open();
                case ("6") -> new ItemVisionWindow(factoryService).open();
                case ("7") -> new StoreVisionWindow(factoryService).open();
                case ("8") -> new ProviderVisionWindow(factoryService).open();
                case ("9") -> new OrderWindow(factoryService).open();
                case ("10") -> new WeightWindow(factoryService).open();
                case ("11") -> new ShortageWindow(factoryService).open();
                case ("12") -> new ShortageVisionWindow(factoryService).open();
                case ("13") -> new OrderVisionWindow(factoryService).open();
                case ("14") -> new ShowExpectedTimeWindow(factoryService).open();
                case ("15") -> new ReportDistributionDelayWindow(factoryService).open();
                case ("0") -> {new LoginScreenWindow().open(); close();}
                default -> print("Illegal choice");
            }

        }
    }
}
