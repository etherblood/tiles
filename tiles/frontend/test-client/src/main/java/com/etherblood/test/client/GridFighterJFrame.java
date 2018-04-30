package com.etherblood.test.client;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.ImageIcon;

/**
 *
 * @author Philipp
 */
public class GridFighterJFrame extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;
    private final List<CharacterDetails> sprites = new ArrayList<>();
    private final GridPanel gridPanel;
    private final StatsPanel statsPanel;
    private final StatsPanel statsPanel2;
    private final AbilitiesPanel abilitiesPanel;
    public Consumer<AbilityUsage> abilityCallback;

    public GridFighterJFrame(int mapWidth, int mapHeight) {
        initComponents();

        SpriteSheet pokemons = new SpriteSheet(new ImageIcon(getClass().getResource("/pokemons.png")), 28, 18);
        SpriteSheet trainers = new SpriteSheet(new ImageIcon(getClass().getResource("/trainers.png")), 10, 10);
        SpriteSheet trees = new SpriteSheet(new ImageIcon(getClass().getResource("/trees.png")), 10, 1);

        gridPanel = new GridPanel(sprites, pokemons, trees, mapWidth, mapHeight);

        statsPanel = new StatsPanel(pokemons);
        statsPanel.setSize(200, 500);
        statsPanel.setLocation(1000, 0);
        add(statsPanel);

        statsPanel2 = new StatsPanel(pokemons);
        statsPanel2.setSize(200, 500);
        statsPanel2.setLocation(1000, 500);
        add(statsPanel2);

        abilitiesPanel = new AbilitiesPanel();
        abilitiesPanel.setSize(200, 500);
        abilitiesPanel.setLocation(1200, 0);
        add(abilitiesPanel);

        gridPanel.setSize(1000, 1000);
        gridPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int x = e.getX() * mapWidth / gridPanel.getWidth();
                int y = e.getY() * mapHeight / gridPanel.getHeight();
                CharacterDetails selected = null;
                for (CharacterDetails sprite : sprites) {
                    if (sprite.x == x && sprite.y == y) {
                        selected = sprite;
                        break;
                    }
                }
                statsPanel2.setPokemon(selected);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (statsPanel2.getPokemon() != null) {
                    statsPanel2.setPokemon(null);
//                    statsPanel2.invalidate();
                }
            }
        });
        gridPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
//                if(!data.has(pokemon, ItsMyTurnComponent.class)) {
//                    System.out.println("not your turn");
//                    return;
//                }
                int x = e.getX() * mapWidth / gridPanel.getWidth();
                int y = e.getY() * mapHeight / gridPanel.getHeight();
//                System.out.println("clicked: " + x + ", " + y);

//                ActionGenerator actionGenerator = actionFactory.getActionGenerator(selectedAbility);
//                if(actionGenerator.validTargets(selectedAbility).contains(index)) {
//                    Object actionEvent = actionGenerator.generateEvent(selectedAbility, index);
//                    queue.fireEvent(actionEvent);
//                    queue.handleEvents();
//                } else {
//                    System.out.println("invalid target");
//                }
//                
//                selectedAbility = moveAbility;
                if (abilitiesPanel.getSelected() != null) {
                    abilityCallback.accept(new AbilityUsage(statsPanel.getPokemon().characterId, abilitiesPanel.getSelected().abilityKey, x, y));
                    return;
                }

                CharacterDetails selected = null;
                for (CharacterDetails sprite : sprites) {
                    if (sprite.x == x && sprite.y == y) {
                        selected = sprite;
                        break;
                    }
                }
                statsPanel.setPokemon(selected);
                abilitiesPanel.setAbilities(selected == null ? Collections.emptyList() : selected.abilities);
//                statsPanel.invalidate();
//                
//                Entity entity = map.get(index);
//                if(data.has(entity, CharacterComponent.class)) {
//                    statsPanel2.setPokemon(entity);
//                } else {
//                    statsPanel2.setPokemon(null);
//                }
////                statsPanel2.invalidate();
//                gridPanel.setHighlights(actionGenerator.validTargets(moveAbility));
//                gridPanel.repaint();
            }
        });
        add(gridPanel);

//        selectedAbility = moveAbility;
//        addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyReleased(KeyEvent e) {
//                switch(e.getKeyCode()) {
//                    case KeyEvent.VK_1:
//                        selectedAbility = scratch;
//                        break;
//                    case KeyEvent.VK_SPACE:
//                        queue.fireEvent(new EndTurnEvent(endTurn, 0));
//                        queue.handleEvents();
//                        statsPanel.setPokemon(pokemon);
//                        
//                        statsPanel2.setPokemon(statsPanel2.getPokemon());
////                        statsPanel2.invalidate();
//                        selectedAbility = moveAbility;
//                        break;
//                    default:
//                        selectedAbility = moveAbility;
//                        break;
//                }
//                System.out.println("ability: " + Util.toString(data, selectedAbility));
//                ActionGenerator actionGenerator = actionFactory.getActionGenerator(selectedAbility);
//                gridPanel.setHighlights(actionGenerator.validTargets(selectedAbility));
//                gridPanel.repaint();
//            }
//        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void updateSprites(List<CharacterDetails> sprites) {
        java.awt.EventQueue.invokeLater(() -> {
            this.sprites.clear();
            this.sprites.addAll(sprites);
            validate();
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
