package Main;

import AIAgent.agent.DemoGUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author : Asnit Bakhati
 */
public class LoginSystem extends JFrame {

    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JLabel messageLabel;
    private static final String FILE_PATH = "username.txt";


    private final int screenWidth = 800;
    private final int screenHeight = 800;

    private final Color primaryDark = new Color(20, 20, 30);
    private final Color accentGreen = new Color(0, 255, 127);
    private final Color accentBlue = new Color(64, 156, 255);
    private final Color cardBg = new Color(35, 35, 50);
    private final Color textLight = Color.WHITE;
    private final Color errorRed = new Color(255, 71, 87);

    private Image appleImage;
    private Image trophyImage;

    public LoginSystem() {
        setTitle("Snake World");
        setSize(screenWidth, screenHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        loadImages();

        JPanel mainContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                g2d.setColor(primaryDark);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                g2d.setColor(new Color(255, 255, 255, 8));
                for (int i = 0; i < 25; i++) {
                    for (int j = 0; j < 25; j++) {
                        if ((i + j) % 2 == 0) {
                            g2d.fillRect(i * 32, j * 32, 32, 32);
                        }
                    }
                }

                if (appleImage != null && trophyImage != null) {
                    g2d.drawImage(appleImage, 32, 32, 24, 24, null);
                    g2d.drawImage(trophyImage, screenWidth - 56, 32, 24, 24, null);

                    g2d.drawImage(appleImage, 32, screenHeight - 56, 24, 24, null);
                    g2d.drawImage(trophyImage, screenWidth - 56, screenHeight - 56, 24, 24, null);
                }
            }
        };
        mainContainer.setLayout(null);

