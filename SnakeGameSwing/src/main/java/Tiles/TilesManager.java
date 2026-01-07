package Tiles;

import Main.GamePanel;

import java.awt.*;

/**
 * @author : Asnit Bakhati
 */
public class TilesManager {

    private final GamePanel panel;
    private final Color bg1;
    private final Color bg2;

    public TilesManager(GamePanel panel) {
        this.panel = panel;
        this.bg1 = new Color(0, 8, 20);
        this.bg2 = new Color(2, 10, 22);
    }

    //draw with alternate background colors
    public void draw(Graphics2D graphics2D) {
        for (int i = 0; i < panel.maxScreenRowUnit; i++) {
            for (int j = 0; j < panel.maxScreenColUnit; j++) {
                if (((j + i) & 1) == 0) {
                    graphics2D.setColor(bg1);
                    graphics2D.fillRect(j * panel.tileSize, i * panel.tileSize, panel.tileSize, panel.tileSize);
                } else {
                    graphics2D.setColor(bg2);
                    graphics2D.fillRect(j * panel.tileSize, i * panel.tileSize, panel.tileSize, panel.tileSize);
                }
            }
        }
    }
}
