package delivery.graphicFrontend;

import delivery.backend.serviceLayer.FactoryService;
import delivery.tools.Response;

import javax.swing.*;

public class TruckVisionWindow {
    private JList truckList;
    private JPanel mainPanel;

    public TruckVisionWindow() {
        JFrame frame = new JFrame("Truck Vision Window");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();

        int width = 600;
        int height = 400;
        frame.setSize(width, height);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        Response response = FactoryService.getInstance().getTruckService().getAvailableTrucks();

        if (response.hasError()) {
            JOptionPane.showMessageDialog(mainPanel, response.getMessage());
        } else {
            DefaultListModel<String> listModel = new DefaultListModel<>();
            Iterable<Object> trucks = (Iterable<Object>) response.getData();
            for (Object truck : trucks) {
                listModel.addElement(truck.toString());
            }
            truckList.setModel(listModel);
        }
    }
}
