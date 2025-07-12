package Main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Objects;

public class KeyHandler extends KeyAdapter {
    GamePanel panel;

    public KeyHandler(GamePanel panel) {
        this.panel = panel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                panel.playSoundEffects(0);
                if (!Objects.equals(panel.direction, "down")) panel.direction = "up";
                if (!panel.gameStart && panel.gameOn) {
                    panel.gameStart = true;
                    panel.timer.setDelay(panel.snakeSpeed);
                }
                break;
            case KeyEvent.VK_DOWN:
                panel.playSoundEffects(0);
                if (!Objects.equals(panel.direction, "up")) panel.direction = "down";
                if (!panel.gameStart && panel.gameOn) {
                    panel.gameStart = true;
                    panel.timer.setDelay(panel.snakeSpeed);
                }
                break;
            case KeyEvent.VK_LEFT:
                panel.playSoundEffects(0);
                if (!Objects.equals(panel.direction, "right")) panel.direction = "left";
                if (!panel.gameStart && panel.gameOn) {
                    panel.gameStart = true;
                    panel.timer.setDelay(panel.snakeSpeed);
                }
                break;
            case KeyEvent.VK_RIGHT:
                panel.playSoundEffects(0);
                if (!Objects.equals(panel.direction, "left")) panel.direction = "right";
                if (!panel.gameStart && panel.gameOn) {
                    panel.gameStart = true;
                    panel.timer.setDelay(panel.snakeSpeed);
                }
                break;
            case KeyEvent.VK_M:
                panel.soundOn = !panel.soundOn;
                break;
            case KeyEvent.VK_SPACE:
                if (!panel.gameOn) {
                    panel.gameOn = true;
                    panel.gameStart = false;

                    panel.bodyParts = 3;
                    panel.direction = "right";

                    panel.snakeX[0] = 4 * panel.tileSize;
                    panel.snakeX[1] = 3 * panel.tileSize;
                    panel.snakeX[2] = 2 * panel.tileSize;

                    panel.snakeY[0] = 12 * panel.tileSize;
                    panel.snakeY[1] = 12 * panel.tileSize;
                    panel.snakeY[2] = 12 * panel.tileSize;

                    panel.snakeDir[0] = panel.direction;
                    panel.snakeDir[1] = panel.direction;
                    panel.snakeDir[2] = panel.direction;

                    panel.snakeSpeed = 155;
                }
                break;
        }
    }

}
