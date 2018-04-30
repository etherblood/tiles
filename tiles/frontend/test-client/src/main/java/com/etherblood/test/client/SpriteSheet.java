package com.etherblood.test.client;

import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

/**
 *
 * @author Philipp
 */
public class SpriteSheet {

    private final Image image;
    private final int tileWidth, tileHeight;
    private final int tilesX, tilesY;

    public SpriteSheet(ImageIcon icon, int tilesX, int tilesY) {
        this(icon.getImage(), icon.getIconWidth(), icon.getIconHeight(), tilesX, tilesY);
    }

    public SpriteSheet(Image image, int width, int height, int tilesX, int tilesY) {
        this.image = image;
        this.tileWidth = width / tilesX;
        this.tileHeight = height / tilesY;
        this.tilesX = tilesX;
        this.tilesY = tilesY;
        if (tileWidth * tilesX != width || tileHeight * tilesY != height) {
            throw new IllegalArgumentException();
        }
    }

    public Image getImage() {
        return image;
    }

    public Rectangle getBounds(int spriteId) {
        int x = spriteId % tilesX;
        int y = spriteId / tilesX;
        return new Rectangle(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
    }

    public int size() {
        return tilesX * tilesY;
    }
}