        JLabel titleLabel = new JLabel("SNAKE APPLE") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 255, 127, 40));
                Font glowFont = getFont();
                g2d.setFont(glowFont);
                for (int i = 0; i < 4; i++) {
                    g2d.drawString(getText(), 4 - i, 4 - i);
                }
                super.paintComponent(g);
            }
        };
        titleLabel.setFont(new Font("Space Mono", Font.BOLD, 56));
        titleLabel.setForeground(accentGreen);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 80, screenWidth, 70);

        JLabel subtitleLabel = new JLabel("Enter the Arena");
        subtitleLabel.setFont(new Font("Space Mono", Font.PLAIN, 18));
        subtitleLabel.setForeground(textLight);

        JPanel loginCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.fillRoundRect(5, 5, getWidth() - 5, getHeight() - 5, 20, 20);
                g2d.setColor(cardBg);
                g2d.fillRoundRect(0, 0, getWidth() - 5, getHeight() - 5, 20, 20);
                g2d.setColor(accentGreen);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth() - 7, getHeight() - 7, 20, 20);
            }
        };
        loginCard.setLayout(null);
        loginCard.setOpaque(false);
        loginCard.setBounds(200, 230, 400, 280);
        JPanel userLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        userLabelPanel.setOpaque(false);
        userLabelPanel.setBounds(30, 30, 340, 24);
        if (appleImage != null) {
            JLabel appleIcon = new JLabel(new ImageIcon(appleImage.getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
            userLabelPanel.add(appleIcon);
        }
        JLabel userLabel = new JLabel("USERNAME");
        userLabel.setFont(new Font("Space Mono", Font.BOLD, 12));
        userLabel.setForeground(textLight);
        userLabelPanel.add(userLabel);
        usernameField = createStyledTextField();
        usernameField.setBounds(30, 58, 340, 45);
        JPanel passLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        passLabelPanel.setOpaque(false);
        passLabelPanel.setBounds(30, 118, 340, 24);
        if (trophyImage != null) {
            JLabel trophyIcon = new JLabel(new ImageIcon(trophyImage.getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
            passLabelPanel.add(trophyIcon);
        }
        JLabel passLabel = new JLabel("PASSWORD");
        passLabel.setFont(new Font("Space Mono", Font.BOLD, 12));
        passLabel.setForeground(textLight);
        passLabelPanel.add(passLabel);
        passwordField = createStyledPasswordField();
        passwordField.setBounds(30, 146, 340, 45);
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Space Mono", Font.PLAIN, 12));
        messageLabel.setForeground(errorRed);
        messageLabel.setBounds(30, 201, 340, 20);

        JButton loginButton = createStyledButton("LOGIN", accentGreen);
        loginButton.setBounds(30, 231, 340, 45);
        loginButton.addActionListener(new SubmitListener());

        loginCard.add(userLabelPanel);
        loginCard.add(usernameField);
        loginCard.add(passLabelPanel);
        loginCard.add(passwordField);
        loginCard.add(messageLabel);
        loginCard.add(loginButton);

        JPanel quickPlayPanel = new JPanel();
        quickPlayPanel.setLayout(new GridLayout(1, 2, 20, 0));
        quickPlayPanel.setOpaque(false);
        quickPlayPanel.setBounds(150, 550, 500, 60);

        JButton guestButton = createStyledButton("GUEST MODE", new Color(255, 193, 7));
        JButton aiButton = createStyledButton("QUILVER AI", accentBlue);

        guestButton.addActionListener(e -> launchManualGame());
        aiButton.addActionListener(e -> {
            this.dispose();
            DemoGUI.main(new String[0]);
        });

        quickPlayPanel.add(guestButton);
        quickPlayPanel.add(aiButton);

        JLabel footerLabel = new JLabel("© 2025 Snake World | Choose Your Path");
        footerLabel.setFont(new Font("Space Mono", Font.PLAIN, 11));
        footerLabel.setForeground(new Color(255, 255, 255, 120));
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        footerLabel.setBounds(0, screenHeight - 50, screenWidth, 20);

        mainContainer.add(titleLabel);
        mainContainer.add(subtitleLabel);
        mainContainer.add(loginCard);
        mainContainer.add(quickPlayPanel);
        mainContainer.add(footerLabel);

        add(mainContainer);

        if (!Files.exists(Paths.get(FILE_PATH))) {
            createNewUser();
        }
    }

    private void loadImages() {
        try {
            appleImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream("apple/apple.png"));
            trophyImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream("extras/trophy.png"));
        } catch (Exception e) {
            System.out.println("Could not load images: " + e.getMessage());
        }
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(30, 30, 50));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                if (hasFocus()) {
                    g2d.setColor(accentGreen);
                    g2d.setStroke(new BasicStroke(2));
                } else {
                    g2d.setColor(new Color(60, 60, 80));
                }
                g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 10, 10);

                super.paintComponent(g);
            }
        };
        field.setOpaque(false);
        field.setFont(new Font("Space Mono", Font.PLAIN, 16));
        field.setForeground(textLight);
        field.setCaretColor(accentGreen);
        field.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(30, 30, 50));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                if (hasFocus()) {
                    g2d.setColor(accentGreen);
                    g2d.setStroke(new BasicStroke(2));
                } else {
                    g2d.setColor(new Color(60, 60, 80));
                }
                g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 10, 10);

                super.paintComponent(g);
            }
        };
        field.setOpaque(false);
        field.setFont(new Font("Space Mono", Font.PLAIN, 16));
        field.setForeground(textLight);
        field.setCaretColor(accentGreen);
        field.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return field;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text) {
            private boolean hover = false;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                Color btnColor = hover ? color.brighter() : color;
                g2d.setColor(btnColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), textX, textY);
            }

            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hover = true;
                        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        hover = false;
                        setCursor(Cursor.getDefaultCursor());
                        repaint();
                    }
                });
            }
        };

        button.setFont(new Font("Space Mono", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        return button;
    }

    private void createNewUser() {
        JPanel panel = new JPanel(new GridLayout(6, 1, 5, 10));
        panel.setBackground(cardBg);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel infoLabel = new JLabel("Create Your Account");
        infoLabel.setFont(new Font("Space Mono", Font.BOLD, 16));
        infoLabel.setForeground(accentGreen);
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Space Mono", Font.PLAIN, 12));
        userLabel.setForeground(textLight);

        JTextField newUsername = new JTextField();
        newUsername.setFont(new Font("Space Mono", Font.PLAIN, 14));
        newUsername.setBackground(new Color(30, 30, 50));
        newUsername.setForeground(textLight);
        newUsername.setCaretColor(accentGreen);
        newUsername.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accentGreen, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Space Mono", Font.PLAIN, 12));
        passLabel.setForeground(textLight);

        JPasswordField newPassword = new JPasswordField();
        newPassword.setFont(new Font("Space Mono", Font.PLAIN, 14));
        newPassword.setBackground(new Color(30, 30, 50));
        newPassword.setForeground(textLight);
        newPassword.setCaretColor(accentGreen);
        newPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accentGreen, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        panel.add(infoLabel);
        panel.add(userLabel);
        panel.add(newUsername);
        panel.add(passLabel);
        panel.add(newPassword);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "No Account Found", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String username = newUsername.getText();
            String password = new String(newPassword.getPassword());

            if (username != null && !username.isEmpty() &&
                    password != null && !password.isEmpty()) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
                    writer.write(username + "\n" + password);
                    new File("highscore.txt").createNewFile();
                    JOptionPane.showMessageDialog(this, "Account Created Successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Error saving account.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
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

                if (enteredUsername.equals(storedUsername) &&
                        enteredPassword.equals(storedPassword)) {
                    messageLabel.setForeground(accentGreen);
                    messageLabel.setText("✓ Login Successful!");

                    Timer timer = new Timer(500, evt -> launchManualGame());
                    timer.setRepeats(false);
                    timer.start();
                } else {
                    messageLabel.setForeground(errorRed);
                    messageLabel.setText("✗ Invalid credentials");

                    // Shake animation
                    Point original = getLocation();
                    Timer shakeTimer = new Timer(50, null);
                    final int[] count = {0};
                    shakeTimer.addActionListener(evt -> {
                        if (count[0] < 6) {
                            int offset = (count[0] % 2 == 0) ? 10 : -10;
                            setLocation(original.x + offset, original.y);
                            count[0]++;
                        } else {
                            setLocation(original);
                            ((Timer) evt.getSource()).stop();
                        }
                    });
                    shakeTimer.start();
                }
            } catch (IOException ex) {
                messageLabel.setForeground(errorRed);
                messageLabel.setText("✗ Error reading user data");
            }
        }
    }

    private void launchManualGame() {
        JFrame frame = new JFrame("Snake Game - Manual");
        GamePanel panel = new GamePanel(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        this.dispose();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new LoginSystem().setVisible(true));
    }
}