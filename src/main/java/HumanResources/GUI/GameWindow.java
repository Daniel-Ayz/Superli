package HumanResources.GUI;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class GameWindow {
    private JButton yesButton;
    private JPanel panel1;
    private JButton noThankYouButton;

    public GameWindow() {
        JFrame frame = new JFrame("GameWindow");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        yesButton.addActionListener(e -> {
            frame.dispose();
            File file = new File(System.getProperty("user.dir"));
            try {
                Process p = Runtime.getRuntime().exec("python " + "game.py",
                        null, file);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        noThankYouButton.addActionListener(e -> {
            frame.dispose();
        });
    }
}
