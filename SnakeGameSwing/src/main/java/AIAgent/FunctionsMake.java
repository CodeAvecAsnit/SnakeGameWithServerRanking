package AIAgent;

import Main.GamePanel;

public class FunctionsMake {

    private final GamePanel gamePanel;

    public FunctionsMake(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

//reward design for shortest path use when size < 10
    public double performAction(int Action){
        int oldDistance =1;
        // take action here by selection random or best action(call function perform(action);
        if(gamePanel.checkSnakeDead()) return -100.0;

        if(gamePanel.checkAppleEaten()) return 10.0;


return 0;


    }



}
