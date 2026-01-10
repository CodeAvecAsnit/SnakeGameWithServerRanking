package UI;

import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * @author : Asnit Bakhati
 */
public class UI {

    private static final String FILE_PATH = "highScore.bin";

    //main singleton gamePanel
    private final GamePanel panel;

    //image of the apple
    private Image apple;

    //image of the trophy
    private Image trophy;

    private int trophyEarned ;

    private int score;

    private boolean scoreSet = false;

    private Font font;

    public UI(GamePanel panel) {
        this.score =  0 ;
        this.panel = panel;
        font = new Font("Space Mono", Font.BOLD, 23);
        setImage();
        this.trophyEarned = getTrophyEarned();
    }

    public int getTrophyEarned() {
        File file = new File("highScore.bin");
        //check if file exists or not
        if (!file.exists()) {
            try{
                writeInFile(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 0;
        }

        //read this file if it exists
        try (DataInputStream dis = new DataInputStream(new FileInputStream(file))) {
            return dis.readInt();
        } catch (IOException e) {
            return 0;
        }
    }

    //set the image for apple(different from apple in game) and trophy
    public void setImage() {
        try {
            apple = ImageIO.read(getClass().getClassLoader().getResourceAsStream("apple/apple.png"));
            trophy = ImageIO.read(getClass().getClassLoader().getResourceAsStream("extras/trophy.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Used Swings inbuilt feature
    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(Color.WHITE);
        graphics2D.setFont(font);

        graphics2D.drawImage(apple, panel.tileSize / 2, panel.tileSize / 4, panel.tileSize - 8, panel.tileSize - 8, null);
        graphics2D.drawString(" " + panel.getAppleEatenFromCollision(),
                panel.tileSize + panel.tileSize / 4,
                panel.tileSize - 2);
        //When game is off means user is out
        if (!panel.gameOn) {
            int currentTrophy = panel.getAppleEatenFromCollision();
            if (currentTrophy> trophyEarned) {
                //get the amount of trophy earned
                trophyEarned = currentTrophy;
                try {
                    File file = new File("username.txt");
                    Scanner scanner = new Scanner(file);
                    String username = scanner.hasNextLine()?scanner.nextLine().trim():null;
                    sendPutRequest(username, trophyEarned);
                }catch (Exception ex){
                    System.out.println(ex.getMessage());
                }
                try {
                    writeInFile(trophyEarned);
                }catch (IOException ex){

                }
            }
            if (!scoreSet) {
                score = currentTrophy;
                scoreSet = true;
            }
            panel.setAppleEatenFromCollision(0);
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

    //migrate this later to another class //for saving game highscore in the server.
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

    public int getScore() {
        // CRITICAL FIX: Always return score from collision checker, not a separate variable
        return panel.getAppleEatenFromCollision();
    }

    // Write in the binary File
    private void writeInFile(int value) throws IOException {
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(new File(FILE_PATH)));
        dos.writeInt(value);
    }
}