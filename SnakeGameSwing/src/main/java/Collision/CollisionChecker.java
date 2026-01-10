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

    // Check if apple was eaten and handle it
    public boolean checkAppleCollision() {
        if (panel.snakeX[0] == apple.getAppleX() && panel.snakeY[0] == apple.getAppleY()) {
            appleEaten++;
            panel.bodyParts++;

            // Increase speed every 5 apples (only if not too fast)
            if (panel.snakeSpeed > 75) {
                if (appleEaten % 5 == 0) {
                    panel.snakeSpeed -= 10;
                }
            }

            // Generate new apple
            apple.newApple();
            return true;
        }
        return false;
    }

    // Check if snake has died (collision with self or walls)
    public boolean isSnakeDead() {
        // Check self-collision (head hitting body)
        for (int i = panel.bodyParts - 1; i > 0; i--) {
            if (panel.snakeX[0] == panel.snakeX[i] && panel.snakeY[0] == panel.snakeY[i]) {
                return true;
            }
        }

        // Check wall collision
        if (panel.snakeY[0] < 0 || panel.snakeY[0] >= panel.screenHeight) {
            return true;
        }
        if (panel.snakeX[0] < 0 || panel.snakeX[0] >= panel.screenWidth) {
            return true;
        }

        return false;
    }

    public int getAppleEaten() {
        return this.appleEaten;
    }

    public void setAppleEaten(int val) {
        this.appleEaten = val;
    }
}