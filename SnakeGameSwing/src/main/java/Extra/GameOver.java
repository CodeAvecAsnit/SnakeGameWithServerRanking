package Extra;

import Main.GamePanel;

import javax.swing.*;
import java.awt.*;

/**
 * @author : Asnit Bakhati
 */
public class GameOver {

    private final GamePanel gamePanel;
    private final Font scoreFont;
    private final Font tryAgainFont;

    public GameOver(GamePanel panel) {
        this.gamePanel = panel;
        this.scoreFont = new Font("Space Mono", Font.BOLD, 30);
        this.tryAgainFont = new Font("Space Mono", Font.PLAIN, 20);
    }

    public void draw(Graphics2D graphics2D, int width, int height) {
        graphics2D.setColor(new Color(0, 0, 0, 150));
        graphics2D.fillRect(0, 0, width, height);

        graphics2D.setColor(Color.WHITE);
        graphics2D.setFont(scoreFont);
        String scoreText = "Final Score: " + gamePanel.getAppleEatenFromCollision();
        int xScore = (gamePanel.screenWidth - graphics2D.getFontMetrics().stringWidth(scoreText)) / 2;
        graphics2D.drawString(scoreText, xScore, gamePanel.tileSize * 6);

        graphics2D.setColor(Color.RED);
        String msg = "GAME OVER";
        int xMsg = (gamePanel.screenWidth - graphics2D.getFontMetrics().stringWidth(msg)) / 2;
        graphics2D.drawString(msg, xMsg, gamePanel.screenHeight / 2);

        graphics2D.setColor(Color.YELLOW);
        graphics2D.setFont(tryAgainFont);
        String retry = "Press SPACE to Play Again";
        int xRetry = (gamePanel.screenWidth - graphics2D.getFontMetrics().stringWidth(retry)) / 2;
        graphics2D.drawString(retry, xRetry, gamePanel.tileSize * 17);
    }
}