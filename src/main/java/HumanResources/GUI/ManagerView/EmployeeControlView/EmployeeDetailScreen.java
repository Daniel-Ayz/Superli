package HumanResources.GUI.ManagerView.EmployeeControlView;

import HumanResources.BusinessLayer.EmployeeModule.Employee;
import HumanResources.BusinessLayer.EmployeeModule.Role;
import HumanResources.ServiceLayer.EmployeeService;
import HumanResources.ServiceLayer.Response;
import HumanResources.ServiceLayer.ServiceFactory;

import javax.swing.*;
import java.util.List;

public class EmployeeDetailScreen {

    private JPanel mainPanel;
    private JLabel employeeIdLabel;
    private JLabel nameLabel;
    private JList<Role> roleList;
    private JButton addRoleButton;
    private JButton removeRoleButton;
    private Employee employee;
    private EmployeeService employeeServiceManagement;

    public EmployeeDetailScreen(Employee employee) {
        this.employee = employee;
        this.employeeServiceManagement = ServiceFactory.getInstance().getEmployeeService();
        JFrame frame = new JFrame("Employee Detail Screen");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(mainPanel);
        frame.pack();

        int width = 500;
        int height = 400;
        frame.setSize(width, height);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        populateFields();
        populateRolesList();

        addRoleButton.addActionListener(e -> addRole());
        removeRoleButton.addActionListener(e -> removeRole());
    }

    private void populateFields() {
        employeeIdLabel.setText(String.format("Employee ID: %s", employee.getId()));
        nameLabel.setText(String.format("Name: %s", employee.getName()));
        //TODO: add more fields later
    }

    private void populateRolesList() {
        List<Role> roles = employee.getRoles();
        roleList.setListData(roles.toArray(new Role[0]));
    }

    private void addRole() {
        // Show dialog to select role to add
        Object[] roleOptions = Role.values();
        Role selectedRole = (Role) JOptionPane.showInputDialog(mainPanel, "Select role to add:", "Add Role", JOptionPane.PLAIN_MESSAGE, null, roleOptions, roleOptions[0]);

        if (selectedRole != null) {
            // Add role to employee
            Response response = employeeServiceManagement.addRole(employee.getId(), selectedRole);
            if (response.isSuccess()) {
                employee = (Employee) employeeServiceManagement.getEmployee(employee.getId()).getData();
                populateRolesList();
                JOptionPane.showMessageDialog(mainPanel, "Role added successfully.");
            } else {
                JOptionPane.showMessageDialog(mainPanel, response.getData().toString());
            }
        }
    }

    private void removeRole() {
        // Get selected role from list
        Role selectedRole = roleList.getSelectedValue();
        if (selectedRole == null) {
            JOptionPane.showMessageDialog(mainPanel, "Please select a role to remove.");
            return;
        }

        // Remove role from employee
        Response response = employeeServiceManagement.removeRole(employee.getId(), selectedRole);
        if (response.isSuccess()) {
            employee = (Employee) employeeServiceManagement.getEmployee(employee.getId()).getData();
            populateRolesList();
            JOptionPane.showMessageDialog(mainPanel, "Role removed successfully.");
        } else {
            JOptionPane.showMessageDialog(mainPanel, response.getData().toString());
        }
    }
}
