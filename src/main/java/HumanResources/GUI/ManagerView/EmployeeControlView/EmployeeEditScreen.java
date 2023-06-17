package HumanResources.GUI.ManagerView.EmployeeControlView;

import HumanResources.BusinessLayer.EmployeeModule.Employee;
import HumanResources.ServiceLayer.EmployeeService;
import HumanResources.ServiceLayer.Response;
import HumanResources.ServiceLayer.ServiceFactory;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EmployeeEditScreen {

    private JPanel mainPanel;
    private JList employeesList;
    private JButton selectEmployeeButton;

    public EmployeeEditScreen() {
        JFrame frame = new JFrame("Employee Edit Screen");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(mainPanel);
        frame.pack();

        int width = 1200;
        int height = 400;
        frame.setSize(width, height);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Get employees from service layer
        Response response = getEmployeeService().getAllEmployees();
        if (!response.isSuccess()) {
            JOptionPane.showMessageDialog(mainPanel, response.getData().toString());
            return;
        }
        List<Employee> employees = (List<Employee>) response.getData();
        employees.sort(Comparator.comparing(Employee::getId));

        // Populate employees list
        List<String> employeeStrings = new ArrayList<>();
        for (Employee employee : employees) {
            String employeeString = String.format("Employee #%s: %s", employee.getId(), employee.getName());
            employeeStrings.add(employeeString);
        }
        employeesList.setListData(employeeStrings.toArray(new String[0]));

        // Add action listener for select employee button
        selectEmployeeButton.addActionListener(e -> selectEmployee(employees));
    }

    private Employee selectEmployeeFromList(List<Employee> employees) {
        int selectedIndex = employeesList.getSelectedIndex();
        if (selectedIndex >= 0) {
            return employees.get(selectedIndex);
        } else {
            return null;
        }
    }

    private void selectEmployee(List<Employee> employees) {
        // Get selected employee from list
        Employee selectedEmployee = selectEmployeeFromList(employees);
        if (selectedEmployee == null) {
            JOptionPane.showMessageDialog(mainPanel, "Please select an employee.");
            return;
        }
        // Open employee detail screen for selected employee

        new EmployeeDetailScreen(selectedEmployee);
    }

    public EmployeeService getEmployeeService() {
        return ServiceFactory.getInstance().getEmployeeService();
    }
}
