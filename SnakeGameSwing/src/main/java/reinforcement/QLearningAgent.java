package reinforcement;

import java.util.Random;

public class QLearningAgent {
    private double[][] QTable;
    private double alpha;      // Learning rate
    private double gamma;      // Discount factor
    private double epsilon;    // Exploration rate
    private SnakeWorld snakeWorld;
    private Random random;

    private final int STATE_SIZE = 12;  // Simplified state representation
    private final int ACTION_SIZE = 4;   // 4 possible actions

    public QLearningAgent(SnakeWorld snakeWorld, double alpha, double gamma, double epsilon) {
        this.snakeWorld = snakeWorld;
        this.alpha = alpha;
        this.gamma = gamma;
        this.epsilon = epsilon;
        this.random = new Random();

        // Initialize Q-Table with small random values
        QTable = new double[STATE_SIZE][ACTION_SIZE];
        for (int i = 0; i < STATE_SIZE; i++) {
            for (int j = 0; j < ACTION_SIZE; j++) {
                QTable[i][j] = random.nextDouble() * 0.01;
            }
        }
    }

    /**
     * Get simplified state index based on snake and apple positions
     * States represent relative positions:
     * - Danger direction (wall or self-collision)
     * - Food direction (relative to snake head)
     * - Current moving direction
     */
    public int getState() {
        int[] arr = snakeWorld.getState();
        int snakeX = arr[0];
        int snakeY = arr[1];
        int appleX = arr[2];
        int appleY = arr[2];

        // Determine danger directions (0-3 bits for up, down, left, right)
        boolean dangerUp = snakeY <= 0;
        boolean dangerDown = snakeY >= SnakeWorld.GridY - 1;
        boolean dangerLeft = snakeX <= 0;
        boolean dangerRight = snakeX >= SnakeWorld.GridX - 1;

        // Determine food direction
        boolean foodUp = appleY < snakeY;
        boolean foodDown = appleY > snakeY;
        boolean foodLeft = appleX < snakeX;
        boolean foodRight = appleX > snakeX;

        // Create a simple state encoding (0-11)
        // This is a simplified state space for demonstration
        int state = 0;

        if (foodUp && !dangerUp) state = 0;
        else if (foodDown && !dangerDown) state = 1;
        else if (foodLeft && !dangerLeft) state = 2;
        else if (foodRight && !dangerRight) state = 3;
        else if (foodUp && foodLeft) state = 4;
        else if (foodUp && foodRight) state = 5;
        else if (foodDown && foodLeft) state = 6;
        else if (foodDown && foodRight) state = 7;
        else if (dangerUp || dangerDown) state = 8;
        else if (dangerLeft || dangerRight) state = 9;
        else state = 10;

        return Math.min(state, STATE_SIZE - 1);
    }

    /**
     * Choose action using epsilon-greedy policy
     */
    public int chooseAction(int state) {
        if (random.nextDouble() < epsilon) {
            return random.nextInt(ACTION_SIZE) + 1;
        }
        return getBestAction(state);
    }

    /**
     * Get the best action for a given state
     */
    private int getBestAction(int state) {
        double maxQ = Double.NEGATIVE_INFINITY;
        int bestAction = 1;

        for (int action = 0; action < ACTION_SIZE; action++) {
            if (QTable[state][action] > maxQ) {
                maxQ = QTable[state][action];
                bestAction = action + 1;
            }
        }

        return bestAction;
    }

    /**
     * Update Q-Table using Q-Learning formula:
     * Q(s,a) = Q(s,a) + α[r + γ * max(Q(s',a')) - Q(s,a)]
     */
    public void updateQTable(int state, int action, double reward, int nextState) {
        int actionIndex = action - 1;
        double maxNextQ = Double.NEGATIVE_INFINITY;
        for (int a = 0; a < ACTION_SIZE; a++) {
            if (QTable[nextState][a] > maxNextQ) {
                maxNextQ = QTable[nextState][a];
            }
        }

        double currentQ = QTable[state][actionIndex];
        double newQ = currentQ + alpha * (reward + gamma * maxNextQ - currentQ);
        QTable[state][actionIndex] = newQ;
    }

    /**
     * Train the agent for a single episode
     */
    public void trainEpisode() {
        snakeWorld.reset();
        int totalReward = 0;
        int steps = 0;
        int maxSteps = 1000;

        while (!snakeWorld.isGameOver() && steps < maxSteps) {
            int currentState = getState();
            int action = chooseAction(currentState);
            double reward = snakeWorld.performAction(action);
            totalReward += reward;
            int nextState = getState();
            updateQTable(currentState, action, reward, nextState);

            steps++;
        }

        System.out.println("Episode finished - Steps: " + steps +
                ", Score: " + snakeWorld.getScore() +
                ", Total Reward: " + totalReward);
    }

    /**
     * Train the agent for multiple episodes
     */
    public void train(int episodes) {
        System.out.println("Starting training for " + episodes + " episodes...");
        System.out.println("Alpha: " + alpha + ", Gamma: " + gamma +
                ", Epsilon: " + epsilon);
        System.out.println("----------------------------------------");

        for (int episode = 1; episode <= episodes; episode++) {
            System.out.print("Episode " + episode + " - ");
            trainEpisode();
            if (episode % 100 == 0) {
                epsilon = Math.max(0.01, epsilon * 0.95);
                System.out.println("Epsilon decayed to: " + epsilon);
            }
            if (episode % 50 == 0) {
                System.out.println("========== Completed " + episode + " episodes ==========");
            }
        }

        System.out.println("----------------------------------------");
        System.out.println("Training complete!");
    }

    /**
     * Test the trained agent (no exploration)
     */
    public void test() {
        snakeWorld.reset();
        double oldEpsilon = epsilon;
        epsilon = 0;

        System.out.println("\n=== Testing Agent ===");
        trainEpisode();

        epsilon = oldEpsilon;
    }

    /**
     * Print Q-Table for debugging
     */
    public void printQTable() {
        System.out.println("\n=== Q-Table ===");
        System.out.println("State\\Action\tUP\t\tDOWN\t\tLEFT\t\tRIGHT");
        for (int i = 0; i < STATE_SIZE; i++) {
            System.out.printf("State %d:\t", i);
            for (int j = 0; j < ACTION_SIZE; j++) {
                System.out.printf("%.3f\t\t", QTable[i][j]);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        SnakeWorld world = new SnakeWorld();

        // Create Q-Learning agent
        // alpha = 0.1 (learning rate)
        // gamma = 0.9 (discount factor)
        // epsilon = 0.3 (exploration rate)
        QLearningAgent agent = new QLearningAgent(world, 0.3, 0.9, 0.3);

        agent.train(500000);

        agent.test();

        agent.printQTable();
    }
}