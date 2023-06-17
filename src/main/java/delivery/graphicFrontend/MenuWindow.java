package delivery.graphicFrontend;

import javax.swing.*;

public class MenuWindow {
    private JPanel mainPanel;

    private JButton addTruckButton;
    private JButton addItemButton;
    //private JButton addStoreButton;
    private JButton addProviderButton;
    private JButton showTrucksButton;
    private JButton showItemsButton;
    private JButton showProvidersButton;
    private JButton makeOrderButton;
    private JButton setTruckWeightButton;
    private JButton fillShortageButton;
    private JButton showShortagesButton;
    private JButton showOrdersButton;
    private JButton showExpectedTimeButton;
    private JButton reportDistributionDelayButton;
    private JButton showStoresButton;

    public MenuWindow() {
        JFrame frame = new JFrame("Menu Window");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();

        int width = 600;
        int height = 600;
        frame.setSize(width, height);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Add action listeners for buttons
        addTruckButton.addActionListener(e -> new TruckWindow());
        addItemButton.addActionListener(e -> new ItemWindow());
        //addStoreButton.addActionListener(e -> addStore());
        addProviderButton.addActionListener(e -> new ProviderWindow());
        showTrucksButton.addActionListener(e -> new TruckVisionWindow());
        showItemsButton.addActionListener(e -> new ItemVisionWindow());
        showProvidersButton.addActionListener(e -> new ProviderVisionWindow());
        showStoresButton.addActionListener(e -> new StoreVisionWindow());
        makeOrderButton.addActionListener(e -> new OrderWindow());
        setTruckWeightButton.addActionListener(e -> new WeightWindow());
        fillShortageButton.addActionListener(e -> new ShortageWindow());
        showShortagesButton.addActionListener(e -> new ShortageVisionWindow());
        showOrdersButton.addActionListener(e -> new OrderVisionWindow());
        showExpectedTimeButton.addActionListener(e -> new ExpectedTimeVisionWindow());
        reportDistributionDelayButton.addActionListener(e -> new DistributionDelayWindow());




        /*logoutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Logout Button Pressed");
            new LoginScreen();
            frame.dispose();
        });*/
    }


    public JPanel getMainPanel() {
        return mainPanel;
    }
}
