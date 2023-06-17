package HR_Delivery;

import delivery.backend.businessLayer.destination.ShipmentArea;
import HumanResources.ServiceLayer.Response;

import javax.swing.*;

public class AddBranchWindow {
    private JPanel mainPanel;
    private JTextField addressTextField;
    private JTextField phoneTextField;
    private JTextField contactNameTextField;
    private JTextField northCoordinateTextField;
    private JTextField eastCoordinateTextField;
    private JComboBox areaComboBox;
    private JButton addBranchButton;
    private JTextField idTextField;
    private JTextField morningStartTimeTextField;
    private JTextField morningEndTimeTextField;
    private JTextField nightStartTimeTextField;
    private JTextField nightEndTimeTextField;

    public AddBranchWindow() {
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

        addBranchButton.addActionListener(e -> addbranch());
    }

    private void addbranch() {
        String address = addressTextField.getText();
        String phone = phoneTextField.getText();
        String contactName = contactNameTextField.getText();
        String area = String.valueOf(areaComboBox.getSelectedItem());
        String northCoordinateString = northCoordinateTextField.getText();
        String eastCoordinateString = eastCoordinateTextField.getText();
        String idString = idTextField.getText();
        String morningStartTimeString = morningStartTimeTextField.getText();
        String morningEndTimeString = morningEndTimeTextField.getText();
        String nightStartTimeString = nightStartTimeTextField.getText();
        String nightEndTimeString = nightEndTimeTextField.getText();

        if (address.isEmpty() || phone.isEmpty() || contactName.isEmpty() || northCoordinateString.isEmpty() || eastCoordinateString.isEmpty() || idString.isEmpty() || morningStartTimeString.isEmpty() || morningEndTimeString.isEmpty() || nightStartTimeString.isEmpty() || nightEndTimeString.isEmpty()) {
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
        int id;
        try {
            id = Integer.parseInt(idString);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mainPanel, "Invalid id: " + idString);
            return;
        }


        Response response = (new HrDeliveryService()).addBranch(id, morningStartTimeString, morningEndTimeString, nightStartTimeString, nightEndTimeString, address, area, phone, contactName, northCoordinate, eastCoordinate);
        if (!response.isSuccess())
            JOptionPane.showMessageDialog(mainPanel, "Error Creating Branch");
        else
            JOptionPane.showMessageDialog(mainPanel, "branch was added successfully");
    }
}
