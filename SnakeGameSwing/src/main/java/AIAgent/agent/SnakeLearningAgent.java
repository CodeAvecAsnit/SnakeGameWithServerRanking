package AIAgent.agent;

import Main.GamePanel;

public class SnakeLearningAgent extends SnakeManagerAgent {
    private int currentStage = 1;
    private int bestEver = 0;

    public SnakeLearningAgent(GamePanel gamePanel) {
        super(gamePanel);
    }

    @Override
    public double performAction(int action) {
        int scoreBefore = gamePanel.getScoreFromUI();
        int distBefore = gamePanel.getManhattanDistance();
        int[] next = nextPosition(gamePanel.snakeX[0], gamePanel.snakeY[0], gamePanel.direction, gamePanel.tileSize);
        int spaceAvailable = countAvailableSpace(next[0], next[1]);

        moveRelative(action);

        if (gamePanel.checkSnakeDead()) return -200.0;

        gamePanel.collisionChecker.checkAppleCollision();
        int scoreAfter = gamePanel.getScoreFromUI();
        if (scoreAfter > scoreBefore) {
            return 50.0 + (scoreAfter * 5.0);
        }
        double reward = 0;
        if (spaceAvailable < gamePanel.bodyParts) {
            reward -= 10.0;
        }
        int distAfter = gamePanel.getManhattanDistance();
        if (spaceAvailable > gamePanel.bodyParts * 1.5) {
            reward += (distBefore - distAfter) / 32.0;
        }
        return reward - 0.05;
    }

    public void train() {
        int totalEpisodes = 500000;
        for (int ep = 1; ep <= totalEpisodes; ep++) {
            gamePanel.setBasics();
            int steps = 0;

            while (gamePanel.gameOn && steps < 2000) {
                int state = getState();
                int action = chooseAction(state);
                double reward = performAction(action);
                updateQTable(state, action, reward, getState());
                steps++;
            }

            int score = gamePanel.getScoreFromUI();
            if (score > bestEver) bestEver = score;

            if (ep % 500 == 0) {
                System.out.println("Ep: " + ep + " | Best: " + bestEver + " | Stage: " + currentStage + " | Eps: " + String.format("%.3f", epsilon));
                updateCurriculum(score);
                if (epsilon > 0.01) epsilon *= 0.995;
            }
        }
    }

    private void updateCurriculum(int lastScore) {
        if (currentStage == 1 && bestEver > 5) currentStage = 2;
        else if (currentStage == 2 && bestEver > 20) currentStage = 3;
        else if (currentStage == 3 && bestEver > 50) currentStage = 4;
        if (currentStage == 4) alpha = 0.05;
    }

    public static void main(String[] args) {
        GamePanel panel = new GamePanel(true);
        SnakeLearningAgent agent = new SnakeLearningAgent(panel);
        agent.train();
    }
}