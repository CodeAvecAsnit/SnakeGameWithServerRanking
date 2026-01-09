package AIAgent.agent;

import AIAgent.component.SnakeLearningAgent;
import Main.GamePanel;

public class HighAppleEaterAgent extends SnakeLearningAgent {

    private int lastAppleEaten;

    public HighAppleEaterAgent(GamePanel gamePanel) {
        super(gamePanel);
        this.lastAppleEaten=0;
    }

    @Override
    public double performAction(int Action) {

        if(gamePanel.checkSnakeDead()){
            lastAppleEaten=0;
            return -100.0;
        }
        if(gamePanel.checkAppleEaten()) {
            lastAppleEaten=0;
            return 10.0;
        }
        ++lastAppleEaten;
        if(lastAppleEaten>(2*gamePanel.maxScreenRowUnit* gamePanel.maxScreenColUnit)){
            gamePanel.gameOn=false;
            lastAppleEaten=0;
            return -1000;
        }
        return -0.1;
    }
}
