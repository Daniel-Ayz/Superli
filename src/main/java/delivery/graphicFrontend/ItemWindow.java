package delivery.graphicFrontend;

import delivery.backend.serviceLayer.FactoryService;
import delivery.tools.Response;

import javax.swing.*;

public class ItemWindow {
    private JPanel mainPanel;
    private JTextField itemNameTextField;
    private JButton addItemButton;

    public ItemWindow() {
        JFrame frame = new JFrame("Add Item Window");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();

        int width = 600;
        int height = 400;
        frame.setSize(width, height);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        addItemButton.addActionListener(e -> addItem());
    }

    private void addItem() {
        String itemName = itemNameTextField.getText();
        if (itemName.isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "Please enter item name");
            return;
        }

        Response response = FactoryService.getInstance().getItemService().addItem(itemName);
        if (response.hasError())
            JOptionPane.showMessageDialog(mainPanel, response.getMessage());
        else
            JOptionPane.showMessageDialog(mainPanel, "Item was added successfully");
    }
}
