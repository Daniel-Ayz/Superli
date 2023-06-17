package delivery.graphicFrontend;

import delivery.backend.serviceLayer.FactoryService;
import delivery.tools.Response;

import javax.swing.*;

public class DistributionDelayWindow {
    private JTextField shipmentIdTextField;
    private JPanel mainPanel;
    private JTextField storeIdTextField;
    private JTextField delayTextField;
    private JButton reportDelayButton;

    public DistributionDelayWindow() {
        JFrame frame = new JFrame("Distribution Delay Window");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();

        int width = 600;
        int height = 400;
        frame.setSize(width, height);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        reportDelayButton.addActionListener(e -> reportDelay());
    }

    private void reportDelay() {
        String shipmentIdString = shipmentIdTextField.getText();
        String storeIdString = storeIdTextField.getText();
        String delayString = delayTextField.getText();

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

        int delay;
        try {
            delay = Integer.parseInt(delayString);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(mainPanel, "Delay must be an integer");
            return;
        }

        if (delay < 0) {
            JOptionPane.showMessageDialog(mainPanel, "Delay must be a positive integer");
            return;
        }

        Response response = FactoryService.getInstance().getDeliveryHandlerService().delayDistribution(shipmentId, storeId, delay);
        if (response.hasError()) {
            JOptionPane.showMessageDialog(mainPanel, response.getMessage());
            return;
        }

        JOptionPane.showMessageDialog(mainPanel, "Delay reported successfully");
    }
}
