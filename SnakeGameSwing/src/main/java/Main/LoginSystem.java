package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author : Asnit Bakhati
 *
 */

public class LoginSystem extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JLabel messageLabel;
    private static final String FILE_PATH = "username.txt";

    public LoginSystem() {
        setTitle("Login Page");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1));

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        JButton submitButton = new JButton("Submit");
        messageLabel = new JLabel("", SwingConstants.CENTER);

        add(new JLabel("Username:"));
        add(usernameField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(submitButton);
        add(messageLabel);

        submitButton.addActionListener(new SubmitListener());

        if (!Files.exists(Paths.get(FILE_PATH))) {
            JOptionPane.showMessageDialog(this, "No user found. Please create an account.");
            createNewUser();
        }
    }

    private void createNewUser() {
        String username = JOptionPane.showInputDialog(this, "Enter Username:");
        String password = JOptionPane.showInputDialog(this, "Enter Password:");

        if (username != null && password != null && !username.isEmpty() && !password.isEmpty()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
                writer.write(username + "\n" + password);
                JOptionPane.showMessageDialog(this, "Account Created! Please log in.");
                File file = new File("highscore.txt");
                BufferedWriter bf = new BufferedWriter(new FileWriter(file));
                bf.write(String.valueOf(0));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving user details.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Username and Password cannot be empty.");
            createNewUser();
        }
    }

    private class SubmitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String enteredUsername = usernameField.getText();
            String enteredPassword = new String(passwordField.getPassword());

            try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
                String storedUsername = reader.readLine();
                String storedPassword = reader.readLine();

                if (enteredUsername.equals(storedUsername) && enteredPassword.equals(storedPassword)) {
                    messageLabel.setText("Login Successful!");
//                    openNextPage();
                } else {
                    messageLabel.setText("Invalid username or password.");
                }
            } catch (IOException ex) {
                messageLabel.setText("Error reading user data.");
            }
        }
    }

//    private void openNextPage() {
//        GamePanel panel = new GamePanel();
//        JFrame frame = new JFrame("Snake Game");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.add(panel);
//        frame.pack();
//        frame.setResizable(false);
//        frame.setVisible(true);
//        frame.setLocationRelativeTo(null);
//        this.dispose();
//    }


    public static void main(String[] args) {
        GamePanel panel = new GamePanel();
        JFrame frame = new JFrame("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}