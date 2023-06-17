package HumanResources.GUI.ManagerView;

import HumanResources.GUI.LoginScreen;
import HumanResources.GUI.ManagerView.BranchView.BranchMainScreen;
import HumanResources.GUI.ManagerView.EmployeeControlView.EmployeeControlMainScreen;
import HumanResources.GUI.ManagerView.ShiftView.ShiftMainScreen;

import javax.swing.*;

public class ManagerMainScreen extends JFrame {
    private JPanel mainPanel;
    private JButton addBranchButton;
    private JButton addShiftButton;
//    private JTable shiftsTable;
    private JButton logoutButton;
    private JButton employeeControlMainScreenButtonButton;

    public ManagerMainScreen() {
        JFrame frame = new JFrame("Manager Main Screen");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        int width = 600;
        int height = 400;
        frame.setSize(width, height);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Add action listeners for buttons
        addBranchButton.addActionListener(e -> addBranch());
        addShiftButton.addActionListener(e -> addShift());
        employeeControlMainScreenButtonButton.addActionListener(e -> employeeControlMainScreenButtonButton());



        logoutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Logout Button Pressed");
            new LoginScreen();
            frame.dispose();
        });
    }

    private void addBranch() {
        new BranchMainScreen();
    }

    private void addShift() {
        new ShiftMainScreen();
    }

    private void employeeControlMainScreenButtonButton() {
        new EmployeeControlMainScreen();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}

