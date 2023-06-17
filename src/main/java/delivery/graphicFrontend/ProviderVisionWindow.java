package delivery.graphicFrontend;

import delivery.backend.serviceLayer.FactoryService;
import delivery.tools.Response;

import javax.swing.*;

public class ProviderVisionWindow {
    private JList providersList;
    private JPanel mainPanel;

    public ProviderVisionWindow() {
        JFrame frame = new JFrame("Providers List");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();

        int width = 600;
        int height = 400;
        frame.setSize(width, height);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        Response response = FactoryService.getInstance().getDestinationService().getProviders();

        if (response.hasError()) {
            JOptionPane.showMessageDialog(mainPanel, response.getMessage());
        } else {
            DefaultListModel<String> listModel = new DefaultListModel<>();
            Iterable<Object> providers = (Iterable<Object>) response.getData();
            for (Object provider : providers) {
                listModel.addElement(provider.toString());
            }
            providersList.setModel(listModel);
        }
    }
}
