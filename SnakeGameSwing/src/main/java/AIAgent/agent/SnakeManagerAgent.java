package AIAgent.agent;

import AIAgent.component.QLearningAgent;
import Extra.Direction;
import Main.GamePanel;

public abstract class SnakeManagerAgent extends QLearningAgent {
    public static final int STATES_SIZE = 4096;
    public static final int ACTION_SIZE = 3;
    private static final String FILE_NAME = "snakeTableVast.bin";
    protected final GamePanel gamePanel;

    public SnakeManagerAgent(GamePanel gamePanel) {

        super(0.1, 0.99, 1.0, STATES_SIZE, ACTION_SIZE, FILE_NAME);
        this.gamePanel = gamePanel;
    }

    /**
     * FLOOD FILL: Calculates reachable empty tiles.
     */
    public int countAvailableSpace(int startX, int startY) {
        int cols = gamePanel.maxScreenColUnit;
        int rows = gamePanel.maxScreenRowUnit;
        boolean[][] visited = new boolean[cols][rows];
        int tileSize = gamePanel.tileSize;

        for (int i = 0; i < gamePanel.bodyParts; i++) {
            int ix = gamePanel.snakeX[i] / tileSize;
            int iy = gamePanel.snakeY[i] / tileSize;
            if (ix >= 0 && ix < cols && iy >= 0 && iy < rows) visited[ix][iy] = true;
        }
        return performFloodFill(startX / tileSize, startY / tileSize, visited);
    }


    private int performFloodFill(int x, int y, boolean[][] visited) {
        if (x < 0 || x >= visited.length || y < 0 || y >= visited[0].length || visited[x][y]) return 0;
        visited[x][y] = true;
        return 1 + performFloodFill(x + 1, y, visited) + performFloodFill(x - 1, y, visited) +
                performFloodFill(x, y + 1, visited) + performFloodFill(x, y - 1, visited);
    }


    public int getState() {
        Direction dir = gamePanel.getDirection();
        int tile = gamePanel.tileSize;
        int headX = gamePanel.snakeX[0];
        int headY = gamePanel.snakeY[0];

        int[] nextPos = nextPosition(headX, headY, dir, tile);
        int isTrapped = (countAvailableSpace(nextPos[0], nextPos[1]) < gamePanel.bodyParts) ? 1 : 0;

        int dS = dangerStraight(dir, tile) ? 1 : 0;
        int dL = dangerLeft(dir, tile) ? 1 : 0;
        int dR = dangerRight(dir, tile) ? 1 : 0;

        int lookS = dangerAtDistance(dir, tile * 2) ? 1 : 0;
        int lookL = dangerAtDistance(turnLeft(dir), tile * 2) ? 1 : 0;
        int lookR = dangerAtDistance(turnRight(dir), tile * 2) ? 1 : 0;

        int appleQuad = getAppleQuadrant(headX, headY, gamePanel.getAppleX(), gamePanel.getAppleY(), dir);

        int wallProx = getWallProximity(headX, headY); // Returns 0-3

        return (isTrapped << 11) | (dS << 10) | (dL << 9) | (dR << 8) |
                (lookS << 7) | (lookL << 6) | (lookR << 5) |
                (appleQuad << 3) | (wallProx & 0x07);
    }



    public int chooseAction(int state) {
        if (random.nextDouble() < epsilon) return random.nextInt(ACTION_SIZE);
        double[] qValues = qTable[state];
        int bestAction = 0;
        for (int i = 1; i < ACTION_SIZE; i++) if (qValues[i] > qValues[bestAction]) bestAction = i;
        return bestAction;
    }



    public void updateQTable(int state, int action, double reward, int nextState) {
        double maxNextQ = 0;
        for (double v : qTable[nextState]) maxNextQ = Math.max(maxNextQ, v);
        qTable[state][action] += alpha * (reward + gamma * maxNextQ - qTable[state][action]);
    }



    public void moveRelative(int action) {
        Direction dir = gamePanel.direction;
        gamePanel.direction = switch (action) {
            case 1 -> turnLeft(dir);
            case 2 -> turnRight(dir);
            default -> dir;
        };
        gamePanel.move();
    }



    private Direction turnLeft(Direction d) {
        return switch (d) {
            case UP -> Direction.LEFT;
            case LEFT -> Direction.DOWN;
            case DOWN -> Direction.RIGHT;
            case RIGHT -> Direction.UP;
        };
    }



    private Direction turnRight(Direction d) {
        return switch (d) {
            case UP -> Direction.RIGHT;
            case RIGHT -> Direction.DOWN;
            case DOWN -> Direction.LEFT;
            case LEFT -> Direction.UP;
        };
    }


    protected int[] nextPosition(int x, int y, Direction d, int t) {
        return switch (d) {
            case UP -> new int[]{x, y-t};
            case DOWN -> new int[]{x, y+t};
            case LEFT -> new int[]{x-t, y};
            case RIGHT -> new int[]{x+t, y}; };
    }


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
        for (int i = 0; i < gamePanel.bodyParts - 1; i++) {
            if (x == gamePanel.snakeX[i] && y == gamePanel.snakeY[i]) return true;
        }
        return false;
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


    private int getDistanceCategory(int distance) {
        if (distance < 96) return 0;  // close (< 3 tiles)
        if (distance < 192) return 1; // medium (3-6 tiles)
        if (distance < 320) return 2; // far (6-10 tiles)
        return 3; // very far (>10 tiles)
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



    /**
     * Check if body is 2-3 tiles ahead (early warning)
     */
    private boolean dangerAtDistance(Direction d, int distance) {
        int headX = gamePanel.snakeX[0];
        int headY = gamePanel.snakeY[0];

        int checkX = headX;
        int checkY = headY;

        switch (d) {
            case UP -> checkY -= distance;
            case DOWN -> checkY += distance;
            case LEFT -> checkX -= distance;
            case RIGHT -> checkX += distance;
        }

        return isDangerAt(checkX, checkY);
    }


    public abstract double performAction(int action);
}