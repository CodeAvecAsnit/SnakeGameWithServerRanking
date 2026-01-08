package Entity;

import Extra.Direction;
import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class Snake {

    private final GamePanel panel;
    private final Image[] snakeImg;

    public Snake(GamePanel panel) {
        this.panel = panel;

        snakeImg = new Image[14];

        setImage();
    }

    public void setImage() {
        try {
            // Load head images
            snakeImg[0] = ImageIO.read(getClass().getClassLoader().getResourceAsStream("snake/head_up.png"));
            snakeImg[1] = ImageIO.read(getClass().getClassLoader().getResourceAsStream("snake/head_down.png"));
            snakeImg[2] = ImageIO.read(getClass().getClassLoader().getResourceAsStream("snake/head_left.png"));
            snakeImg[3] = ImageIO.read(getClass().getClassLoader().getResourceAsStream("snake/head_right.png"));

            // Load body images
            snakeImg[4] = ImageIO.read(getClass().getClassLoader().getResourceAsStream("snake/body_horizontal.png"));
            snakeImg[5] = ImageIO.read(getClass().getClassLoader().getResourceAsStream("snake/body_vertical.png"));
            snakeImg[6] = ImageIO.read(getClass().getClassLoader().getResourceAsStream("snake/body_bottomleft.png"));
            snakeImg[7] = ImageIO.read(getClass().getClassLoader().getResourceAsStream("snake/body_topleft.png"));
            snakeImg[8] = ImageIO.read(getClass().getClassLoader().getResourceAsStream("snake/body_bottomright.png"));
            snakeImg[9] = ImageIO.read(getClass().getClassLoader().getResourceAsStream("snake/body_topright.png"));

            // Load tail images
            snakeImg[10] = ImageIO.read(getClass().getClassLoader().getResourceAsStream("snake/tail_up.png"));
            snakeImg[11] = ImageIO.read(getClass().getClassLoader().getResourceAsStream("snake/tail_down.png"));
            snakeImg[12] = ImageIO.read(getClass().getClassLoader().getResourceAsStream("snake/tail_left.png"));
            snakeImg[13] = ImageIO.read(getClass().getClassLoader().getResourceAsStream("snake/tail_right.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D graphics2D) {
        for (int i = 0; i < panel.bodyParts; i++) {
            if (i == 0) {
                switch (panel.getDirection()) {
                    case UP:
                        graphics2D.drawImage(snakeImg[0], panel.snakeX[i], panel.snakeY[i], panel.tileSize, panel.tileSize, null);
                        break;
                    case DOWN:
                        graphics2D.drawImage(snakeImg[1], panel.snakeX[i], panel.snakeY[i], panel.tileSize, panel.tileSize, null);
                        break;
                    case LEFT:
                        graphics2D.drawImage(snakeImg[2], panel.snakeX[i], panel.snakeY[i], panel.tileSize, panel.tileSize, null);
                        break;
                    case RIGHT:
                        graphics2D.drawImage(snakeImg[3], panel.snakeX[i], panel.snakeY[i], panel.tileSize, panel.tileSize, null);
                        break;
                }
            } else if (i == panel.bodyParts - 1) {
                if (!Objects.equals(panel.snakeDir[i], panel.snakeDir[i - 1])) {
                    switch (panel.snakeDir[i - 1]) {
                        case UP:
                            graphics2D.drawImage(snakeImg[11], panel.snakeX[i], panel.snakeY[i], panel.tileSize, panel.tileSize, null);
                            break;
                        case DOWN:
                            graphics2D.drawImage(snakeImg[10], panel.snakeX[i], panel.snakeY[i], panel.tileSize, panel.tileSize, null);
                            break;
                        case LEFT:
                            graphics2D.drawImage(snakeImg[13], panel.snakeX[i], panel.snakeY[i], panel.tileSize, panel.tileSize, null);
                            break;
                        case RIGHT:
                            graphics2D.drawImage(snakeImg[12], panel.snakeX[i], panel.snakeY[i], panel.tileSize, panel.tileSize, null);
                            break;
                    }
                } else {
                    switch (panel.snakeDir[i]) {
                        case UP:
                            graphics2D.drawImage(snakeImg[11], panel.snakeX[i], panel.snakeY[i], panel.tileSize, panel.tileSize, null);
                            break;
                        case DOWN:
                            graphics2D.drawImage(snakeImg[10], panel.snakeX[i], panel.snakeY[i], panel.tileSize, panel.tileSize, null);
                            break;
                        case LEFT:
                            graphics2D.drawImage(snakeImg[13], panel.snakeX[i], panel.snakeY[i], panel.tileSize, panel.tileSize, null);
                            break;
                        case RIGHT:
                            graphics2D.drawImage(snakeImg[12], panel.snakeX[i], panel.snakeY[i], panel.tileSize, panel.tileSize, null);
                            break;
                    }
                }
            } else {
                if (Objects.equals(panel.snakeDir[i], Direction.RIGHT) && Objects.equals(panel.snakeDir[i - 1], Direction.RIGHT) || Objects.equals(panel.snakeDir[i], Direction.LEFT) && Objects.equals(panel.snakeDir[i - 1], Direction.LEFT)) {
                    graphics2D.drawImage(snakeImg[4], panel.snakeX[i], panel.snakeY[i], panel.tileSize, panel.tileSize, null);
                }
                if (Objects.equals(panel.snakeDir[i], Direction.UP) && Objects.equals(panel.snakeDir[i - 1], Direction.UP) || Objects.equals(panel.snakeDir[i],Direction.DOWN) && Objects.equals(panel.snakeDir[i - 1], Direction.DOWN)) {
                    graphics2D.drawImage(snakeImg[5], panel.snakeX[i], panel.snakeY[i], panel.tileSize, panel.tileSize, null);
                }
                if (Objects.equals(panel.snakeDir[i], Direction.RIGHT) && Objects.equals(panel.snakeDir[i - 1], Direction.DOWN)) {
                    graphics2D.drawImage(snakeImg[6], panel.snakeX[i], panel.snakeY[i], panel.tileSize, panel.tileSize, null);
                }
                if (Objects.equals(panel.snakeDir[i], Direction.DOWN) && Objects.equals(panel.snakeDir[i - 1], Direction.RIGHT)) {
                    graphics2D.drawImage(snakeImg[9], panel.snakeX[i], panel.snakeY[i], panel.tileSize, panel.tileSize, null);
                }
                if (Objects.equals(panel.snakeDir[i], Direction.RIGHT) && Objects.equals(panel.snakeDir[i - 1], Direction.UP)) {
                    graphics2D.drawImage(snakeImg[7], panel.snakeX[i], panel.snakeY[i], panel.tileSize, panel.tileSize, null);
                }
                if (Objects.equals(panel.snakeDir[i],Direction. UP) && Objects.equals(panel.snakeDir[i - 1], Direction.RIGHT)) {
                    graphics2D.drawImage(snakeImg[8], panel.snakeX[i], panel.snakeY[i], panel.tileSize, panel.tileSize, null);
                }
                if (Objects.equals(panel.snakeDir[i], Direction.LEFT) && Objects.equals(panel.snakeDir[i - 1], Direction.UP)) {
                    graphics2D.drawImage(snakeImg[9], panel.snakeX[i], panel.snakeY[i], panel.tileSize, panel.tileSize, null);
                }
                if (Objects.equals(panel.snakeDir[i], Direction.UP) && Objects.equals(panel.snakeDir[i - 1], Direction.LEFT)) {
                    graphics2D.drawImage(snakeImg[6], panel.snakeX[i], panel.snakeY[i], panel.tileSize, panel.tileSize, null);
                }
                if (Objects.equals(panel.snakeDir[i], Direction.LEFT) && Objects.equals(panel.snakeDir[i - 1], Direction.DOWN)) {
                    graphics2D.drawImage(snakeImg[8], panel.snakeX[i], panel.snakeY[i], panel.tileSize, panel.tileSize, null);
                }
                if (Objects.equals(panel.snakeDir[i], Direction.DOWN) && Objects.equals(panel.snakeDir[i - 1], Direction.LEFT)) {
                    graphics2D.drawImage(snakeImg[7], panel.snakeX[i], panel.snakeY[i], panel.tileSize, panel.tileSize, null);
                }
            }
        }
    }
}
