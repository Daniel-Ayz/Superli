package HR_Delivery;

import HumanResources.GUI.LoginScreen;
import HumanResources.ServiceLayer.ServiceFactory;
import delivery.backend.serviceLayer.FactoryService;
import delivery.graphicFrontend.MenuWindow;

import javax.swing.*;

public class HRDeliveryMainMenu {
    private JButton humanResourcesMenuButton;
    private JPanel mainPanel;
    private JButton deliveryMenuButton;
    private JButton addBranchButton;
    private JButton initializeWithBaseDataButton;

    public HRDeliveryMainMenu() {
        JFrame frame = new JFrame("Choose Menu");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        int width = 600;
        int height = 400;
        frame.setSize(width, height);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        humanResourcesMenuButton.addActionListener(e -> new LoginScreen());
        deliveryMenuButton.addActionListener(e -> new MenuWindow());
        addBranchButton.addActionListener(e -> new AddBranchWindow());
        initializeWithBaseDataButton.addActionListener(e -> InitializeWithData());
    }

    private void InitializeWithData() {
        FactoryService.clearDeliveryDB();
        ServiceFactory.clearHRDB();
        ServiceFactory.initHR();
        FactoryService.initDelivery();
        JOptionPane.showMessageDialog(null, "Initialized Successfully!");
    }
}
