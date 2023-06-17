package delivery.graphicFrontend;

import delivery.backend.serviceLayer.FactoryService;
import delivery.tools.Response;

import javax.swing.*;

public class ExpectedTimeVisionWindow {
    private JTextField shipmentIdTextField;
    private JPanel mainPanel;
    private JTextField storeIdTextField;
    private JButton showExpectedTimeButton;

    public ExpectedTimeVisionWindow() {
        JFrame frame = new JFrame("Expected Time Vision Window");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();

        int width = 600;
        int height = 400;
        frame.setSize(width, height);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        showExpectedTimeButton.addActionListener(e -> showExpectedTime());
    }

    private void showExpectedTime() {
        String shipmentIdString = shipmentIdTextField.getText();
        String storeIdString = storeIdTextField.getText();

        int shipmentId;
        try {
            shipmentId = Integer.parseInt(shipmentIdString);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(mainPanel, "Shipment id must be an integer");
            return;
        }

        int storeId;
        try {
            storeId = Integer.parseInt(storeIdString);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(mainPanel, "Store id must be an integer");
            return;
        }

        Response response = FactoryService.getInstance().getDeliveryHandlerService().getExpectedTime(shipmentId, storeId);

        if (response.hasError()) {
            JOptionPane.showMessageDialog(mainPanel, response.getMessage());
        } else {
            JOptionPane.showMessageDialog(mainPanel, "Expected time: " + response.getData());
        }
    }
}
