package delivery.graphicFrontend;

import delivery.backend.businessLayer.shipment.Shipment;
import delivery.backend.serviceLayer.FactoryService;
import delivery.tools.Response;

import javax.swing.*;
import java.util.Map;

public class OrderVisionWindow {
    private JList ordersList;
    private JPanel mainPanel;

    public OrderVisionWindow() {
        JFrame frame = new JFrame("Orders List");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();

        int width = 600;
        int height = 400;
        frame.setSize(width, height);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        Response response = FactoryService.getInstance().getDeliveryHandlerService().getOrders();

        if(response.hasError())
            JOptionPane.showMessageDialog(mainPanel, response.getMessage());
        else{
            DefaultListModel<String> listModel = new DefaultListModel<>();
            Object t = ((Map<Integer, Shipment>) response.getData());
            Map<Integer, Shipment> orders = (Map<Integer, Shipment>) response.getData();
            for (Map.Entry order : orders.entrySet()) {
                listModel.addElement(order.toString());
            }
            ordersList.setModel(listModel);
        }
    }
}
