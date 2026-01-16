package AIAgent.agent;

import Main.GamePanel;

/**
 * @author : Asnit Bakhati
 */

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

        if (gamePanel.checkSnakeDead()) {
            gamePanel.gameOn = false;
            return -200.0;
        }

        gamePanel.collisionChecker.checkAppleCollision();
        int scoreAfter = gamePanel.getScoreFromUI();

        if (scoreAfter > scoreBefore) {
            return 100.0 + (scoreAfter * 2.0);
        }

        double reward = 0;

        if (spaceAvailable < gamePanel.bodyParts) {
            reward -= 15.0;
        } else {
            int distAfter = gamePanel.getManhattanDistance();
            reward += (distBefore > distAfter) ? 1.0 : -1.5;
        }

        return reward - 0.05;
    }


    public void train() {
        int totalEpisodes = 1000000;
        int windowSize = 500;
        int[] scoreHistory = new int[windowSize];
        int historyIndex = 0;
        System.out.println("Training Started. Target Score: " + (gamePanel.gameUnits / 2));

        for (int ep = 1; ep <= totalEpisodes; ep++) {
            gamePanel.setBasics();
            int steps = 0;
            while (gamePanel.gameOn && steps < 4000) {
                int state = getState();
                int action = chooseAction(state);
                double reward = performAction(action);
                int nextState = gamePanel.gameOn ? getState() : state;
                updateQTable(state, action, reward, nextState);
                steps++;
            }

            int score = gamePanel.getScoreFromUI();
            scoreHistory[historyIndex % windowSize] = score;
            historyIndex++;

            if (score > bestEver) bestEver = score;

            if (ep % windowSize == 0) {
                double averageScore = 0;
                for (int s : scoreHistory) averageScore += s;
                averageScore /= windowSize;
                System.out.println("--------------------------------------------------");
                System.out.println(String.format("Ep: %d | Avg (Last 500): %.2f | Best: %d", ep, averageScore, bestEver));
                System.out.println(String.format("Stage: %d | ε: %.3f ", currentStage, epsilon));
                System.out.println("--------------------------------------------------");
                updateCurriculum((int)averageScore);
                if (epsilon > 0.01) epsilon *= 0.995;
                if (ep % 10000 == 0) {
                    try { saveQTable(); } catch (Exception e) { System.out.println("Save failed"); }
                }
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