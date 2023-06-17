package delivery.graphicFrontend;

import delivery.backend.serviceLayer.FactoryService;
import delivery.tools.Response;

import javax.swing.*;

public class StoreVisionWindow {
    private JList storesList;
    private JPanel mainPanel;

    public StoreVisionWindow() {
        JFrame frame = new JFrame("Stores List");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();

        int width = 600;
        int height = 400;
        frame.setSize(width, height);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        Response response = FactoryService.getInstance().getDestinationService().getStores();

        if (response.hasError()) {
            JOptionPane.showMessageDialog(mainPanel, response.getMessage());
        } else {
            DefaultListModel<String> listModel = new DefaultListModel<>();
            Iterable<Object> stores = (Iterable<Object>) response.getData();
            for (Object store : stores) {
                listModel.addElement(store.toString());
            }
            storesList.setModel(listModel);
        }
    }
}
