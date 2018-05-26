package com.etherblood.test.client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JPanel;

/**
 *
 * @author Philipp
 */
public class GridPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private final List<CharacterDetails> sprites;
    private final SpriteSheet pokemons, trees;
    private final int boardTilesX, boardTilesY;
    private final Set<Integer> highlights = new HashSet<>();

    public GridPanel(List<CharacterDetails> sprites, SpriteSheet pokemons, SpriteSheet trees, int boardTilesX, int boardTilesY) {
        this.sprites = sprites;
        this.pokemons = pokemons;
        this.trees = trees;
        this.boardTilesX = boardTilesX;
        this.boardTilesY = boardTilesY;
    }
    
    @Override
    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        float boardTileWidth = (float)getWidth() / boardTilesX;
        float boardTileHeight = (float)getHeight() / boardTilesY;
        
        g.setColor(Color.LIGHT_GRAY);
        for (int y = 0; y < boardTilesY; y++) {
            for (int x = 0; x < boardTilesX; x++) {
                if(((x + y) & 1) != 0) {
                    g.fillRect((int)(boardTileWidth * x), (int)(boardTileHeight * y), (int)boardTileWidth, (int)boardTileHeight);
                }
            }
        }
        g.setColor(Color.BLUE);
        for (Integer highlight : highlights) {
            int x = highlight % boardTilesX;
            int y = highlight / boardTilesX;
            g.drawRect((int)(boardTileWidth * x), (int)(boardTileHeight * y), (int)boardTileWidth, (int)boardTileHeight);
        }
        
        for (CharacterDetails sprite : sprites) {
            Rectangle source;
            Image image;
            if(sprite.spriteId != null) {
                source = pokemons.getBounds(sprite.spriteId);
                image = pokemons.getImage();
            } else {
                source = trees.getBounds((sprite.x ^ sprite.y) % trees.size());
                image = trees.getImage();
            }
            float destX = sprite.x * boardTileWidth;
            float destY = sprite.y * boardTileHeight;
            g.drawImage(image, (int)destX, (int)destY, (int)(destX + boardTileWidth), (int)(destY + boardTileHeight), source.x, source.y, source.x + source.width, source.y + source.height, null);
        }
    }
    
    public void setHighlights(Collection<Integer> highlights) {
        this.highlights.clear();
        this.highlights.addAll(highlights);
    }

}
