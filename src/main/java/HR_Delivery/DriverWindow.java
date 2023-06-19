package HR_Delivery;

import HumanResources.GUI.EmployeeView.EmployeeMainScreen;
import delivery.graphicFrontend.DriverMenuWindow;

import javax.swing.*;

public class DriverWindow {
    private JButton shiftsManagmentButton;
    private JPanel mainPanel;
    private JButton deliveryMenuButton;

    public DriverWindow(String employeeID) {
        JFrame frame = new JFrame("Driver Window");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();

        int width = 600;
        int height = 600;
        frame.setSize(width, height);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Add action listeners for buttons
        shiftsManagmentButton.addActionListener(e -> new EmployeeMainScreen(employeeID));
        deliveryMenuButton.addActionListener(e -> new DriverMenuWindow());
    }
}
