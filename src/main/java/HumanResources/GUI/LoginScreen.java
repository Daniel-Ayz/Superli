package HumanResources.GUI;

import HumanResources.GUI.EmployeeView.EmployeeMainScreen;
import HumanResources.GUI.ManagerView.ManagerMainScreen;
import HumanResources.ServiceLayer.Response;
import HumanResources.ServiceLayer.ServiceFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import FaceRecognition.FaceRecognition;

public class LoginScreen {
    private JPanel mainPanel;
    private JTextField usernameTextField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JRadioButton employeeRadioButton;
    private JRadioButton managerRadioButton;
    private JButton faceRecognitionButton;
    private ButtonGroup buttonGroup;

    public LoginScreen() {
        JFrame frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(mainPanel);
        frame.pack();

        int width = 300;
        int height = 200;
        frame.setSize(width, height);

        frame.setLocationRelativeTo(null); // Center the frame
        frame.setVisible(true);

        // Add listener to login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameTextField.getText();
                char[] password = passwordField.getPassword();
                boolean isEmployee = employeeRadioButton.isSelected();
                boolean isManager = managerRadioButton.isSelected();

                if (!isEmployee && !isManager) {
                    JOptionPane.showMessageDialog(null, "Please select your role.");
                    return;
                }

                // TODO: Validate username and password
                if (isValidLogin(username, password, isEmployee, isManager)) {
                    if (isEmployee) {
                        new EmployeeMainScreen(username);
                    } else {
                        new ManagerMainScreen();
                    }
                    frame.dispose(); // Close the login window
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password.");
                }

            }
        });

        // Add listener to face recognition button
        faceRecognitionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = FaceRecognition.recognizeFaces();
                if (name.equals("ERROR")) {
                    JOptionPane.showMessageDialog(null, "Face not recognized.");
                } else {
//                    boolean isEmployee = employeeRadioButton.isSelected();
                    usernameTextField.setText(name);
                    if (!name.equals("ADMIN")) {
                        new EmployeeMainScreen(name);
                    } else {
                        new ManagerMainScreen();
                    }
                    frame.dispose(); // Close the login window
                }
            }
        });

        // Group the radio buttons
        buttonGroup = new ButtonGroup();
        buttonGroup.add(employeeRadioButton);
        buttonGroup.add(managerRadioButton);
    }

    private boolean isValidLogin(String username, char[] password, boolean isEmployee, boolean isManager) {
        //TODO: REMOVE THIS, ONLY FOR TESTING
        if(true)
            return true;
        // For now, Manager has username "admin" and password "admin"
        if(isManager){
            return username.equals("admin") && new String(password).equals("admin");
        }
        else if (!username.isEmpty() && password.length > 0){
            Response response = ServiceFactory.getInstance().getEmployeeService().checkPassword(username, new String(password));
            return response.isSuccess();
        }
        else {
            return false;
        }
    }
}
