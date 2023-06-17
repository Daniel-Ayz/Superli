package HumanResources.GUI.ManagerView.BranchView;

import HumanResources.BusinessLayer.BranchModule.Branch;
import HumanResources.ServiceLayer.BranchService;
import HumanResources.ServiceLayer.Response;
import HumanResources.ServiceLayer.ServiceFactory;

import javax.swing.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BranchEditScreen {

    private JPanel mainPanel;
    private JList branchesList;
    //    private JButton selectBranchButton;

    public BranchEditScreen() {
        JFrame frame = new JFrame("Branch Edit Screen");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(mainPanel);
        frame.pack();

        int width = 1200;
        int height = 400;
        frame.setSize(width, height);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Get branches from service layer
        Response response = getBranchService().getAllBranches();
        if (!response.isSuccess()) {
            JOptionPane.showMessageDialog(mainPanel, response.getData().toString());
            return;
        }
        List<Branch> branches = ((Map<Integer, Branch>) response.getData()).values().stream().toList();

        // Populate shifts list in format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<String> branchStrings = new ArrayList<>();
        for (Branch branch : branches) {
            branchStrings.add(branch.toString());
        }
        branchesList.setListData(branchStrings.toArray(new String[0]));


        // Add action listener for select shift button
        // selectShiftButton.addActionListener(e -> selectShift(shifts));
    }

//    private Shift selectShiftFromList(List<Shift> shifts){
//        int selectedIndex = shiftsList.getSelectedIndex();
//        if (selectedIndex >= 0) {
//            return shifts.get(selectedIndex);
//        } else {
//            return null;
//        }
//    }

//    private void selectShift(List<Shift> shifts) {
//        // Get selected shift from list
//
//        Shift selectedShift = selectShiftFromList(shifts);
//        if (selectedShift == null) {
//            JOptionPane.showMessageDialog(mainPanel, "Please select a shift.");
//            return;
//        }
//
//        // Open shift detail screen for selected shift
//        new ShiftDetailScreen(selectedShift);
//    }

    public BranchService getBranchService() {
        return ServiceFactory.getInstance().getBranchService();
    }
}
