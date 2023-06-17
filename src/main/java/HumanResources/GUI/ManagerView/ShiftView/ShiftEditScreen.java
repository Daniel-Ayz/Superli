package HumanResources.GUI.ManagerView.ShiftView;

import HumanResources.BusinessLayer.ShiftModule.Shift;
import HumanResources.ServiceLayer.Response;
import HumanResources.ServiceLayer.ServiceFactory;
import HumanResources.ServiceLayer.ShiftServiceManagement;

import javax.swing.*;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ShiftEditScreen {

    private JPanel mainPanel;
    private JList shiftsList;
    private JButton selectShiftButton;
//    private List<Shift> shifts;

    public ShiftEditScreen() {
        JFrame frame = new JFrame("Shift Edit Screen");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(mainPanel);
        frame.pack();

        int width = 1200;
        int height = 400;
        frame.setSize(width, height);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Get upcoming shifts from service layer
        Response response = getShiftServiceManagement().getUpcomingShifts();
        if (!response.isSuccess()) {
            JOptionPane.showMessageDialog(mainPanel, response.getData().toString());
            return;
        }
        List<Shift> shifts = (List<Shift>) response.getData();
        shifts.sort((s1, s2) -> s1.getDate().compareTo(s2.getDate()));

        // Populate shifts list in format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<String> shiftStrings = new ArrayList<>();
        for (Shift shift : shifts) {
            String dateString = formatter.format(shift.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            String shiftString = String.format("Shift #%s: Date: %s %s Branch: %s", shift.getShiftId(), dateString, shift.getShiftType(), shift.getBranchId());
            shiftStrings.add(shiftString);
        }
        shiftsList.setListData(shiftStrings.toArray(new String[0]));


//        shiftsList.setListData(shifts.toArray(new Shift[0]));

        // Add action listener for select shift button
        selectShiftButton.addActionListener(e -> selectShift(shifts));
    }

    private Shift selectShiftFromList(List<Shift> shifts){
        int selectedIndex = shiftsList.getSelectedIndex();
        if (selectedIndex >= 0) {
            return shifts.get(selectedIndex);
        } else {
            return null;
        }
    }

    private void selectShift(List<Shift> shifts) {
        // Get selected shift from list

        Shift selectedShift = selectShiftFromList(shifts);
        if (selectedShift == null) {
            JOptionPane.showMessageDialog(mainPanel, "Please select a shift.");
            return;
        }

        // Open shift detail screen for selected shift
        new ShiftDetailScreen(selectedShift);
    }

    public ShiftServiceManagement getShiftServiceManagement() {
        return ServiceFactory.getInstance().getShiftServiceManagement();
    }
}

