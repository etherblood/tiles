package com.etherblood.test.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Philipp
 */
public class AbilitiesPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private List<AbilityDetails> abilities;
    private AbilityDetails selected;

    public AbilitiesPanel() {
        super(new BorderLayout());
        setVisible(false);
    }

    public void setAbilities(List<AbilityDetails> abilities) {
        this.abilities = abilities;
        selected = null;
        refresh();
    }

    public void selectAbility(AbilityDetails ability) {
        selected = ability;
        refresh();
    }

    public AbilityDetails getSelected() {
        return selected;
    }

    private void refresh() {
        removeAll();
        for (AbilityDetails ability : abilities) {
            JLabel abilityLabel = new JLabel(ability.abilityName);
            if (selected != null && selected.abilityKey == ability.abilityKey) {
                abilityLabel.setForeground(Color.RED);
            }
            abilityLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    selectAbility(ability);
                }
            });
            add(abilityLabel);
        }
        setVisible(!abilities.isEmpty());
        invalidate();
    }

}
