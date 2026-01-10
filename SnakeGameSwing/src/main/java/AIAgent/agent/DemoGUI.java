package AIAgent.agent;

import Main.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Demo GUI
 * Watch the enhanced state space agent play
 */
public class DemoGUI {

    private static SnakeManagerAgent agent;
    private static GamePanel panel;
    private static Timer gameTimer;
    private static JFrame frame;
    private static JLabel statsLabel;

    private static int gamesPlayed = 0;
    private static int totalScore = 0;
    private static int bestScore = 0;
    private static int currentStreak = 0;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            panel = new GamePanel(false);
            agent = new SnakeLearningAgent(panel);
            try {
                agent.loadQTable();
            } catch (Exception e) {
                int choice = JOptionPane.showConfirmDialog(null,
                        "Automated Snake Game",
                        "Snake game",
                        JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    trainQuickly();
                } else {
                    return;
                }
            }

            agent.epsilon = 0.05;
            frame = new JFrame("Enhanced Snake AI - State Space: 2048");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            frame.add(panel, BorderLayout.CENTER);
            JPanel statsPanel = new JPanel();
            statsPanel.setBackground(Color.BLACK);
            statsPanel.setPreferredSize(new Dimension(panel.screenWidth, 50));
            statsLabel = new JLabel();
            statsLabel.setForeground(Color.WHITE);
            statsLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
            updateStatsLabel();
            statsPanel.add(statsLabel);
            frame.add(statsPanel, BorderLayout.SOUTH);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            startAIGameLoop();
        });
    }

    private static void trainQuickly() {
        JOptionPane.showMessageDialog(null,
                "Trained",
                "Snake Game",
                JOptionPane.INFORMATION_MESSAGE);

        new Thread(() -> {
            GamePanel trainPanel = new GamePanel(true);
            SnakeManagerAgent trainAgent = new SnakeLearningAgent(trainPanel);


            for (int ep = 1; ep <= 5000; ep++) {
                trainPanel.setBasics();
                int steps = 0;

                while (trainPanel.gameOn && steps < 500) {
                    int state = trainAgent.getState();
                    int action = trainAgent.chooseAction(state);
                    trainAgent.performAction(action);

                    int nextState = trainPanel.gameOn ? trainAgent.getState() : state;
                    trainAgent.updateQTable(state, action, 0, nextState);
                    steps++;
                }

                if (trainPanel.gameOn) trainPanel.gameOn = false;

                if (ep % 500 == 0) {
                    System.out.println("Episode " + ep + "/5000 complete");
                }

                if (ep % 50 == 0 && trainAgent.epsilon > 0.1) {
                    trainAgent.epsilon *= 0.99;
                }
            }

            try {
                trainAgent.saveQTable();
                System.out.println("✓ Quick training complete!");

                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null,
                            "Training complete! Restarting demo...",
                            "Ready",
                            JOptionPane.INFORMATION_MESSAGE);
                    main(null);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void startAIGameLoop() {
        gameTimer = new Timer(panel.snakeSpeed, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (panel.gameOn) {
                    int state = agent.getState();
                    int action = agent.chooseAction(state);
                    agent.performAction(action);
                    panel.repaint();
                    gameTimer.setDelay(panel.snakeSpeed);
                    updateStatsLabel();
                } else {
                    int score = panel.getScoreFromUI();
                    gamesPlayed++;
                    totalScore += score;

                    if (score > 0) {
                        currentStreak++;
                    } else {
                        currentStreak = 0;
                    }

                    if (score > bestScore) {
                        bestScore = score;
                        frame.setTitle("Enhanced Snake AI - NEW BEST: " + bestScore + " apples!");
                    }

                    double avg = totalScore / (double) gamesPlayed;
                    System.out.println("Game " + gamesPlayed +
                            ": " + score + " apples | " +
                            "Avg: " + String.format("%.2f", avg) + " | " +
                            "Best: " + bestScore + " | " +
                            "Streak: " + currentStreak);

                    updateStatsLabel();
                    Timer restartTimer = new Timer(1500, evt -> {
                        panel.startGame();
                        ((Timer)evt.getSource()).stop();
                    });
                    restartTimer.setRepeats(false);
                    restartTimer.start();
                }
            }
        });

        gameTimer.start();
    }

    private static void updateStatsLabel() {
        if (statsLabel != null) {
            double avg = gamesPlayed > 0 ? totalScore / (double) gamesPlayed : 0;
            String stats = String.format(
                    "Games: %d  |  Best: %d  |  Avg: %.2f  |  Current: %d  |  Streak: %d",
                    gamesPlayed, bestScore, avg, panel.getScoreFromUI(), currentStreak
            );
            statsLabel.setText(stats);
        }
    }
}