package Tiles;

import Main.GamePanel;

import java.awt.*;

public class TilesManager {
    GamePanel panel;
    Color bg1, bg2;

    public TilesManager(GamePanel panel) {
        this.panel = panel;
        bg1 = new Color(0, 8, 20);
        bg2 = new Color(2, 10, 22);
    }

    public void draw(Graphics2D graphics2D) {
        for (int i = 0; i < panel.maxScreenRowUnit; i++) {
            for (int j = 0; j < panel.maxScreenColUnit; j++) {
                if ((j + i) % 2 == 0) {
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
