package Entity;

import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class Apple {
    GamePanel panel;

    Image appleImg;
    Random random;

    public int appleX, appleY;

    public Apple(GamePanel panel) {
        random = new Random();

        this.panel = panel;
        setImage();
    }

    public void newApple() {
        appleX = random.nextInt(panel.screenWidth / panel.tileSize) * panel.tileSize;
        appleY = random.nextInt(panel.screenHeight / panel.tileSize) * panel.tileSize;

        if (appleY == 0) {
            appleY += 2 * panel.tileSize;
        }
    }

    public void setImage() {
        try {
            appleImg = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("apple/apple.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void draw(Graphics2D graphics2D) {
        graphics2D.drawImage(appleImg, appleX, appleY, panel.tileSize, panel.tileSize, null);
    }
}
