package HumanResources.GUI.ManagerView.ShiftView;

import HumanResources.BusinessLayer.EmployeeModule.Employee;
import HumanResources.BusinessLayer.EmployeeModule.Role;
import HumanResources.BusinessLayer.ShiftModule.Shift;
import HumanResources.ServiceLayer.Response;
import HumanResources.ServiceLayer.ServiceFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ShiftDetailScreen extends JFrame {
    private JPanel mainPanel;
    private JLabel shiftIdLabel;
    private JLabel dateLabel;
    private JLabel shiftTypeLabel;
    private JLabel branchLabel;
    private JList<Employee> requestedEmployeesList;
    private JList<String> approvedEmployeesList;
    private JTable rolesTable;
    private JButton approveButton;
    private JButton disapproveButton;
    private JButton addRoleButton;
    private JButton removeRoleButton;
    private JLabel requestedThisShiftLabel;
    private JLabel approvedOnShiftLabel;
    private JLabel requiredRolesOnShiftLabel;
    private Shift shift;

    public ShiftDetailScreen(Shift shift) {
        this.shift = shift;
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Shift Detail");

        // Set shift info
        shiftIdLabel.setText("Shift ID: " + shift.getShiftId());
        dateLabel.setText("Date: " + shift.getDate().toString());
        shiftTypeLabel.setText("Shift Type: " + shift.getShiftType().toString());
        branchLabel.setText("Branch: " + shift.getBranchId());

        updateLists();

        // Set roles table
        DefaultTableModel rolesModel = (DefaultTableModel) rolesTable.getModel();
        rolesModel.addColumn("Role");
        rolesModel.addColumn("Count");
        for (Role role : shift.getRequiredEmployees().keySet()) {
            int count = shift.getRequiredEmployees().get(role);
            rolesModel.addRow(new Object[]{role, count});
        }

        // Set button listeners
        approveButton.addActionListener(e -> approveShift());
        disapproveButton.addActionListener(e -> disapproveShift());
        addRoleButton.addActionListener(e -> addRoleToShift());
        removeRoleButton.addActionListener(e -> removeRoleFromShift());

        int width = 600;
        int height = 400;
        setSize(width, height);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateLists() {
        // Set requested employees list
        DefaultListModel<Employee> requestedEmployeesModel = new DefaultListModel<>();
        for (Employee employee : shift.getRequestedEmployees()) {
            requestedEmployeesModel.addElement(employee);
        }
        requestedEmployeesList.setModel(requestedEmployeesModel);
        requestedEmployeesList.setCellRenderer(new EmployeeListCellRenderer());

        // Set approved employees list
        DefaultListModel<String> approvedEmployeesModel = new DefaultListModel<>();
        for (Employee employee : shift.getApprovedEmployees().keySet()) {
            Role role = shift.getApprovedEmployees().get(employee);
            String employeeInfo = String.format("%s - %s (%s)", employee.getId(), employee.getName(), role);
            approvedEmployeesModel.addElement(employeeInfo);
        }
        approvedEmployeesList.setModel(approvedEmployeesModel);
    }

    private void approveShift() {
        // Get the selected employee from the requested employees list
        Employee employee = requestedEmployeesList.getSelectedValue();
        if (employee == null) {
            JOptionPane.showMessageDialog(this, "Please select an employee to approve.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get the selected role from the employee's roles
        Object[] roles = employee.getRoles().toArray();
        if (roles.length == 0) {
            JOptionPane.showMessageDialog(this, "The selected employee has no roles.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Role role = (Role) JOptionPane.showInputDialog(this, "Select a role:", "Role Selection", JOptionPane.PLAIN_MESSAGE, null, roles, roles[0]);
        if (role == null) {
            return;
        }

        // Call the approveShift() service function
        Response response = ServiceFactory.getInstance().getShiftServiceManagement().approveShift(shift.getShiftId(), employee.getId(), role);
        if (response.isSuccess()) {
            updateLists();
            JOptionPane.showMessageDialog(this, "Shift approved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, response.getData().toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void disapproveShift() {
        // Get the selected employee from the approved employees list
        String employeeInfo = approvedEmployeesList.getSelectedValue();
        if (employeeInfo == null) {
            JOptionPane.showMessageDialog(this, "Please select an employee to disapprove.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Extract the employee id from the employee info string
        String[] tokens = employeeInfo.split(" - ");
        String employeeId = tokens[0];

        // Call the disapproveShift() service function
        Response response = ServiceFactory.getInstance().getShiftServiceManagement().disapproveShift(shift.getShiftId(), employeeId);
        if (response.isSuccess()) {
            updateLists();
            JOptionPane.showMessageDialog(this, "Shift disapproved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, response.getData().toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addRoleToShift() {
        // Get the selected role from the available roles
        Object[] roles = Role.values();
        Role role = (Role) JOptionPane.showInputDialog(this, "Select a role to add:", "Add Required Role", JOptionPane.PLAIN_MESSAGE, null, roles, roles[0]);
        if (role == null) {
            return;
        }

        // Get the count of required employees for the role
        String countStr = JOptionPane.showInputDialog(this, "Enter the count of required employees for the role:", "Add Required Role", JOptionPane.PLAIN_MESSAGE);
        if (countStr == null || countStr.isEmpty()) {
            return;
        }
        int count;
        try {
            count = Integer.parseInt(countStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid count entered.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Call the addRequiredRoleToShift() service function
        Response response = ServiceFactory.getInstance().getShiftServiceManagement().addRequiredRoleToShift(shift.getShiftId(), role, count);
        if (response.isSuccess()) {
            updateRolesTable();
            JOptionPane.showMessageDialog(this, "Required role added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, response.getData().toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeRoleFromShift() {
        // Get the selected role from the required roles
        int rowIndex = rolesTable.getSelectedRow();
        if (rowIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a role to remove.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Role role = (Role) rolesTable.getValueAt(rowIndex, 0);

        // Call the removeRequiredRoleFromShift() service function
        Response response = ServiceFactory.getInstance().getShiftServiceManagement().removeRequiredRoleFromShift(shift.getShiftId(), role);
        if (response.isSuccess()) {
            updateRolesTable();
            JOptionPane.showMessageDialog(this, "Required role removed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, response.getData().toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateRolesTable() {
        // Set roles table
        DefaultTableModel rolesModel = (DefaultTableModel) rolesTable.getModel();
        rolesModel.setRowCount(0);
        for (Role role : shift.getRequiredEmployees().keySet()) {
            int count = shift.getRequiredEmployees().get(role);
            rolesModel.addRow(new Object[]{role, count});
        }
    }



    private class EmployeeListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Employee employee = (Employee) value;
            String employeeInfo = String.format("%s - %s (%s)", employee.getId(), employee.getName(), employee.getRoles());
            return super.getListCellRendererComponent(list, employeeInfo, index, isSelected, cellHasFocus);
        }
    }
}

