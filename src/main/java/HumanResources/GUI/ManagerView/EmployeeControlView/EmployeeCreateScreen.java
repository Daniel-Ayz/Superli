package HumanResources.GUI.ManagerView.EmployeeControlView;

import HR_Delivery.LicenseType;
import HumanResources.BusinessLayer.EmployeeModule.Role;
import HumanResources.ServiceLayer.EmployeeService;
import HumanResources.ServiceLayer.Response;
import HumanResources.ServiceLayer.ServiceFactory;
import MailVerification.JavaMailUtil;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EmployeeCreateScreen extends JFrame{
    private JPanel mainPanel;
    private JTextField nameTextField;
    private JTextField idTextField;
    private JTextField personalInfoTextField;
    private JTextField bankNameTextField;
    private JTextField bankAccountNumberTextField;
    private JSpinner startDateSpinner;
    private JTextArea termsAndConditionsTextArea;
    private JTextField salaryTextField;
    private JButton addEmployeeButton;
    private JLabel nameLabel;
    private JLabel idLabel;
    private JLabel personalInfoLabel;
    private JLabel bankNameLabel;
    private JLabel bankAccountNumberLabel;
    private JLabel termsAndConditionsLabel;
    private JLabel salaryLabel;
    private JLabel startDateLabel;
    private JLabel rolesLabel;
    private JPanel rolesPane;
    private JLabel emailLabel;
    private JTextField emailTextField;
    private JLabel emailPasswordLabel;
    private JTextField emailPasswordTextField;
    private JButton sendPasswordButton;
    private JButton faceRecognitionImageButton;
    private String password = "";

    public EmployeeCreateScreen(){
        JFrame frame = new JFrame("Create Employee");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Set up start date spinner
        SpinnerDateModel startDateModel = new SpinnerDateModel();
        startDateSpinner.setModel(startDateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(startDateSpinner, "dd/MM/yyyy");
        startDateSpinner.setEditor(dateEditor);

        // Set up roles panel with checkboxes
        rolesPane.setLayout(new BoxLayout(rolesPane, BoxLayout.PAGE_AXIS));
        for (Role role : Role.values()) {
            JCheckBox roleCheckBox = new JCheckBox(role.toString());
            rolesPane.add(roleCheckBox);
        }
        emailPasswordTextField.setEditable(false);
        sendPasswordButton.addActionListener(e -> sendPassword());
        // Add action listener for addEmployee button
        addEmployeeButton.addActionListener(e -> addEmployee());
        faceRecognitionImageButton.addActionListener(e -> takePicture(idTextField.getText()));
    }

    private void takePicture(String id){
        File file = new File(System.getProperty("user.dir") + "\\AttendanceProject\\");

        try {
            Process p = Runtime.getRuntime().exec("python " + "TakePicture.py"+" "+id,
                    null, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addEmployee() {
        // Get input values from fields
        String name = nameTextField.getText();
        String id = idTextField.getText();
        String personalInfo = personalInfoTextField.getText();
        String bankName = bankNameTextField.getText();
        String bankAccountNumber = bankAccountNumberTextField.getText();
        java.util.Date startDate = (java.util.Date) startDateSpinner.getValue();
        String termsAndConditions = termsAndConditionsTextArea.getText();
        //check if salary is a number
        if(!salaryTextField.getText().matches("[0-9]+")) {
            JOptionPane.showMessageDialog(mainPanel, "Salary must be a number");
            return;
        }
        double salary = Double.parseDouble(salaryTextField.getText());

        // Get selected roles from checkboxes
        List<Role> roles = new ArrayList<>();
        Component[] components = rolesPane.getComponents();
        for (Component component : components) {
            if (component instanceof JCheckBox) {
                JCheckBox roleCheckBox = (JCheckBox) component;
                if (roleCheckBox.isSelected()) {
                    Role role = Role.valueOf(roleCheckBox.getText());
                    roles.add(role);
                }
            }
        }

        //check if got mail
        if(this.password.equals("")) {
            JOptionPane.showMessageDialog(mainPanel, "Please send password to email");
            return;
        }

        //check if password matches
        if(!emailPasswordTextField.getText().equals(this.password)) {
            JOptionPane.showMessageDialog(mainPanel, "Password does not match");
            return;
        }

        LicenseType licenseType = null;
        //check if driver is selected
        if(roles.contains(Role.DRIVER)) {
            Object[] licenses = LicenseType.values();
            licenseType = (LicenseType) JOptionPane.showInputDialog(this, "Select a license to add:", "Add License Type", JOptionPane.PLAIN_MESSAGE, null, licenses, licenses[0]);
            if (licenseType == null) {
                JOptionPane.showMessageDialog(this, "Invalid count entered.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Add employee using service layer
        EmployeeService employeeServiceManagement = getEmployeeService();
        Response response = employeeServiceManagement.addEmployee(name, id, personalInfo, bankName, bankAccountNumber,
                startDate, termsAndConditions, roles, salary, licenseType);

        // Show message and close window
        JOptionPane.showMessageDialog(mainPanel, response.getData().toString());
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
        frame.dispose();
    }

    private void sendPassword() {
        //get email
        String email = emailTextField.getText();

        //check if email is valid
        if(!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(mainPanel, "Invalid email");
            return;
        }

        //generate password
        this.password = String.format("%06d", new Random().nextInt(999999));
        Response<String> sendPasswordResponse = JavaMailUtil.sendMail(email, password);
        if(!sendPasswordResponse.isSuccess()) {
            JOptionPane.showMessageDialog(mainPanel, sendPasswordResponse.getData());
            return;
        }
        JOptionPane.showMessageDialog(mainPanel, "Password sent to email");
        emailPasswordTextField.setEditable(true);
    }

    public EmployeeService getEmployeeService() {
        return ServiceFactory.getInstance().getEmployeeService();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
