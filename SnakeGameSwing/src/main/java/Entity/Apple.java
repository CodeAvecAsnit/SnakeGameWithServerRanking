package Entity;

import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

/**
 * @author : Asnit Bakhati
 * This class deals with all the Apple Generation and draws the image of Apple in AppleX and AppleY
 */
public class Apple {
    //Inject the main Game Panel
    private final GamePanel panel;
    //Load the Apple Image from files
    private Image appleImg;
    //Import class Random for generating random position for apples
    private final Random random;

    //X-Coordinate of apple
    private int appleX;
    //Y-Coordinate of apple
    private int appleY;

    //load using constructor
    public Apple(GamePanel panel) {
        this.random = new Random();
        this.panel = panel;
        setImage();
    }

    //load new apple in new position
    public void newApple() {
        appleX = random.nextInt(panel.maxScreenColUnit) * panel.tileSize;
        appleY = random.nextInt(panel.screenHeight / panel.tileSize) * panel.tileSize;

        if (appleY == 0) {
            appleY += 2 * panel.tileSize;//nothing much 0 is reserved space for score and trophy so try prevent generating apple there.
        }
    }

    //load image in Image object
    public void setImage() {
        try {
            appleImg = ImageIO.read(Objects.
                    requireNonNull(getClass().
                    getClassLoader().
                            getResourceAsStream("apple/apple.png")));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //draw the image
    public void draw(Graphics2D graphics2D) {
        graphics2D.drawImage(appleImg, appleX, appleY, panel.tileSize, panel.tileSize, null);
    }

    //set apple position at the start;
    public void setAppleStart() {
        this.appleX = 3 * (panel.maxScreenColUnit/5) * panel.tileSize;//2/3rds of the screen
        this.appleY = 12 * panel.tileSize;//same position as the head
    }

    public int getAppleX(){return this.appleX;}
    public int getAppleY(){return this.appleY;}
}
