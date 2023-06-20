package HumanResources.CLI;

import delivery.backend.serviceLayer.FactoryService;

public class EmployeeWindow extends Window{
    public EmployeeWindow() {
        super(FactoryService.getInstance());
    }


    @Override
    public void open() {
        print("""
                Please select an option:
                1 - view shift
                2 - request shift
                3 - logout""");

        String input = getInput();

        switch (input) {
            case "1" -> print("view shift");
            case "2" -> print("request shift");
            case "3" -> {
                new LoginScreenWindow().open();
            }
            default -> print("Invalid input");
        }
    }
}
