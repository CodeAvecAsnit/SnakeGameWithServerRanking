package Collision;

import Entity.Apple;
import Main.GamePanel;

public class CollisionChecker {
    GamePanel panel;
    Apple apple;

    public int appleEaten = 0;

    public CollisionChecker(GamePanel panel, Apple apple) {
        this.panel = panel;
        this.apple = apple;
    }

    public void checkAppleCollision() {
        if (panel.snakeX[0] == apple.appleX && panel.snakeY[0] == apple.appleY) {
            appleEaten++;
            panel.bodyParts++;
            if (panel.snakeSpeed > 75) {
                if (appleEaten % 5 == 0)
                    panel.snakeSpeed -= 10;
            }
            apple.newApple();
            panel.playSoundEffects(2);
        }
    }

    public void checkSnakeCollision() {
        for (int i = panel.bodyParts - 1; i > 0; i--) {
            if (panel.snakeX[0] == panel.snakeX[i] && panel.snakeY[0] == panel.snakeY[i]) {
                panel.gameOn = false;
                panel.playSoundEffects(1);
                break;
            }
        }

        if (panel.snakeY[0] < 0 || panel.snakeY[0] >= panel.screenHeight) {
            panel.gameOn = false;
            panel.playSoundEffects(1);
        }

        if (panel.snakeX[0] < 0 || panel.snakeX[0] >= panel.screenWidth) {
            panel.gameOn = false;
            panel.playSoundEffects(1);
        }
    }
}
