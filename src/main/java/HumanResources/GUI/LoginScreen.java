package HumanResources.GUI;

import HR_Delivery.DriverWindow;
import HR_Delivery.HRDeliveryMainMenu;
import HumanResources.GUI.EmployeeView.EmployeeMainScreen;
import HumanResources.GUI.ManagerView.ManagerMainScreen;
import HumanResources.ServiceLayer.Response;
import HumanResources.ServiceLayer.ServiceFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import FaceRecognition.FaceRecognition;
import delivery.graphicFrontend.MenuWindow;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class LoginScreen {
    private JPanel mainPanel;
    private JTextField usernameTextField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JRadioButton employeeRadioButton;
    private JRadioButton managerRadioButton;
    private JButton faceRecognitionButton;
    private JRadioButton logisticManagerRadioButton;
    private ButtonGroup buttonGroup;

    private LoginScreen() {
        JFrame frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
                boolean isLogisticManager = logisticManagerRadioButton.isSelected();

                if (!isEmployee && !isManager && !isLogisticManager) {
                    JOptionPane.showMessageDialog(null, "Please select your role.");
                    return;
                }

                // TODO: Validate username and password
                if (isValidLogin(username, password, isEmployee, isManager, isLogisticManager)) {
                    if (isEmployee) {
                        Response res = ServiceFactory.getInstance().getEmployeeService().isDriver(username);
                        if(res.isSuccess())
                            new DriverWindow(username);
                        else
                            new EmployeeMainScreen(username);
                    }
                    else if(isLogisticManager){
                        new MenuWindow();
                    }
                    else {
                        new HRDeliveryMainMenu();
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
                    if (name.equals("ADMIN")) {
                        new HRDeliveryMainMenu();
                    }
                    else if(name.equals("LOGISTIC")){
                        new MenuWindow();
                    }
                    else {
                        Response res = ServiceFactory.getInstance().getEmployeeService().isDriver(name);
                        if(res.isSuccess())
                            new DriverWindow(name);
                        else
                            new EmployeeMainScreen(name);
                    }
                    frame.dispose(); // Close the login window
                }
            }
        });

        // Group the radio buttons
        buttonGroup = new ButtonGroup();
        buttonGroup.add(employeeRadioButton);
        buttonGroup.add(managerRadioButton);
        buttonGroup.add(logisticManagerRadioButton);

    }

    public static void CreateLoginScreen() {
        new LoginScreen();
        welcomeSound();
    }

    private static void welcomeSound(){
        new Thread(() -> { // Lambda Expression
            try {
                FileInputStream fileInputStream = new FileInputStream("welcome.mp3");
                Player player = new Player(fileInputStream);
                player.play();
                player.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (JavaLayerException e) {
                e.printStackTrace();
            }  catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

    private boolean isValidLogin(String username, char[] password, boolean isEmployee, boolean isManager, boolean isLogisticManager) {
        //TODO: REMOVE THIS, ONLY FOR TESTING
        if(true)
            return true;
        // For now, Manager has username "admin" and password "admin"
        if(isManager){
            return username.equals("admin") && new String(password).equals("admin");
        }
        else if(isLogisticManager){
            return username.equals("logistic") && new String(password).equals("logistic");
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
