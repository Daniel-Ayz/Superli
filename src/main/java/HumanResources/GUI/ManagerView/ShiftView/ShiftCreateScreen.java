package HumanResources.GUI.ManagerView.ShiftView;

import HumanResources.BusinessLayer.ShiftModule.ShiftType;
import HumanResources.ServiceLayer.Response;
import HumanResources.ServiceLayer.ServiceFactory;
import HumanResources.ServiceLayer.ShiftServiceManagement;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShiftCreateScreen {
    private JPanel mainPanel;
    private JTextField branchIdTextField;
    private JComboBox shiftTypeComboBox;
    private JButton addShiftButton;
    private JSpinner dateSpinner;

    public ShiftCreateScreen() {
        JFrame frame = new JFrame("Create Shift");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Populate shift type combo box
        for (ShiftType type : ShiftType.values()) {
            shiftTypeComboBox.addItem(type.toString());
        }

        // Set up date spinner
        SpinnerDateModel spinnerModel = new SpinnerDateModel();
        dateSpinner.setModel(spinnerModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
        dateSpinner.setEditor(dateEditor);

        // Add action listener for addShift button
        addShiftButton.addActionListener(e -> addShift());
    }

    private void addShift() {
        // Get input values from fields
        String branchIdString = branchIdTextField.getText();
        String shiftTypeString = String.valueOf(shiftTypeComboBox.getSelectedItem());
        Date date = (Date) dateSpinner.getValue();

        // Validate inputs
        if (branchIdString.isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "Please enter branch ID");
            return;
        }
        int branchId;
        try {
            branchId = Integer.parseInt(branchIdString);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mainPanel, "Invalid branch ID: " + branchIdString);
            return;
        }
        if (date == null) {
            JOptionPane.showMessageDialog(mainPanel, "Please select a date");
            return;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        ShiftType shiftType = ShiftType.valueOf(shiftTypeString);

        // Add shift using service layer
        ShiftServiceManagement shiftServiceManagement = getShiftServiceManagement();
        Response response = shiftServiceManagement.addShift(date, shiftType, branchId);

        // Show message and close window
        JOptionPane.showMessageDialog(mainPanel, response.getData().toString());
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
        frame.dispose();
    }

    public ShiftServiceManagement getShiftServiceManagement() {
        return ServiceFactory.getInstance().getShiftServiceManagement();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
