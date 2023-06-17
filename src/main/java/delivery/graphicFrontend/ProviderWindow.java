package delivery.graphicFrontend;

import delivery.backend.businessLayer.destination.ShipmentArea;
import delivery.backend.serviceLayer.FactoryService;
import delivery.tools.Response;

import javax.swing.*;

public class ProviderWindow {
    private JPanel mainPanel;
    private JTextField addressTextField;
    private JTextField phoneTextField;
    private JTextField contactNameTextField;
    private JTextField northCoordinateTextField;
    private JTextField eastCoordinateTextField;
    private JComboBox areaComboBox;
    private JButton addProviderButton;

    public ProviderWindow() {
        JFrame frame = new JFrame("Add Provider Window");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();

        int width = 600;
        int height = 400;
        frame.setSize(width, height);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        for (ShipmentArea area : ShipmentArea.values()) {
            areaComboBox.addItem(area.toString());
        }

        addProviderButton.addActionListener(e -> addProvider());
    }

    private void addProvider() {
        String address = addressTextField.getText();
        String phone = phoneTextField.getText();
        String contactName = contactNameTextField.getText();
        String area = String.valueOf(areaComboBox.getSelectedItem());
        String northCoordinateString = northCoordinateTextField.getText();
        String eastCoordinateString = eastCoordinateTextField.getText();

        if (address.isEmpty() || phone.isEmpty() || contactName.isEmpty() || northCoordinateString.isEmpty() || eastCoordinateString.isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "Please fill all fields");
            return;
        }

        int northCoordinate;
        try {
            northCoordinate = Integer.parseInt(northCoordinateString);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mainPanel, "Invalid north coordinate: " + northCoordinateString);
            return;
        }
        int eastCoordinate;
        try {
            eastCoordinate = Integer.parseInt(eastCoordinateString);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mainPanel, "Invalid east coordinate: " + eastCoordinateString);
            return;
        }


        Response response = FactoryService.getInstance().getDestinationService().addDestination(address, area, phone, contactName, northCoordinate, eastCoordinate, true);
        if (response.hasError())
            JOptionPane.showMessageDialog(mainPanel, response.getMessage());
        else
            JOptionPane.showMessageDialog(mainPanel, "Provider was added successfully");
    }
}
