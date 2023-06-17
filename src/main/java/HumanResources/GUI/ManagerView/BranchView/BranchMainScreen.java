package HumanResources.GUI.ManagerView.BranchView;


import javax.swing.*;

public class BranchMainScreen {
    private JPanel mainPanel;
    private JButton addBranchButton;
    private JButton viewBranchButton;


    public BranchMainScreen() {
        JFrame frame = new JFrame("Branch Main Screen");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setSize(400,200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Add action listeners for buttons
        addBranchButton.addActionListener(e -> addBranch());
        viewBranchButton.addActionListener(e -> editBranch());
    }

    private void addBranch() {
        new BranchCreateScreen();
    }

    private void editBranch() {
        new BranchEditScreen();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
