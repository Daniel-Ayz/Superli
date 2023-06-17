package delivery.graphicFrontend;

import delivery.backend.serviceLayer.FactoryService;
import delivery.tools.Response;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class ShortageVisionWindow {
    private JList stockShortageList;
    private JPanel mainPanel;
    private JList supplyLeftoversList;

    public ShortageVisionWindow() {
        JFrame frame = new JFrame("Shortage Vision Window");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();

        int width = 600;
        int height = 400;
        frame.setSize(width, height);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        Response response = FactoryService.getInstance().getDeliveryHandlerService().viewStoreShortage();
        if(response.hasError())
            JOptionPane.showMessageDialog(mainPanel, response.getMessage());
        else{
            //set HashMap of <Integer, HashMap<Integer, Integer>> to JList
            DefaultListModel<String> listModel = new DefaultListModel<>();
            HashMap<Integer, HashMap<Integer, Integer>> storeShortage = (HashMap<Integer, HashMap<Integer, Integer>>) response.getData();
            for (Integer outerKey : storeShortage.keySet()) {
                listModel.addElement("Store ID: " + outerKey.toString() + "\n");

                HashMap<Integer, Integer> innerMap = storeShortage.get(outerKey);
                for (Map.Entry<Integer, Integer> value : innerMap.entrySet()) {
                    listModel.addElement("Item ID: " + value.getKey().toString() + ", Amount: " + value.getValue().toString() + "\n");
                }
                listModel.addElement("\n");
            }
            stockShortageList.setModel(listModel);
        }

        response = FactoryService.getInstance().getDeliveryHandlerService().viewProvidersLeftovers();
        if(response.hasError())
            JOptionPane.showMessageDialog(mainPanel, response.getMessage());
        else{
            //set HashMap of <Integer, HashMap<Integer, Integer>> to JList
            DefaultListModel<String> listModel = new DefaultListModel<>();
            HashMap<Integer, HashMap<Integer, Integer>> providersLeftovers = (HashMap<Integer, HashMap<Integer, Integer>>) response.getData();
            for (Integer outerKey : providersLeftovers.keySet()) {
                listModel.addElement("Provider ID: " + outerKey.toString() + "\n");

                HashMap<Integer, Integer> innerMap = providersLeftovers.get(outerKey);
                for (Map.Entry<Integer, Integer> value : innerMap.entrySet()) {
                    listModel.addElement("Item ID: " + value.getKey().toString() + ", Amount: " + value.getValue().toString() + "\n");
                }
                listModel.addElement("\n");
            }
            supplyLeftoversList.setModel(listModel);
        }
    }
}
