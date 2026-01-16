package Main;

import Entity.SnakeGame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author : Asnit Bakhati
 *
 */

//Ignore this part for now. Update this part while working on server connection.
public class HighScorePanel extends JPanel {
    private final int screenWidth;
    private final int screenHeight;
    private JFrame frame;
    private GamePanel gamePanel;

    public HighScorePanel(int screenWidth, int screenHeight, JFrame frame, GamePanel gamePanel) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.frame = frame;
        this.gamePanel = gamePanel;

        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setLayout(new BorderLayout());
        setBackground(new Color(45, 45, 45)); // Dark background

        // Fetch and display high scores
        List<SnakeGame> highScores = fetchHighScores();
        add(createHighScoreList(highScores), BorderLayout.CENTER);

        // Start Game Button
        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.BOLD, 14));
        startButton.setForeground(Color.WHITE);
        startButton.setBackground(new Color(70, 130, 180)); // Steel Blue
        startButton.setFocusPainted(false);
        startButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(gamePanel);
                frame.revalidate();
                frame.repaint();
                gamePanel.startGame();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(45, 45, 45)); // Dark background
        buttonPanel.add(startButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private List<SnakeGame> fetchHighScores() {
        try {
            URL url = new URL("http://localhost:8080/api/snakeGame/get/top10");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.toString(), new TypeReference<List<SnakeGame>>() {});
        } catch (Exception e) {
            return null;
        }
    }

    private JPanel createHighScoreList(List<SnakeGame> highScores) {
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new GridLayout(highScores.size() + 2, 1)); // +2 for title and header
        listPanel.setBackground(new Color(45, 45, 45));

        // Title Label
        JLabel titleLabel = new JLabel("Top 10 High Scores", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        listPanel.add(titleLabel);

        // Header Row: S.No | Username | Score
        JPanel headerPanel = new JPanel(new GridLayout(1, 3));
        headerPanel.setBackground(new Color(70, 70, 70));

        JLabel snoHeader = new JLabel("S.No", SwingConstants.CENTER);
        JLabel userHeader = new JLabel("Username", SwingConstants.CENTER);
        JLabel scoreHeader = new JLabel("Score", SwingConstants.CENTER);

        snoHeader.setFont(new Font("Arial", Font.BOLD, 14));
        userHeader.setFont(new Font("Arial", Font.BOLD, 14));
        scoreHeader.setFont(new Font("Arial", Font.BOLD, 14));

        snoHeader.setForeground(Color.WHITE);
        userHeader.setForeground(Color.WHITE);
        scoreHeader.setForeground(Color.WHITE);

        headerPanel.add(snoHeader);
        headerPanel.add(userHeader);
        headerPanel.add(scoreHeader);

        listPanel.add(headerPanel);

        // Adding High Score List
        int index = 1;
        for (SnakeGame game : highScores) {
            JPanel rowPanel = new JPanel(new GridLayout(1, 3));
            rowPanel.setBackground(index % 2 == 0 ? new Color(60, 60, 60) : new Color(50, 50, 50));

            JLabel snoLabel = new JLabel(String.valueOf(index), SwingConstants.CENTER);
            JLabel userLabel = new JLabel(game.getGame_username(), SwingConstants.CENTER);
            JLabel scoreLabel = new JLabel(String.valueOf(game.getScore()), SwingConstants.CENTER);

            snoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            scoreLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            snoLabel.setForeground(Color.WHITE);
            userLabel.setForeground(Color.WHITE);
            scoreLabel.setForeground(Color.WHITE);

            rowPanel.add(snoLabel);
            rowPanel.add(userLabel);
            rowPanel.add(scoreLabel);

            listPanel.add(rowPanel);
            index++;
        }

        return listPanel;
    }
}