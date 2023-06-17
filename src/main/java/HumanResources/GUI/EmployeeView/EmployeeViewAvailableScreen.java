package HumanResources.GUI.EmployeeView;

import HumanResources.BusinessLayer.EmployeeModule.Role;
import HumanResources.BusinessLayer.ShiftModule.Shift;
import HumanResources.ServiceLayer.Response;
import HumanResources.ServiceLayer.ServiceFactory;
import HumanResources.ServiceLayer.ShiftServiceEmployee;

import javax.swing.*;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class EmployeeViewAvailableScreen {

    private JPanel mainPanel;
    private JList shiftsList;
    private JButton selectShiftButton;
    private String employeeId;
    private List<Shift> shifts;

    public EmployeeViewAvailableScreen(String employeeId) {
        this.employeeId = employeeId;
        JFrame frame = new JFrame("Available Shifts");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(mainPanel);
        frame.pack();

        int width = 1200;
        int height = 400;
        frame.setSize(width, height);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        loadData();
        // Add action listener for select shift button
        selectShiftButton.addActionListener(e -> selectShift(shifts));
    }

    private void loadData(){
        // Get available shifts for employee from service layer
        Response response = getShiftServiceEmployee().getAllShiftsAvailableFromDate(new Date(), employeeId);
        if (!response.isSuccess()) {
            JOptionPane.showMessageDialog(mainPanel, response.getData().toString());
            return;
        }
        shifts = (List<Shift>) response.getData();
        shifts.sort((s1, s2) -> s1.getDate().compareTo(s2.getDate()));

        // Populate shifts list in format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<String> shiftStrings = new ArrayList<>();
        for (Shift shift : shifts) {
            String dateString = formatter.format(shift.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
//            String shiftString = String.format("Shift #%s: Date: %s %s Branch: %s", shift.getShiftId(), dateString, shift.getShiftType(), shift.getBranchId());
//            shiftStrings.add(shiftString);
            // Get required roles and format them as a string
            StringBuilder rolesBuilder = new StringBuilder();
            Map<Role, Integer> requiredRoles = shift.getRequiredEmployees();
            for (Map.Entry<Role, Integer> entry : requiredRoles.entrySet()) {
                Role role = entry.getKey();
                int count = entry.getValue();
                rolesBuilder.append(String.format("%s (%d), ", role.toString(), count));
            }
            // Remove last comma and space from roles string
            String rolesString = rolesBuilder.toString().replaceAll(", $", "");

            // Create shift string and add it to the list
            String shiftString = String.format("Shift #%s: Date: %s Type: %s Roles: %s", shift.getShiftId(), dateString, shift.getShiftType(), rolesString);
            shiftStrings.add(shiftString);

        }
        shiftsList.setListData(shiftStrings.toArray(new String[0]));

    }

    private Shift selectShiftFromList(List<Shift> shifts){
        int selectedIndex = shiftsList.getSelectedIndex();
        if (selectedIndex >= 0) {
            return shifts.get(selectedIndex);
        } else {
            return null;
        }
    }

    private void selectShift(List<Shift> shifts) {
        // Get selected shift from list

        Shift selectedShift = selectShiftFromList(shifts);
        if (selectedShift == null) {
            JOptionPane.showMessageDialog(mainPanel, "Please select a shift.");
            return;
        }

        // Request shift from service layer
        Response response = getShiftServiceEmployee().requestShiftByEmployee(employeeId, selectedShift.getShiftId());
        if (!response.isSuccess()) {
            JOptionPane.showMessageDialog(mainPanel, response.getData().toString());
            return;
        }
        else {
            JOptionPane.showMessageDialog(mainPanel, "Shift requested successfully.");
        }
        loadData();
    }

    public ShiftServiceEmployee getShiftServiceEmployee() {
        return ServiceFactory.getInstance().getShiftServiceEmployee();
    }
}
