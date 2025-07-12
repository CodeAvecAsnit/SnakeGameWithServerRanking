package Main;

import Collision.CollisionChecker;
import Entity.Apple;
import Entity.Snake;
import Extra.GameOver;
import SoundEffects.Sound;
import Tiles.TilesManager;
import UI.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements ActionListener {
    public final int maxScreenRowUnit = 24;
    public final int maxScreenColUnit = 21;

    final int unitSize = 16;
    final int scale = 2;
    public final int tileSize = scale * unitSize;

    public final int screenWidth = maxScreenColUnit * tileSize;
    public final int screenHeight = maxScreenRowUnit * tileSize;
    public final int gameUnits = (screenWidth * screenHeight) / tileSize;

    public int snakeSpeed = 155;
    public String direction = "right";

    public int bodyParts = 3;

    public boolean soundOn = true;

    public int[] snakeX;
    public int[] snakeY;
    public String[] snakeDir;

    Timer timer;

    public boolean gameStart = false;
    public boolean gameOn = false;

    KeyHandler handler = new KeyHandler(this);
    Apple apple = new Apple(this);
    Snake snake = new Snake(this);
    public CollisionChecker collisionChecker = new CollisionChecker(this, apple);
    TilesManager manager = new TilesManager(this);
    public UI ui = new UI(this);
    GameOver gameOver = new GameOver(this);
    Sound sound = new Sound();

    public GamePanel() {
        snakeX = new int[gameUnits];
        snakeY = new int[gameUnits];
        snakeDir = new String[gameUnits];

        snakeX[0] = 4 * tileSize;
        snakeX[1] = 3 * tileSize;
        snakeX[2] = 2 * tileSize;

        snakeY[0] = 12 * tileSize;
        snakeY[1] = 12 * tileSize;
        snakeY[2] = 12 * tileSize;

        snakeDir[0] = direction;
        snakeDir[1] = direction;
        snakeDir[2] = direction;

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.addKeyListener(handler);
        this.setFocusable(true);

        startGame();
    }

    public void startGame() {
        snakeX = new int[gameUnits];
        snakeY = new int[gameUnits];
        snakeDir = new String[gameUnits];

        snakeX[0] = 4 * tileSize;
        snakeX[1] = 3 * tileSize;
        snakeX[2] = 2 * tileSize;

        snakeY[0] = 12 * tileSize;
        snakeY[1] = 12 * tileSize;
        snakeY[2] = 12 * tileSize;

        snakeDir[0] = "right";
        snakeDir[1] = "right";
        snakeDir[2] = "right";

        bodyParts = 3;
        direction = "right";
        gameOn = true;
        gameStart = true;

        apple.newApple();

        if (timer != null) {
            timer.stop();
        }

        timer = new Timer(snakeSpeed, this);
        timer.start();

        requestFocusInWindow();
        repaint();
    }

    public void paint(Graphics graphics) {
        super.paint(graphics);
        draw(graphics);
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            snakeX[i] = snakeX[i - 1];
            snakeY[i] = snakeY[i - 1];
            snakeDir[i] = snakeDir[i - 1];
        }

        switch (direction) {
            case "right":
                snakeX[0] += tileSize;
                snakeDir[0] = "right";
                break;
            case "left":
                snakeX[0] -= tileSize;
                snakeDir[0] = "left";
                break;
            case "up":
                snakeY[0] -= tileSize;
                snakeDir[0] = "up";
                break;
            case "down":
                snakeY[0] += tileSize;
                snakeDir[0] = "down";
                break;
        }
    }

    public void draw(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;

        manager.draw(graphics2D);

        if (gameOn) {
            apple.draw(graphics2D);

            snake.draw(graphics2D);
        } else {
           ui.draw(graphics2D);
           gameOver.draw(graphics2D,screenWidth,screenHeight);
        }

        ui.draw(graphics2D);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameStart && gameOn) {
            timer.setDelay(snakeSpeed);
            move();
            collisionChecker.checkAppleCollision();
            collisionChecker.checkSnakeCollision();
        }
        repaint();
    }

    public void playSoundEffects(int i) {
        if (soundOn) {
            sound.setFile(i);
            sound.play();
        }
    }
}
