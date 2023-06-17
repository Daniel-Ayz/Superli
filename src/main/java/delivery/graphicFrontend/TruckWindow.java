package delivery.graphicFrontend;

import HR_Delivery.LicenseType;
import delivery.backend.serviceLayer.FactoryService;
import delivery.tools.Response;

import javax.swing.*;

public class TruckWindow {
    private JPanel mainPanel;
    private JTextField licenseNumberTextField;
    private JTextField modelTextField;
    private JTextField weightTextField;
    private JTextField maxWeightTextField;
    private JComboBox licenseTypeComboBox;
    private JButton addTruckButton;


    public TruckWindow() {
        JFrame frame = new JFrame("Add Truck Window");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();

        int width = 600;
        int height = 400;
        frame.setSize(width, height);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        for (LicenseType type : LicenseType.values()) {
            licenseTypeComboBox.addItem(type.toString());
        }

        addTruckButton.addActionListener(e -> addTruck());

        /*logoutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Logout Button Pressed");
            new LoginScreen();
            frame.dispose();
        });*/
    }

    private void addTruck() {
        String licenseNumber = licenseNumberTextField.getText();
        String model = modelTextField.getText();
        String weightString = weightTextField.getText();
        String maxWeightString = maxWeightTextField.getText();
        String licenseTypeString = String.valueOf(licenseTypeComboBox.getSelectedItem());

        if (licenseNumber.isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "Please enter license number");
            return;
        }
        if (model.isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "Please enter model");
            return;
        }
        if (weightString.isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "Please enter weight");
            return;
        }
        if (maxWeightString.isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "Please enter max weight");
            return;
        }
        if (licenseTypeString.isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "Please select license type");
            return;
        }

        int weight;
        try {
            weight = Integer.parseInt(weightString);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mainPanel, "Invalid weight: " + weightString);
            return;
        }
        int maxWeight;
        try {
            maxWeight = Integer.parseInt(maxWeightString);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mainPanel, "Invalid max weight: " + maxWeightString);
            return;
        }


        Response response = FactoryService.getInstance().getTruckService().addTruck(licenseNumber, model, weight, maxWeight, licenseTypeString);

        if (!response.hasError()) {
            JOptionPane.showMessageDialog(mainPanel, "Truck added successfully");
        } else {
            JOptionPane.showMessageDialog(mainPanel, response.getMessage());
        }
    }
}
