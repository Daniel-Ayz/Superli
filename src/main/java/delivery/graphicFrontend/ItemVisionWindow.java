package delivery.graphicFrontend;

import delivery.backend.serviceLayer.FactoryService;
import delivery.tools.Response;

import javax.swing.*;

public class ItemVisionWindow {
    private JList itemsList;
    private JPanel mainPanel;

    public ItemVisionWindow() {
        JFrame frame = new JFrame("Items List");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();

        int width = 600;
        int height = 400;
        frame.setSize(width, height);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        Response response = FactoryService.getInstance().getItemService().getItems();

        if (response.hasError()) {
            JOptionPane.showMessageDialog(mainPanel, response.getMessage());
        } else {
            DefaultListModel<String> listModel = new DefaultListModel<>();
            Iterable<Object> items = (Iterable<Object>) response.getData();
            for (Object item : items) {
                listModel.addElement(item.toString());
            }
            itemsList.setModel(listModel);
        }
    }
}
