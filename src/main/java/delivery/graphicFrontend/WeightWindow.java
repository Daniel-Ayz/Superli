package delivery.graphicFrontend;

import delivery.backend.serviceLayer.FactoryService;
import delivery.tools.Response;

import javax.swing.*;

public class WeightWindow {
    private JTextField shipmentIdTextField;
    private JPanel mainPanel;
    private JTextField supplierIdTextField;
    private JTextField weightTextField;
    private JButton setWeightButton;

    public WeightWindow() {
        JFrame frame = new JFrame("Set Weight Window");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();

        int width = 600;
        int height = 400;
        frame.setSize(width, height);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        setWeightButton.addActionListener(e -> setWeight());
    }

    private void setWeight() {
        String shipmentIdString = shipmentIdTextField.getText();
        String supplierIdString = supplierIdTextField.getText();
        String weightString = weightTextField.getText();

        int shipmentId;
        try {
            shipmentId = Integer.parseInt(shipmentIdString);
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(mainPanel, "Shipment ID must be an integer");
            return;
        }

        int supplierId;
        try {
            supplierId = Integer.parseInt(supplierIdString);
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(mainPanel, "Supplier ID must be an integer");
            return;
        }

        int weight;
        try {
            weight = Integer.parseInt(weightString);
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(mainPanel, "Weight must be an integer");
            return;
        }

        Response response = FactoryService.getInstance().getDeliveryHandlerService().setWeight(shipmentId, supplierId, weight);
        if (response.hasError()) {
            JOptionPane.showMessageDialog(mainPanel, response.getMessage());
            return;
        }


        response = FactoryService.getInstance().getDeliveryHandlerService().isOverWeighted(shipmentId);

        if (response.hasError()) {
            JOptionPane.showMessageDialog(mainPanel, response.getMessage());
            return;
        }
        else if((boolean) response.getData()) {
            new TreatmentWindow(shipmentId, supplierId);
        }
        else {
            JOptionPane.showMessageDialog(mainPanel, "Truck Weight of order " + shipmentId + " at supplier " + supplierId + " was set successfully");
        }


    }
}
