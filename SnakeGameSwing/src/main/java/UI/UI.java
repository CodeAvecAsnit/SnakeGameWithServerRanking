package UI;

import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class UI {

    private GamePanel panel;
    private Image apple;
    private Image trophy;

    int trophyEarned = 0;
    public int score = 0;

    boolean scoreSet = false;

    private Font font;

    public UI(GamePanel panel) {
        this.panel = panel;
        font = new Font("Space Mono", Font.BOLD, 23);
        setImage();
        this.trophyEarned = getTrophyEarned();
    }

    public int getTrophyEarned() {
        File file = new File("highscore.txt");

        if (!file.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("0");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 0;
        }

        try (Scanner scanner = new Scanner(file)) {
            if (scanner.hasNextLine()) {
                String content = scanner.nextLine().trim();
                return Integer.parseInt(content);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
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
        graphics2D.drawString(" " + panel.collisionChecker.appleEaten,
                panel.tileSize + panel.tileSize / 4,
                panel.tileSize - 2);

        if (!panel.gameOn) {
            if (panel.collisionChecker.appleEaten > trophyEarned) {
                trophyEarned = panel.collisionChecker.appleEaten;
                try {
                    File file = new File("username.txt");
                    Scanner scanner = new Scanner(file);
                    String username = scanner.hasNextLine()?scanner.nextLine().trim():null;
                    sendPutRequest(username, trophyEarned);
                }catch (Exception ex){
                    System.out.println(ex.getMessage());
                }

                File file = new File("highscore.txt");
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write(String.valueOf(trophyEarned));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (!scoreSet) {
                score = panel.collisionChecker.appleEaten;
                scoreSet = true;
            }
            panel.collisionChecker.appleEaten = 0;

        } else {
            score = 0;
            scoreSet = false;
        }

        graphics2D.drawImage(trophy,
                panel.tileSize * 4, panel.tileSize / 4,
                panel.tileSize - 8, panel.tileSize - 8, null);
        graphics2D.drawString(" " + trophyEarned,
                4 * panel.tileSize + panel.tileSize - 9,
                panel.tileSize - 2);
    }

    private void sendPutRequest(String username, int score) {
        try {
            String urlString = "http://localhost:8080/api/snakeGame/update/" + username + "," + score;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            int responseCode = conn.getResponseCode();
            System.out.println("PUT Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Score updated successfully!");
            } else {
                System.out.println("Failed to update score.");
            }

            conn.disconnect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}