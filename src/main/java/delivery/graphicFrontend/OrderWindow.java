package delivery.graphicFrontend;

import delivery.backend.businessLayer.destination.Provider;
import delivery.backend.businessLayer.destination.Store;
import delivery.backend.businessLayer.item.Item;
import delivery.backend.serviceLayer.FactoryService;
import delivery.tools.Response;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class OrderWindow {
    private JPanel mainPanel;
    private JTextField storeIdTextField;
    private JTextField itemIdStockTextField;
    private JTextField amountStockTextField;
    private JTextField providerTextField;
    private JTextField itemIdSupplyTextField;
    private JTextField amountSupplyTextField;
    private JButton addItemStockButton;
    private JButton addItemSupplyButton;
    private JButton makeOrderButton;

    private Map<Integer, Map<Integer, Integer>> stockOrder = new HashMap<>();
    private Map<Integer, Map<Integer, Integer>> supplyOrder = new HashMap<>();

    public OrderWindow() {
        JFrame frame = new JFrame("Order Window");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();

        int width = 600;
        int height = 600;
        frame.setSize(width, height);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        addItemStockButton.addActionListener(e -> addItemStock());
        addItemSupplyButton.addActionListener(e -> addItemSupply());
        makeOrderButton.addActionListener(e -> makeOrder());
    }

    private void makeOrder() {
        Response response = FactoryService.getInstance().getDeliveryHandlerService().handleOrder(stockOrder, supplyOrder);

        if (response.hasError()) {
            JOptionPane.showMessageDialog(mainPanel, response.getMessage());
        } else {
            JOptionPane.showMessageDialog(mainPanel, "Order made successfully");
        }
    }

    private void addItemSupply() {
        String providerString = providerTextField.getText();
        String itemIdString = itemIdSupplyTextField.getText();
        String amountString = amountSupplyTextField.getText();

        if (providerString.isEmpty() || itemIdString.isEmpty() || amountString.isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "Please fill all slots");
            return;
        }

        int providerId;
        try {
            providerId = Integer.parseInt(providerString);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(mainPanel, "Provider ID must be a number");
            return;
        }

        int itemId;
        try {
            itemId = Integer.parseInt(itemIdString);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(mainPanel, "Item ID must be a number");
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(amountString);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(mainPanel, "Amount must be a number");
            return;
        }

        Response responseProviders = FactoryService.getInstance().getDestinationService().getProviders();
        if (responseProviders.hasError()) {
            JOptionPane.showMessageDialog(mainPanel, responseProviders.getMessage());
            return;
        }

        Iterable<Provider> providers = (Iterable<Provider>) responseProviders.getData();
        boolean providerExists = false;
        for (Provider provider : providers) {
            if (provider.getId() == providerId) {
                providerExists = true;
                break;
            }
        }

        if (!providerExists) {
            JOptionPane.showMessageDialog(mainPanel, "Provider ID does not exist");
            return;
        }

        Response responseItems = FactoryService.getInstance().getItemService().getItems();
        if (responseItems.hasError()) {
            JOptionPane.showMessageDialog(mainPanel, responseItems.getMessage());
            return;
        }

        Iterable<Item> allItems = (Iterable<Item>) responseItems.getData();
        boolean itemExists = false;
        for (Item item : allItems) {
            if (item.getId() == itemId) {
                itemExists = true;
                break;
            }
        }

        if (!itemExists) {
            JOptionPane.showMessageDialog(mainPanel, "Item ID does not exist");
            return;
        }

        if (stockOrder.containsKey(providerId)) {
            Map<Integer, Integer> items = stockOrder.get(providerId);
            if (items.containsKey(itemId)) {
                items.put(itemId, items.get(itemId) + amount);
            } else {
                items.put(itemId, amount);
            }
        } else {
            Map<Integer, Integer> items = new HashMap<>();
            items.put(itemId, amount);
            supplyOrder.put(providerId, items);
        }

        JOptionPane.showMessageDialog(mainPanel, "Item: " + itemId + " added to supply order of provider: " + providerId + " successfully");
    }

    private void addItemStock() {
        String storeIdString = storeIdTextField.getText();
        String itemIdString = itemIdStockTextField.getText();
        String amountString = amountStockTextField.getText();

        if (storeIdString.isEmpty() || itemIdString.isEmpty() || amountString.isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "Please fill all slots");
            return;
        }

        int storeId;
        try {
            storeId = Integer.parseInt(storeIdString);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(mainPanel, "Store ID must be a number");
            return;
        }

        int itemId;
        try {
            itemId = Integer.parseInt(itemIdString);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(mainPanel, "Item ID must be a number");
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(amountString);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(mainPanel, "Amount must be a number");
            return;
        }

        Response responseStores = FactoryService.getInstance().getDestinationService().getStores();
        if (responseStores.hasError()) {
            JOptionPane.showMessageDialog(mainPanel, responseStores.getMessage());
            return;
        }

        Iterable<Store> stores = (Iterable<Store>) responseStores.getData();
        boolean storeExists = false;
        for (Store store : stores) {
            if (store.getId() == storeId) {
                storeExists = true;
                break;
            }
        }

        if (!storeExists) {
            JOptionPane.showMessageDialog(mainPanel, "Store ID does not exist");
            return;
        }

        Response responseItems = FactoryService.getInstance().getItemService().getItems();
        if (responseItems.hasError()) {
            JOptionPane.showMessageDialog(mainPanel, responseItems.getMessage());
            return;
        }

        Iterable<Item> allItems = (Iterable<Item>) responseItems.getData();
        boolean itemExists = false;
        for (Item item : allItems) {
            if (item.getId() == itemId) {
                itemExists = true;
                break;
            }
        }

        if (!itemExists) {
            JOptionPane.showMessageDialog(mainPanel, "Item ID does not exist");
            return;
        }

        if (stockOrder.containsKey(storeId)) {
            Map<Integer, Integer> items = stockOrder.get(storeId);
            if (items.containsKey(itemId)) {
                items.put(itemId, items.get(itemId) + amount);
            } else {
                items.put(itemId, amount);
            }
        } else {
            Map<Integer, Integer> items = new HashMap<>();
            items.put(itemId, amount);
            stockOrder.put(storeId, items);
        }

        JOptionPane.showMessageDialog(mainPanel, "Item: " + itemId + " added to stock order of store: " + storeId + " successfully");
    }
}
