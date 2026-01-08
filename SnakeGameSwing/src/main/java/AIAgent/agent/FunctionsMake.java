package AIAgent.agent;

import Main.GamePanel;

public class FunctionsMake {

    private final GamePanel gamePanel;

    public FunctionsMake(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

//reward design for shortest path use when size < 10
    public double performActionGreedy(int Action){
        int oldDistance = gamePanel.getManhattanDistance();
        // take action here by selection random or best action(call function perform(action);
        if(gamePanel.checkSnakeDead()) return -100.0;

        if(gamePanel.checkAppleEaten()) return 10.0;

        return (gamePanel.getManhattanDistance()<oldDistance)? 1 : -1;
    }

 //TODO : calculate the amount of last apple eaten if that is more than gridx*gridy times than return -10000;
  // reason : RL agent learn the safest policy so for larger inputs they learn that the continuing in same loop is safest option so
    public double performAction(int Action){
        if(gamePanel.checkSnakeDead()) return -100.0;

        return (gamePanel.checkAppleEaten())? 10.0:-0.01;
    }


    public int getAction() {
        int snakeHeadX = gamePanel.snakeX[0];
        int snakeHeadY = gamePanel.snakeY[0];
        int appleX = gamePanel.getAppleX();
        int appleY = gamePanel.getAppleY();

        return  (appleStraight()  ? 1 : 0) << 5 |
                (appleLeft()      ? 1 : 0) << 4 |
                (appleRight()     ? 1 : 0) << 3 |
                (dangerStraight() ? 1 : 0) << 2 |
                (dangerLeft()     ? 1 : 0) << 1 |
                (dangerRight()    ? 1 : 0);
    }


    boolean appleRight(){
        return true;
    }

    boolean appleLeft(){
        return true;
    }

    boolean appleStraight(){
        return true;
    }

    boolean dangerLeft(){
        return true;
    }

    boolean dangerRight(){
        return true;
    }

    boolean dangerStraight(){
        return true;
    }




}
