package HumanResources.GUI.ManagerView.EmployeeControlView;

import javax.swing.*;

public class EmployeeControlMainScreen {
    private JPanel mainPanel;
    private JButton addEmployeeButton;
    private JButton editEmployeeButton;

    public EmployeeControlMainScreen() {
        JFrame frame = new JFrame("Employee Control Main Screen");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setSize(400,200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Add action listeners for buttons
        addEmployeeButton.addActionListener(e -> addEmployee());
        editEmployeeButton.addActionListener(e -> editEmployee());
    }

    private void addEmployee() {
        new EmployeeCreateScreen();
    }

    private void editEmployee() {
        new EmployeeEditScreen();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
