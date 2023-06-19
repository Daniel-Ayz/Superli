package HumanResources.GUI.EmployeeView;

import HumanResources.GUI.LoginScreen;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EmployeeMainScreen {
    private JPanel mainPanel;
    private JLabel welcomeLabel;
    private JButton viewShiftsButton;
    private JButton requestShiftButton;
    private JButton logoutButton;

    public EmployeeMainScreen(String employeeId) {
        welcomeLabel.setText("Welcome!");
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMMM d, yyyy");
        String currentDate = formatter.format(new Date());
        JLabel dateLabel = new JLabel(currentDate);
        dateLabel.setFont(new Font("Arial", Font.BOLD, 14));
        dateLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JFrame frame = new JFrame("Employee Main Screen");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


        viewShiftsButton.addActionListener(e -> {
            new EmployeeViewApprovedScreen(employeeId, new Date());
        });

        requestShiftButton.addActionListener(e -> {
            new EmployeeViewAvailableScreen(employeeId);
        });

        logoutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Logout Button Pressed");
            new LoginScreen();
            frame.dispose();
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
