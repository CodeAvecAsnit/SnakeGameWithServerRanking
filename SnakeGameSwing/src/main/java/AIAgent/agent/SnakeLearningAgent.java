package AIAgent.agent;

import Main.GamePanel;

/**
 * LEARNING AGENT
 *
 * Strategy: Train in progressive stages
 * 1. Stage 1: Learn to not die (survive 100+ steps)
 * 2. Stage 2: Learn to reach apple (get first apple)
 * 3. Stage 3: Learn to get multiple apples
 * 4. Stage 4: Optimize for high scores
 */
public class SnakeLearningAgent extends SnakeManagerAgent {

    private int currentStage = 1;
    private int stageProgress = 0;

    public SnakeLearningAgent(GamePanel gamePanel) {
        super(gamePanel);
    }

    @Override
    public double performAction(int action) {
        int scoreBefore = gamePanel.getScoreFromUI();
        int distBefore = gamePanel.getManhattanDistance();

        moveRelative(action);

        // Death penalty
        if (gamePanel.checkSnakeDead()) {
            gamePanel.gameOn = false;
            return getDeathPenalty();
        }

        // Apple reward
        gamePanel.collisionChecker.checkAppleCollision();
        int scoreAfter = gamePanel.getScoreFromUI();

        if (scoreAfter > scoreBefore) {
            return getAppleReward(scoreAfter);
        }

        // Distance based reward
        int distAfter = gamePanel.getManhattanDistance();
        double distanceReward = (distBefore - distAfter) / 32.0;

        // Stage specific rewards
        return distanceReward + getStageBonus() - 0.05;
    }

    private double getDeathPenalty() {
        return switch (currentStage) {
            case 1 -> -50.0;  // Stage 1: Moderate penalty, focus on survival
            case 2 -> -75.0;  // Stage 2: Higher penalty
            default -> -100.0; // Stage 3+: Full penalty
        };
    }

    private double getAppleReward(int totalScore) {
        double baseReward = 50.0;

        // Bonus for multiple apples in one game
        if (totalScore > 1) {
            baseReward += totalScore * 5.0; // Increasing rewards
        }

        return baseReward;
    }

    private double getStageBonus() {
        // Small bonus for just surviving (stage 1 focus)
        if (currentStage == 1) {
            return 0.1; // Reward survival
        }
        return 0.0;
    }

    public void advanceStage() {
        currentStage++;
        System.out.println("\n🎓 ADVANCING TO STAGE " + currentStage + "!");

        switch (currentStage) {
            case 2:
                System.out.println("   Focus: Learn to reach the apple");
                epsilon = 0.7;
                break;
            case 3:
                System.out.println("   Focus: Get multiple apples");
                epsilon = 0.5;
                break;
            case 4:
                System.out.println("   Focus: Maximize score");
                epsilon = 0.3;
                alpha = 0.1;
                break;
        }

        stageProgress = 0;
    }

    public static void main(String[] args) {

        GamePanel panel = new GamePanel(true);
        SnakeLearningAgent agent = new SnakeLearningAgent(panel);
        try{
            agent.loadQTable();
        }catch (Exception ex){}

        System.out.println("Training Strategy:");
        System.out.println("  Stage 1: Learn survival (100+ steps)");
        System.out.println("  Stage 2: Learn to eat first apple");
        System.out.println("  Stage 3: Get multiple apples");
        System.out.println("  Stage 4: Optimize for high scores\n");

        int totalEpisodes = 400000;
        int bestScore = 0;
        int[] recentScores = new int[100];
        int[] recentSteps = new int[100];
        int episodeIndex = 0;

        for (int ep = 1; ep <= totalEpisodes; ep++) {
            panel.setBasics();

            double totalReward = 0;
            int steps = 0;
            int maxSteps = 1000;

            while (panel.gameOn && steps < maxSteps) {
                int state = agent.getState();
                int action = agent.chooseAction(state);
                double reward = agent.performAction(action);
                totalReward += reward;

                int nextState = panel.gameOn ? agent.getState() : state;
                agent.updateQTable(state, action, reward, nextState);
                steps++;
            }

            if (panel.gameOn) panel.gameOn = false;

            int score = panel.getScoreFromUI();
            recentScores[episodeIndex % 100] = score;
            recentSteps[episodeIndex % 100] = steps;
            episodeIndex++;

            if (score > bestScore) {
                bestScore = score;
                System.out.println("★ NEW RECORD: " + bestScore + " apples at episode " + ep + "!");
            }

            if (ep % 100 == 0) {
                double avgScore = 0;
                double avgSteps = 0;
                for (int i = 0; i < 100; i++) {
                    avgScore += recentScores[i];
                    avgSteps += recentSteps[i];
                }
                avgScore /= 100;
                avgSteps /= 100;

                System.out.println("Ep " + ep +
                        " [Stage " + agent.currentStage + "] | " +
                        "Best: " + bestScore +
                        " | Avg: " + String.format("%.2f", avgScore) +
                        " | Steps: " + String.format("%.0f", avgSteps) +
                        " | ε: " + String.format("%.3f", agent.epsilon));

                if (agent.currentStage == 1 && avgSteps > 100) {
                    agent.stageProgress++;
                    if (agent.stageProgress >= 3) { // 3 consecutive good results
                        agent.advanceStage();
                    }
                } else if (agent.currentStage == 2 && avgScore > 0.5) {
                    agent.stageProgress++;
                    if (agent.stageProgress >= 3) {
                        agent.advanceStage();
                    }
                } else if (agent.currentStage == 3 && avgScore > 2.0) {
                    agent.stageProgress++;
                    if (agent.stageProgress >= 3) {
                        agent.advanceStage();
                    }
                } else {
                    agent.stageProgress = 0;
                }
            }

            if (ep % 100 == 0 && agent.epsilon > 0.05) {
                agent.epsilon *= 0.99;
            }
        }

        System.out.println("Final Stage: " + agent.currentStage);
        System.out.println("Best Score: " + bestScore);
        System.out.println("Final ε: " + String.format("%.3f", agent.epsilon));

        try {
            agent.saveQTable();
            System.out.println("\n✓ Curriculum-trained Q-table saved!");
        } catch (Exception e) {
            System.err.println("✗ Failed: " + e.getMessage());
        }
    }
}