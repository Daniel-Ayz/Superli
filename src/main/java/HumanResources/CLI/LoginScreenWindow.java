package HumanResources.CLI;

import HR_Delivery.DriverWindow;
import HR_Delivery.HRDeliveryMainMenu;
import HumanResources.GUI.EmployeeView.EmployeeMainScreen;
import HumanResources.ServiceLayer.Response;
import HumanResources.ServiceLayer.ServiceFactory;
import delivery.backend.serviceLayer.FactoryService;
import delivery.frontend.DriverMenuWindow;
import delivery.frontend.MenuWindow;
import org.python.antlr.ast.Str;

import javax.swing.*;

public class LoginScreenWindow extends Window{
    public LoginScreenWindow() {
        super(FactoryService.getInstance());
    }

    @Override
    public void open() {
        boolean suc = false;
        int role = 0;
        while (!suc) {
            print("""
                    Please enter role:
                    1 - Manager
                    2 - Employee
                    3 - Driver
                    4 - Logistic Manager""");

            try {
                role = Integer.parseInt(getInput());
                if (role == 1 || role == 2 || role == 3 || role == 4) {
                    suc = true;
                }
            } catch (Exception e) {

            }
            boolean isEmployee = false;
            boolean isManager = false;
            boolean isLogisticManager = false;

            switch (role) {
                case 1 -> isManager = true;
                case 2 -> isEmployee = true;
                case 3 -> isEmployee = true;
                case 4 -> isLogisticManager = true;
            }

            String username;
            print("Enter username:");
            username = getInput();

            char[] password;
            print("Enter password:");
            password = getInput().toCharArray();

            while (!isValidLogin(username, password, isEmployee, isManager, isLogisticManager)) {
                print("Invalid username or password.");
                print("Enter username:");
                username = getInput();
                print("Enter password:");
                password = getInput().toCharArray();
            }
            if (isEmployee) {
                Response res = ServiceFactory.getInstance().getEmployeeService().isDriver(username);
                if(res.isSuccess())
                    new DriverMenuWindow(FactoryService.getInstance());
                else
                    print("Employee");
            }
            else if(isLogisticManager){
                (new MenuWindow(FactoryService.getInstance())).open();
            }
            else {
                print("Manager");
            }

        }
    }

    private boolean isValidLogin(String username, char[] password, boolean isEmployee, boolean isManager, boolean isLogisticManager) {
        //TODO: REMOVE THIS, ONLY FOR TESTING
        if(true)
            return true;
        // For now, Manager has username "admin" and password "admin"
        if(isManager){
            return username.equals("admin") && new String(password).equals("admin");
        }
        else if(isLogisticManager){
            return username.equals("logistic") && new String(password).equals("logistic");
        }
        else if (!username.isEmpty() && password.length > 0){
            Response response = ServiceFactory.getInstance().getEmployeeService().checkPassword(username, new String(password));
            return response.isSuccess();
        }
        else {
            return false;
        }
    }
}
