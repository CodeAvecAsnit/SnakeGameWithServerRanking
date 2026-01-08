package Main;

import Extra.Direction;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Objects;

/**
 * @author : Asnit Bakhati
 */
public class KeyHandler extends KeyAdapter {

    private final GamePanel panel;

    public KeyHandler(GamePanel panel) {
        this.panel = panel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (!Objects.equals(panel.getDirection(), Direction.DOWN)) panel.setDirection(Direction.UP);
                if (!panel.gameStart && panel.gameOn) {
                    panel.gameStart = true;
                    panel.timer.setDelay(panel.snakeSpeed);
                }
                break;

            case KeyEvent.VK_DOWN:
                if (!Objects.equals(panel.getDirection(), Direction.UP)) panel.setDirection(Direction.DOWN);
                if (!panel.gameStart && panel.gameOn) {
                    panel.gameStart = true;
                    panel.timer.setDelay(panel.snakeSpeed);
                }
                break;

            case KeyEvent.VK_LEFT:
                if (!Objects.equals(panel.getDirection(), Direction.RIGHT)) panel.setDirection(Direction.LEFT);
                if (!panel.gameStart && panel.gameOn) {
                    panel.gameStart = true;
                    panel.timer.setDelay(panel.snakeSpeed);
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (!Objects.equals(panel.getDirection(), Direction.LEFT)) panel.setDirection(Direction.RIGHT);
                if (!panel.gameStart && panel.gameOn) {
                    panel.gameStart = true;
                    panel.timer.setDelay(panel.snakeSpeed);
                }
                break;
            case KeyEvent.VK_SPACE:

                if (!panel.gameOn) {
                    panel.gameOn = true;
                    panel.gameStart = false;

                    panel.bodyParts = 3;
                    panel.setDirection(Direction.RIGHT);

                    panel.snakeX[0] = 4 * panel.tileSize;
                    panel.snakeX[1] = 3 * panel.tileSize;
                    panel.snakeX[2] = 2 * panel.tileSize;

                    panel.snakeY[0] = 12 * panel.tileSize;
                    panel.snakeY[1] = 12 * panel.tileSize;
                    panel.snakeY[2] = 12 * panel.tileSize;

                    panel.snakeDir[0] = panel.getDirection();
                    panel.snakeDir[1] = panel.getDirection();
                    panel.snakeDir[2] = panel.getDirection();

                    panel.snakeSpeed = 155;
                }
                break;
        }
    }
}
