package HumanResources.GUI.EmployeeView;

import HumanResources.BusinessLayer.ShiftModule.Shift;
import HumanResources.ServiceLayer.Response;
import HumanResources.ServiceLayer.ServiceFactory;

import javax.swing.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmployeeViewApprovedScreen {

    private JPanel mainPanel;
    private JLabel titleLabel;
    private JList shiftsList;
    private JButton backButton;

    private final String employeeId;
    private final Date date;

    public EmployeeViewApprovedScreen(String employeeId, Date date) {
        this.employeeId = employeeId;
        this.date = date;
        initUI();
        loadData();
    }

    private void initUI() {
        JFrame frame = new JFrame("Employee View Approved Screen");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(mainPanel);
        frame.pack();

        int width = 800;
        int height = 600;
        frame.setSize(width, height);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        backButton.addActionListener(e -> frame.dispose());
    }

    private void loadData() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        titleLabel.setText(String.format("Approved Shifts for Employee %s from %s", employeeId, dateFormat.format(date)));

        // Get approved shifts from service layer
        Response response = ServiceFactory.getInstance().getShiftServiceEmployee().getAllShiftsApprovedFromDate(date, employeeId);
        if (!response.isSuccess()) {
            JOptionPane.showMessageDialog(mainPanel, response.getData().toString());
            return;
        }
        List<Shift> shifts = (List<Shift>) response.getData();

        // Populate shifts list in format
        List<String> shiftStrings = new ArrayList<>();
        for (Shift shift : shifts) {
            String shiftString = String.format("Shift #%s: Date: %s %s Branch: %s", shift.getShiftId(), dateFormat.format(shift.getDate()), shift.getShiftType(), shift.getBranchId());
            shiftStrings.add(shiftString);
        }
        shiftsList.setListData(shiftStrings.toArray(new String[0]));
    }
}
