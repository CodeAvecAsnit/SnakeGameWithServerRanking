package AIAgent.agent;

import AIAgent.component.QLearningAgent;
import Extra.Direction;
import Main.GamePanel;

/**
 * ENHANCED STATE SPACE AGENT
 *
 */

public class SnakeManagerAgent extends QLearningAgent {
    public static final int STATES_SIZE = 2048;
    public static final int ACTION_SIZE = 3;
    private static final String FILE_NAME = "snakeTableEnhanced.bin";
    protected final GamePanel gamePanel;

    public SnakeManagerAgent(GamePanel gamePanel) {
        super(0.15, 0.95, 1.0, STATES_SIZE, ACTION_SIZE, FILE_NAME);
        this.gamePanel = gamePanel;
    }

    public double performAction(int action) {
        int scoreBefore = gamePanel.getScoreFromUI();
        int distBefore = gamePanel.getManhattanDistance();

        moveRelative(action);

        // Death penalty
        if (gamePanel.checkSnakeDead()) {
            gamePanel.gameOn = false;
            return -100.0;
        }

        // Apple reward
        gamePanel.collisionChecker.checkAppleCollision();
        int scoreAfter = gamePanel.getScoreFromUI();

        if (scoreAfter > scoreBefore) {
            return 50.0; // Big reward for eating apple
        }

        // Distance-based reward (encourage moving toward apple)
        int distAfter = gamePanel.getManhattanDistance();
        double distanceReward = (distBefore - distAfter) / 32.0;

        double distancePenalty = 0;
        if (distAfter > 300) {
            distancePenalty = -0.5; // Penalize being far from apple
        }

        return distanceReward + distancePenalty - 0.05; // Small time penalty
    }

    public void moveRelative(int action) {
        Direction dir = gamePanel.direction;
        Direction newDir;

        switch (action) {
            case 0: // STRAIGHT
                newDir = dir;
                break;
            case 1: // LEFT
                newDir = switch (dir) {
                    case UP -> Direction.LEFT;
                    case LEFT -> Direction.DOWN;
                    case DOWN -> Direction.RIGHT;
                    case RIGHT -> Direction.UP;
                };
                break;
            case 2: // RIGHT
                newDir = switch (dir) {
                    case UP -> Direction.RIGHT;
                    case RIGHT -> Direction.DOWN;
                    case DOWN -> Direction.LEFT;
                    case LEFT -> Direction.UP;
                };
                break;
            default:
                throw new IllegalArgumentException("Invalid action: " + action);
        }

        gamePanel.direction = newDir;
        gamePanel.move();
    }

    /**
     * ENHANCED STATE ENCODING (11 bits = 2048 states)
     */
    public int getState() {
        Direction currDirection = gamePanel.getDirection();
        int snakeHeadX = gamePanel.snakeX[0];
        int snakeHeadY = gamePanel.snakeY[0];
        int appleX = gamePanel.getAppleX();
        int appleY = gamePanel.getAppleY();
        int tile = gamePanel.tileSize;

        // 1. Apple quadrant (2 bits)
        int appleQuadrant = getAppleQuadrant(snakeHeadX, snakeHeadY, appleX, appleY, currDirection);

        // 2. Immediate danger (3 bits)
        int dangerStraight = dangerStraight(currDirection, tile) ? 1 : 0;
        int dangerLeft = dangerLeft(currDirection, tile) ? 1 : 0;
        int dangerRight = dangerRight(currDirection, tile) ? 1 : 0;

        // 3. Distance category (2 bits: 0=close, 1=medium, 2=far, 3=very far)
        int distance = gamePanel.getManhattanDistance();
        int distCategory = getDistanceCategory(distance);

        // 4. Body danger ahead (2 bits: body close in 2-3 tiles)
        int bodyDangerAhead = getBodyDangerAhead(currDirection, tile);

        // 5. Wall proximity (2 bits: how close to walls)
        int wallProximity = getWallProximity(snakeHeadX, snakeHeadY);

        // Encode state: 11 bits total
        return (appleQuadrant << 9) |      // bits 10-9
                (dangerStraight << 8) |      // bit 8
                (dangerLeft << 7) |          // bit 7
                (dangerRight << 6) |         // bit 6
                (distCategory << 4) |        // bits 5-4
                (bodyDangerAhead << 2) |     // bits 3-2
                (wallProximity);             // bits 1-0
    }

    /**
     * Get which quadrant the apple is in relative to snake's direction
     * 0 = ahead-right, 1 = ahead-left, 2 = behind-right, 3 = behind-left
     */
    private int getAppleQuadrant(int snakeX, int snakeY, int appleX, int appleY, Direction dir) {
        int dx = appleX - snakeX;
        int dy = appleY - snakeY;

        switch (dir) {
            case RIGHT:
                if (dx > 0 && dy <= 0) return 0; // ahead-right (up-right)
                if (dx > 0 && dy > 0) return 1;  // ahead-left (down-right)
                if (dx <= 0 && dy <= 0) return 2; // behind-right (up-left)
                return 3; // behind-left (down-left)
            case LEFT:
                if (dx < 0 && dy >= 0) return 0;
                if (dx < 0 && dy < 0) return 1;
                if (dx >= 0 && dy >= 0) return 2;
                return 3;
            case DOWN:
                if (dy > 0 && dx >= 0) return 0;
                if (dy > 0 && dx < 0) return 1;
                if (dy <= 0 && dx >= 0) return 2;
                return 3;
            case UP:
                if (dy < 0 && dx <= 0) return 0;
                if (dy < 0 && dx > 0) return 1;
                if (dy >= 0 && dx <= 0) return 2;
                return 3;
        }
        return 0;
    }

