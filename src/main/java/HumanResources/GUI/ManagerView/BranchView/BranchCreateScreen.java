package HumanResources.GUI.ManagerView.BranchView;

import HumanResources.ServiceLayer.BranchService;
import HumanResources.ServiceLayer.Response;
import HumanResources.ServiceLayer.ServiceFactory;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BranchCreateScreen {
    private JPanel mainPanel;
    private JTextField idTextField;
    private JTextField morningStartTimeTextField;
    private JTextField morningEndTimeTextField;
    private JTextField nightStartTimeTextField;
    private JTextField nightEndTimeTextField;
    private JButton addButton;


    public BranchCreateScreen() {
        JFrame frame = new JFrame("Add Branch");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


        addButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idTextField.getText());
                String morningStartTime = morningStartTimeTextField.getText();
                String morningEndTime = morningEndTimeTextField.getText();
                String nightStartTime = nightStartTimeTextField.getText();
                String nightEndTime = nightEndTimeTextField.getText();

                //change to be the method in HRDeliveryService
                addBranch(id, morningStartTime, morningEndTime, nightStartTime, nightEndTime);

                frame.dispose(); // Close the window after the branch has been added
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid branch ID.");
            }
        });

        idTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                idTextField.setText("");
            }
        });

        morningStartTimeTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                morningStartTimeTextField.setText("");
            }
        });

        morningEndTimeTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                morningEndTimeTextField.setText("");
            }
        });

        nightStartTimeTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                nightStartTimeTextField.setText("");
            }
        });

        nightEndTimeTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                nightEndTimeTextField.setText("");
            }
        });





    }
    private void addBranch(int id, String morningStartTime, String morningEndTime, String nightStartTime, String nightEndTime) {
        BranchService branchService = getBranchService();
        Response response = branchService.addBranch(id, morningStartTime, morningEndTime, nightStartTime, nightEndTime);
        JOptionPane.showMessageDialog(null, response.getData().toString());
    }

    private BranchService getBranchService() {
        return ServiceFactory.getInstance().getBranchService();
    }
}
