package AIAgent.agent;

import Main.GamePanel;
import javax.swing.*;

/**
 * Clean Demo GUI
 * Replicates the original game look with AI control.
 */
public class DemoGUI {

    private static SnakeLearningAgent agent;
    private static GamePanel panel;
    private static Timer aiLoop;
    private static int gamesPlayed = 0;
    private static boolean isResetting = false;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            panel = new GamePanel(false);
            if (panel.getTimer() != null) panel.getTimer().stop();
            agent = new SnakeLearningAgent(panel);
            try {
                agent.loadQTable();
                agent.epsilon = 0.0;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "No trained model found. Please train the agent first.");
                return;
            }
            JFrame frame = new JFrame("Snake Bot");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.add(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            startAILoop();
        });
    }

    private static void startAILoop() {
        aiLoop = new Timer(panel.snakeSpeed, e -> {
            if (panel.gameOn) {
                isResetting = false;
                int state = agent.getState();
                int action = agent.chooseAction(state);
                agent.performAction(action);
                panel.repaint();
            } else if (!isResetting) {
                isResetting = true;
                gamesPlayed++;
                aiLoop.stop();
                Timer pause = new Timer(1500, evt -> {
                    panel.startGame();
                    if (panel.getTimer() != null) panel.getTimer().stop();
                    aiLoop.start();
                    ((Timer)evt.getSource()).stop();
                });
                pause.setRepeats(false);
                pause.start();
            }
        });
        aiLoop.start();
    }
}