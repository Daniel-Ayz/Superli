package delivery.graphicFrontend;

import delivery.backend.serviceLayer.FactoryService;
import delivery.tools.Response;

import javax.swing.*;

public class ShortageWindow {
    private JButton fillShortageButton;
    private JPanel mainPanel;

    public ShortageWindow() {
        JFrame frame = new JFrame("Shortage Window");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();

        int width = 600;
        int height = 400;
        frame.setSize(width, height);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        fillShortageButton.addActionListener(e -> fillShortage());
    }

    private void fillShortage() {
        Response response = FactoryService.getInstance().getDeliveryHandlerService().handleShortage();
        if(response.hasError())
            JOptionPane.showMessageDialog(mainPanel, response.getMessage());

        else {
            JOptionPane.showMessageDialog(mainPanel, "Shortages were handled successfully");
        }
    }
}
