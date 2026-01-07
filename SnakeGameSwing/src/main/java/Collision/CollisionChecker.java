package Collision;

import Entity.Apple;
import Main.GamePanel;

public class CollisionChecker {
    private final GamePanel panel;
    private final Apple apple;

    private int appleEaten;

    public CollisionChecker(GamePanel panel, Apple apple) {
        this.panel = panel;
        this.apple = apple;
        this.appleEaten = 0;
    }

    //reward function
    public boolean checkAppleCollision() {
        if (panel.snakeX[0] == apple.getAppleX() && panel.snakeY[0] == apple.getAppleY()) {
            appleEaten++;
            panel.bodyParts++;
            //increase speed of snake every five apples eaten and als
            if (panel.snakeSpeed > 75) {
                if (appleEaten % 5 == 0)
                    panel.snakeSpeed -= 10;
            }
            apple.newApple();
            return true;
        }
        return false;
    }

    //death function
    public boolean isSnakeDead(){
        for (int i = panel.bodyParts - 1; i > 0; i--) {
            if (panel.snakeX[0] == panel.snakeX[i] && panel.snakeY[0] == panel.snakeY[i]) {
                return true;
            }
        }
        if (panel.snakeY[0] < 0 || panel.snakeY[0] >= panel.screenHeight) {
            return true;
        }
        return (panel.snakeX[0] < 0 || panel.snakeX[0] >= panel.screenWidth);
    }

    public int getAppleEaten(){
        return this.appleEaten;
    }

    public void setAppleEaten(int val){
        this.appleEaten=val;
    }
}
