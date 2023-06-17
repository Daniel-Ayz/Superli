package delivery.graphicFrontend;

import delivery.backend.businessLayer.shipment.Treatment;
import delivery.backend.serviceLayer.FactoryService;
import delivery.tools.Response;

import javax.swing.*;

public class TreatmentWindow {
    private JComboBox treatmentComboBox;
    private JPanel mainPanel;
    private JButton setTreatmentButton;

    private int shipmentId;
    private int supplierId;

    public TreatmentWindow(int shipmentId, int supplierId) {
        this.shipmentId = shipmentId;
        this.supplierId = supplierId;

        JFrame frame = new JFrame("Treatment Window");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();

        int width = 600;
        int height = 400;
        frame.setSize(width, height);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        for (Treatment treatment : Treatment.values()) {
            treatmentComboBox.addItem(treatment.toString());
        }

        setTreatmentButton.addActionListener(e -> setTreatment());
    }

    private void setTreatment() {
        String treatmentString = String.valueOf(treatmentComboBox.getSelectedItem());

        int treatmentNum;
        switch (treatmentString) {
            case "DROPPING_OR_SWITCHING_DESTINATION":
                treatmentNum = 1;
                break;
            case "SWITCHING_TRUCK":
                treatmentNum = 2;
                break;
            case "DROPPING_ITEMS":
                treatmentNum = 3;
                break;
            default:
                treatmentNum = 0;
        }

        Response response = FactoryService.getInstance().getDeliveryHandlerService().handleError(shipmentId, treatmentNum, supplierId);
        if (response.hasError()) {
            JOptionPane.showMessageDialog(mainPanel, response.getMessage());
            return;
        }

        JOptionPane.showMessageDialog(mainPanel, "Treatment set to " + treatmentString);
    }
}
