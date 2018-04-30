package com.etherblood.test.client;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Optional;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Philipp
 */
public class StatsPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private final SpriteSheet pokemons;
    private CharacterDetails pokemon;

    private final JLabel nameLabel = new JLabel();
    private final JLabel iconLabel = new JLabel();
    private final JLabel healthLabel = new JLabel();
    private final JLabel apLabel = new JLabel();
    private final JLabel mpLabel = new JLabel();

    public StatsPanel(SpriteSheet pokemons) {
        super(new BorderLayout());
        this.pokemons = pokemons;

        JPanel header = new JPanel();
        add(header, BorderLayout.NORTH);
        header.add(iconLabel);
        header.add(nameLabel);
        JPanel body = new JPanel();
        add(body, BorderLayout.CENTER);
        body.add(healthLabel);
        body.add(apLabel);
        body.add(mpLabel);
        setVisible(false);
    }

    public CharacterDetails getPokemon() {
        return pokemon;
    }

    public void setPokemon(CharacterDetails pokemon) {
        this.pokemon = pokemon;
        setVisible(pokemon != null);
        if (pokemon != null) {
            int spriteId = Optional.ofNullable(pokemon.spriteId).orElse(28 * 18 - 1);
            Rectangle bounds = pokemons.getBounds(spriteId);
            BufferedImage bi = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_ARGB);
            bi.getGraphics().drawImage(pokemons.getImage(), -bounds.x, -bounds.y, null);
            iconLabel.setIcon(new ImageIcon(bi));
            nameLabel.setText(pokemon.name);
            healthLabel.setText(pokemon.health + " health");
            apLabel.setText(pokemon.ap + " ap");
            mpLabel.setText(pokemon.mp + " mp");
        }
        invalidate();
    }

}
