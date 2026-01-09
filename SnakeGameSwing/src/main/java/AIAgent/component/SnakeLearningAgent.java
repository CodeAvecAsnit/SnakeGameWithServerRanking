package AIAgent.component;

import Extra.Direction;
import Main.GamePanel;

public abstract class SnakeLearningAgent extends QLearningAgent {
    public static final int STATES_SIZE = 64;
    public static final int ACTION_SIZE = 3;
    private static final String FILE_NAME = "snakeTable.bin";
    protected final GamePanel gamePanel;

    public SnakeLearningAgent(GamePanel gamePanel) {
        super(0.1,0.9,0.2,STATES_SIZE, ACTION_SIZE ,FILE_NAME);
        this.gamePanel = gamePanel;
    }

    public abstract double performAction(int Action);

    public int getState() {
        Direction currDirection = gamePanel.getDirection();
        int snakeHeadX = gamePanel.snakeX[0];
        int snakeHeadY = gamePanel.snakeY[0];
        int appleX = gamePanel.getAppleX();
        int appleY = gamePanel.getAppleY();
        int tile = gamePanel.tileSize;

        return  (appleStraight(snakeHeadX,snakeHeadY,appleX,appleY,currDirection)? 1 : 0)<< 5 |
                (appleLeft(snakeHeadX,snakeHeadY,appleX,appleY,currDirection) ? 1 : 0) << 4 |
                (appleRight(snakeHeadX,snakeHeadY,appleX,appleY,currDirection)? 1 : 0) << 3 |
                (dangerStraight(currDirection,tile)?1:0)<<2 |
                (dangerLeft(currDirection,tile) ?1:0)<<1 |
                (dangerRight(currDirection,tile)?1 :0);
    }

    private boolean appleStraight(int snakeHeadX, int snakeHeadY,
                                  int appleX, int appleY,
                                  Direction d) {
        switch (d) {
            case UP:  return (appleX == snakeHeadX && appleY < snakeHeadY);
            case DOWN: return (appleX == snakeHeadX && appleY > snakeHeadY);
            case LEFT: return (appleY == snakeHeadY && appleX < snakeHeadX);
            case RIGHT: return (appleY == snakeHeadY && appleX > snakeHeadX);
        }
        return false;
    }

    private boolean appleLeft(int snakeHeadX, int snakeHeadY,
                              int appleX, int appleY,
                              Direction d) {
        switch (d) {
            case UP: return (appleX < snakeHeadX);
            case DOWN: return (appleX > snakeHeadX);
            case LEFT: return (appleY > snakeHeadY);
            case RIGHT: return (appleY < snakeHeadY);
        }
        return false;
    }

    private boolean appleRight(int snakeHeadX, int snakeHeadY,
                               int appleX, int appleY,
                               Direction d) {
        switch (d) {
            case UP: return (appleX > snakeHeadX);
            case DOWN: return (appleX < snakeHeadX);
            case LEFT: return (appleY < snakeHeadY);
            case RIGHT:return (appleY > snakeHeadY);
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

    private Direction turnLeft(Direction d) {
        switch(d) {
            case UP: return Direction.LEFT;
            case DOWN: return Direction.RIGHT;
            case LEFT: return Direction.DOWN;
            case RIGHT: return Direction.UP;
        }
        return d;
    }

    private Direction turnRight(Direction d) {
        switch(d) {
            case UP: return Direction.RIGHT;
            case DOWN: return Direction.LEFT;
            case LEFT: return Direction.UP;
            case RIGHT: return Direction.DOWN;
        }
        return d;
    }

    private int getBestAct(double[] col){
        if(col[0]>=col[1] && col[0]>=col[2]) return 0;
        return (col[1]>=col[0] && col[1]>=col[2])? 1 : 2;
    }


    public int chooseAction(int state){
        if(random.nextDouble()<epsilon) return random.nextInt(ACTION_SIZE);
            return getBestAct(qTable[state]);
    }

    private static int max(int[] arr){
        return (arr[0]>arr[1])?arr[0]:arr[1];
    }



    /**
     * Update Q-Table using Q-Learning formula:
     * Q(s,a) = Q(s,a) + α[r + γ * max(Q(s',a')) - Q(s,a)]
     */
    public void updateQTable(int state, int action, double reward, int nextState){
        double maxQ = Double.NEGATIVE_INFINITY;
        for(int i = 0 ; i < ACTION_SIZE ; ++i){
            maxQ = Math.max(maxQ,qTable[state][i]);
        }
        double currentQ = qTable[state][action];
        qTable[state][action] =  currentQ + alpha*( reward + gamma * maxQ - currentQ);
    }


    public void trainEpisodes(){
        gamePanel.startGame();
        int totalReward = 0;

        while(!gamePanel.gameOn){
            int currentState = getState();
            int action = chooseAction(currentState);
            double reward = performAction(action);
            totalReward+= reward;
            int nextState = getState();
            updateQTable(currentState,action,reward,nextState);
        }
        System.out.println("Episode finished - Score: "+gamePanel.getScoreFromUI()+" , Total Reward: "+totalReward);
    }

    public void train(int episodes){
        System.out.println("Training started ");
        System.out.println("----------------------------------------");

        for(int episode = 1 ; episodes <= episodes ; ++episode){
            trainEpisodes();
            if(episode%1000==0) {
                if (epsilon > 0.2) epsilon *= 0.95;
            }
        }

        System.out.println("----------------------------------------");
        System.out.println("Training complete!");
    }
}
