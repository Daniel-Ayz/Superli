package HR_Delivery;

import HumanResources.GUI.EmployeeView.EmployeeMainScreen;
import HumanResources.GUI.LoginScreen;
import delivery.graphicFrontend.DriverMenuWindow;

import javax.swing.*;

import static HumanResources.GUI.LoginScreen.CreateLoginScreen;

public class DriverWindow {
    private JButton shiftsManagmentButton;
    private JPanel mainPanel;
    private JButton deliveryMenuButton;
    private JButton logoutButton;

    public DriverWindow(String employeeID) {
        JFrame frame = new JFrame("Driver Window");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        int width = 600;
        int height = 600;
        frame.setSize(width, height);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Add action listeners for buttons
        shiftsManagmentButton.addActionListener(e -> new EmployeeMainScreen(employeeID));
        deliveryMenuButton.addActionListener(e -> new DriverMenuWindow());
        logoutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Logout Button Pressed");
            CreateLoginScreen();
            frame.dispose();
        });
    }
}
