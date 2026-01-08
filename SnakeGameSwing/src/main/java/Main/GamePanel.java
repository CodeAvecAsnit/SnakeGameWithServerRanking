package Main;

import Collision.CollisionChecker;
import Entity.Apple;
import Entity.Snake;
import Extra.GameOver;
import Tiles.TilesManager;
import UI.UI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Extra.Direction;

import static Extra.Direction.RIGHT;

public class GamePanel extends JPanel implements ActionListener {

    public final int maxScreenRowUnit = 25;
    public final int maxScreenColUnit = 25;

    private final int unitSize = 16;
    private final int scale = 2;
    public final int tileSize = scale * unitSize;

    public final int screenWidth = maxScreenColUnit * tileSize;
    public final int screenHeight = maxScreenRowUnit * tileSize;
    public final int gameUnits =maxScreenColUnit*maxScreenRowUnit;


    public Direction direction;

    public int snakeSpeed = 10;

    public int bodyParts = 3;
    public int[] snakeX;
    public int[] snakeY;
    public Direction[] snakeDir;

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

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.addKeyListener(handler);
        this.setFocusable(true);
        startGame();
    }

    public void startGame() {
        snakeX = new int[gameUnits];
        snakeY = new int[gameUnits];
        snakeDir = new Direction[gameUnits];

        snakeX[0] = 4 * tileSize;
        snakeX[1] = 3 * tileSize;
        snakeX[2] = 2 * tileSize;

        snakeY[0] = 12 * tileSize;
        snakeY[1] = 12 * tileSize;
        snakeY[2] = 12 * tileSize;

        snakeDir[0] = RIGHT;
        snakeDir[1] = RIGHT;
        snakeDir[2] = RIGHT;

        bodyParts = 3;
        direction = RIGHT;
        gameOn = true;
        gameStart = true;
        apple.setAppleStart();
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
            case RIGHT:
                snakeX[0] += tileSize;
                snakeDir[0] = RIGHT;
                break;
            case LEFT:
                snakeX[0] -= tileSize;
                snakeDir[0] = Direction.LEFT;
                break;
            case UP:
                snakeY[0] -= tileSize;
                snakeDir[0] =Direction.UP;
                break;
            case DOWN:
                snakeY[0] += tileSize;
                snakeDir[0] = Direction.DOWN;
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
            if(collisionChecker.isSnakeDead()){
                gameOn=false;
                apple.setAppleStart();
            }
        }
        repaint();
    }

    public int getScoreFromUI(){
        return ui.getScore();
    }

    public int getAppleEatenFromCollision(){
        return collisionChecker.getAppleEaten();
    }

    public void setAppleEatenFromCollision(int num){
        collisionChecker.setAppleEaten(num);
    }

    public Direction getDirection(){
        return this.direction;
    }

    public void setDirection(Direction direction){
        this.direction = direction;
    }

    public boolean checkSnakeDead(){
        return collisionChecker.isSnakeDead();
    }

    public boolean checkAppleEaten(){
        return collisionChecker.checkAppleCollision();
    }

    public int getManhattanDistance(){
        return Math.abs(apple.getAppleX()-snakeX[0])+Math.abs(apple.getAppleY()-snakeY[0]);
    }

    public int getAppleX(){
        return apple.getAppleX();
    }

    public int getAppleY(){
        return apple.getAppleY();
    }

}
