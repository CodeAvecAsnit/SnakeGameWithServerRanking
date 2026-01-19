package UI;

import Main.APIRequest;
import Main.DatabaseManager;
import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;

/**
 * @author : Asnit Bakhati
 */
public class UI {

    private final GamePanel panel;

    //image of the apple
    private Image apple;

    //image of the trophy
    private Image trophy;

    private int trophyEarned;

    private int score;

    private boolean scoreSet = false;

    private Font font;

    private DatabaseManager dbm;

    public UI(GamePanel panel) {
        this.score = 0;
        this.panel = panel;
        this.dbm = new DatabaseManager();
        font = new Font("Space Mono", Font.BOLD, 23);
        setImage();
        this.trophyEarned = getTrophyEarned();
    }

    public int getTrophyEarned() {
        try {
            return dbm.getHighScore();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void setImage() {
        try {
            apple = ImageIO.read(getClass().getClassLoader().getResourceAsStream("apple/apple.png"));
            trophy = ImageIO.read(getClass().getClassLoader().getResourceAsStream("extras/trophy.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(Color.WHITE);
        graphics2D.setFont(font);

        graphics2D.drawImage(apple, panel.tileSize / 2, panel.tileSize / 4, panel.tileSize - 8, panel.tileSize - 8, null);
        graphics2D.drawString(" " + panel.getAppleEatenFromCollision(),
                panel.tileSize + panel.tileSize / 4, panel.tileSize - 2);

        graphics2D.drawImage(trophy, panel.tileSize * 4, panel.tileSize / 4, panel.tileSize - 8, panel.tileSize - 8, null);
        graphics2D.drawString(" " + trophyEarned, 4 * panel.tileSize + panel.tileSize - 9, panel.tileSize - 2);
    }


    public void handleGameOverScore() {
        int currentScore = panel.getAppleEatenFromCollision();

        if (currentScore > trophyEarned) {
            trophyEarned = currentScore;
            saveHighScore(trophyEarned);
        }
        new Thread(() -> {
            try {
                String username = dbm.getStoredUsername();
                APIRequest.sendPutRequest(username, trophyEarned);
            } catch (Exception ex) {
                System.out.println("Server unreachable, score saved locally only.");
            }
        }).start();
    }

    public int getScore() {
        return panel.getAppleEatenFromCollision();
    }

    private void saveHighScore(int value) {
        try {
            dbm.updateHighScore(value);
        } catch (Exception e) {
            System.out.println("Error saving high score: " + e.getMessage());
            e.printStackTrace();
        }
    }
}