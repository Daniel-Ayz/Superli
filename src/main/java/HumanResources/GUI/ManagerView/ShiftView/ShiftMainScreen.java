package HumanResources.GUI.ManagerView.ShiftView;

import javax.swing.*;

public class ShiftMainScreen {
    private JPanel mainPanel;
    private JButton addShiftButton;
    private JButton editShiftButton;

    public ShiftMainScreen() {
        JFrame frame = new JFrame("Shift Main Screen");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setSize(400,200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Add action listeners for buttons
        addShiftButton.addActionListener(e -> addShift());
        editShiftButton.addActionListener(e -> editShift());
    }

    private void addShift() {
        new ShiftCreateScreen();
    }

    private void editShift() {
        new ShiftEditScreen();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
