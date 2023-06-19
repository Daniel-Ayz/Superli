package delivery.graphicFrontend;

import javax.swing.*;

public class DriverMenuWindow {
    private JButton showOrdersButton;
    private JPanel mainPanel;
    private JButton setTruckWeightButton;
    private JButton showExpectedTimeButton;
    private JButton reportDistributionDelayButton;

    public DriverMenuWindow() {
        JFrame frame = new JFrame("Driver Menu Window");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();

        int width = 600;
        int height = 600;
        frame.setSize(width, height);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Add action listeners for buttons
        showOrdersButton.addActionListener(e -> new OrderVisionWindow());
        setTruckWeightButton.addActionListener(e -> new WeightWindow());
        showExpectedTimeButton.addActionListener(e -> new ExpectedTimeVisionWindow());
        reportDistributionDelayButton.addActionListener(e -> new DistributionDelayWindow());
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
