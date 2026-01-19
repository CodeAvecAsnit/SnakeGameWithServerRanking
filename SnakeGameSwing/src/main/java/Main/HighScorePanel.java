package Main;

import Entity.SnakeGameUser;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * @author : Asnit Bakhati
 *
 */
public class HighScorePanel extends JPanel {
    private final int screenWidth;
    private final int screenHeight;
    private JFrame frame;
    private GamePanel gamePanel;

    public HighScorePanel(int screenWidth, int screenHeight, JFrame frame, GamePanel gamePanel) throws RuntimeException{
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.frame = frame;
        this.gamePanel = gamePanel;

        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setLayout(new BorderLayout());
        setBackground(new Color(45, 45, 45));

        try {
            List<SnakeGameUser> highScores = APIRequest.fetchHighScores();
            add(createHighScoreList(highScores), BorderLayout.CENTER);
        }catch (RuntimeException ex){}

        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.BOLD, 14));
        startButton.setForeground(Color.WHITE);
        startButton.setBackground(new Color(70, 130, 180));
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
        buttonPanel.setBackground(new Color(45, 45, 45));
        buttonPanel.add(startButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }


    private JPanel createHighScoreList(List<SnakeGameUser> highScores) {
        JPanel listPanel = new JPanel();
        if (highScores == null || highScores.isEmpty()) {
            listPanel.setLayout(new BorderLayout());
            listPanel.setBackground(new Color(45, 45, 45));
            JLabel noDataLabel = new JLabel("No high scores available. Server might be offline.", SwingConstants.CENTER);
            noDataLabel.setFont(new Font("Arial", Font.BOLD, 16));
            noDataLabel.setForeground(Color.WHITE);
            listPanel.add(noDataLabel, BorderLayout.CENTER);
            return listPanel;
        }
        listPanel.setLayout(new GridLayout(highScores.size() + 2, 1)); // +2 for title and header
        listPanel.setBackground(new Color(45, 45, 45));
        JLabel titleLabel = new JLabel("High Scores", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        listPanel.add(titleLabel);

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

        int index = 1;
        for (SnakeGameUser game : highScores) {
            JPanel rowPanel = new JPanel(new GridLayout(1, 3));
            rowPanel.setBackground(index % 2 == 0 ? new Color(60, 60, 60) : new Color(50, 50, 50));

            JLabel snoLabel = new JLabel(String.valueOf(index), SwingConstants.CENTER);
            JLabel userLabel = new JLabel(game.getGameUsername(), SwingConstants.CENTER);
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