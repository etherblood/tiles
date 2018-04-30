package com.etherblood.rules.battle;

import com.etherblood.events.Event;
import com.etherblood.rules.stats.PokemonTypes;

/**
 *
 * @author Philipp
 */
public class DamageEvent extends Event {

    public int attacker, defender, damage;
    public PokemonTypes type;

    public DamageEvent(int attacker, int defender, int attack, PokemonTypes type) {
        this.attacker = attacker;
        this.defender = defender;
        this.damage = attack;
        this.type = type;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{attacker=" + attacker + ", defender=" + defender + ", attack=" + damage + ", type=" + type + '}';
    }

}
