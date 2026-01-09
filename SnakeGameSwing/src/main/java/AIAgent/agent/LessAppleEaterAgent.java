package AIAgent.agent;


import AIAgent.component.SnakeLearningAgent;
import Main.GamePanel;

public class LessAppleEaterAgent extends SnakeLearningAgent {

    public LessAppleEaterAgent(GamePanel gamePanel) {
        super(gamePanel);
    }

    @Override
    public double performAction(int Action){
        int oldDistance = gamePanel.getManhattanDistance();
        // take action here by selection random or best action(call function perform(action);
        if(gamePanel.checkSnakeDead()) return -100.0;

        if(gamePanel.checkAppleEaten()) return 10.0;

        return (gamePanel.getManhattanDistance()<oldDistance)? 0.1 : -0.1;
    }
}