    private int getDistanceCategory(int distance) {
        if (distance < 96) return 0;  // close (< 3 tiles)
        if (distance < 192) return 1; // medium (3-6 tiles)
        if (distance < 320) return 2; // far (6-10 tiles)
        return 3; // very far (>10 tiles)
    }

    /**
     * Check if body is 2-3 tiles ahead (early warning)
     */
    private int getBodyDangerAhead(Direction dir, int tile) {
        int headX = gamePanel.snakeX[0];
        int headY = gamePanel.snakeY[0];

        int danger = 0;
        int[] pos2 = getPositionInDirection(headX, headY, dir, tile * 2);
        if (isBodyAt(pos2[0], pos2[1])) danger = 2;

        int[] pos3 = getPositionInDirection(headX, headY, dir, tile * 3);
        if (isBodyAt(pos3[0], pos3[1])) danger = Math.max(danger, 1);

        return danger;
    }

    private boolean isBodyAt(int x, int y) {
        for (int i = 1; i < gamePanel.bodyParts; i++) {
            if (x == gamePanel.snakeX[i] && y == gamePanel.snakeY[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get wall proximity: 0=far, 1=medium, 2=close, 3=very close
     */
    private int getWallProximity(int x, int y) {
        int minDist = Math.min(
                Math.min(x, gamePanel.screenWidth - x),
                Math.min(y, gamePanel.screenHeight - y)
        );

        if (minDist < 64) return 3;  // very close (< 2 tiles)
        if (minDist < 128) return 2; // close (< 4 tiles)
        if (minDist < 192) return 1; // medium
        return 0; // far from walls
    }

    private int[] getPositionInDirection(int x, int y, Direction dir, int distance) {
        switch (dir) {
            case UP: return new int[]{x, y - distance};
            case DOWN: return new int[]{x, y + distance};
            case LEFT: return new int[]{x - distance, y};
            case RIGHT: return new int[]{x + distance, y};
        }
        return new int[]{x, y};
    }

    // Helper methods from original agent
    public boolean dangerStraight(Direction d, int tile) {
        int[] next = nextPosition(gamePanel.snakeX[0], gamePanel.snakeY[0], d, tile);
        return isDangerAt(next[0], next[1]);
    }

    public boolean dangerLeft(Direction d, int tile) {
        Direction leftDir = turnLeft(d);
        int[] next = nextPosition(gamePanel.snakeX[0], gamePanel.snakeY[0], leftDir, tile);
        return isDangerAt(next[0], next[1]);
    }

    public boolean dangerRight(Direction d, int tile) {
        Direction rightDir = turnRight(d);
        int[] next = nextPosition(gamePanel.snakeX[0], gamePanel.snakeY[0], rightDir, tile);
        return isDangerAt(next[0], next[1]);
    }

    private boolean isDangerAt(int x, int y) {
        if (x < 0 || x >= gamePanel.screenWidth || y < 0 || y >= gamePanel.screenHeight) {
            return true;
        }
        for (int i = 0; i < gamePanel.bodyParts; i++) {
            if (x == gamePanel.snakeX[i] && y == gamePanel.snakeY[i]) {
                return true;
            }
        }
        return false;
    }

    private int[] nextPosition(int snakeHeadX, int snakeHeadY, Direction d, int tile) {
        int nextX = snakeHeadX;
        int nextY = snakeHeadY;
        switch (d) {
            case UP -> nextY -= tile;
            case DOWN -> nextY += tile;
            case LEFT -> nextX -= tile;
            case RIGHT -> nextX += tile;
        }
        return new int[]{nextX, nextY};
    }

    private Direction turnLeft(Direction d) {
        return switch (d) {
            case UP -> Direction.LEFT;
            case DOWN -> Direction.RIGHT;
            case LEFT -> Direction.DOWN;
            case RIGHT -> Direction.UP;
        };
    }

    private Direction turnRight(Direction d) {
        return switch (d) {
            case UP -> Direction.RIGHT;
            case DOWN -> Direction.LEFT;
            case LEFT -> Direction.UP;
            case RIGHT -> Direction.DOWN;
        };
    }

    public int chooseAction(int state) {
        // Smart exploration
        if (random.nextDouble() < epsilon) {
            // Decode danger bits for smart exploration
            boolean dangerS = ((state >> 8) & 1) == 1;
            boolean dangerL = ((state >> 7) & 1) == 1;
            boolean dangerR = ((state >> 6) & 1) == 1;

            // Get apple quadrant
            int appleQuad = (state >> 9) & 0b11;

            double[] weights = new double[3];
            weights[0] = dangerS ? 0.1 : 1.0;
            weights[1] = dangerL ? 0.1 : 1.0;
            weights[2] = dangerR ? 0.1 : 1.0;

            // Bias toward apple (quadrant 0 or 1 means apple is ahead)
            if (appleQuad <= 1) {
                weights[0] *= 3.0; // Favor going straight if apple ahead
            }

            double total = weights[0] + weights[1] + weights[2];
            double r = random.nextDouble() * total;

            if (r < weights[0]) return 0;
            if (r < weights[0] + weights[1]) return 1;
            return 2;
        }
        double[] q = qTable[state];
        if (q[0] >= q[1] && q[0] >= q[2]) return 0;
        return (q[1] >= q[2]) ? 1 : 2;
    }

    public void updateQTable(int state, int action, double reward, int nextState) {
        double maxQ = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < ACTION_SIZE; ++i) {
            maxQ = Math.max(maxQ, qTable[nextState][i]);
        }
        double currentQ = qTable[state][action];
        qTable[state][action] = currentQ + alpha * (reward + gamma * maxQ - currentQ);
    }

}