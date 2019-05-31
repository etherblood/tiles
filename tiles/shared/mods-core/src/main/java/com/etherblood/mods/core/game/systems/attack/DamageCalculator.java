package com.etherblood.mods.core.game.systems.attack;

public class DamageCalculator {

    public int calculateFinalDamage(int damage, int power, int toughness) {
        int dividend;
        int divisor;

        if (power < 0) {
            dividend = 100;
            divisor = 100 - power;
        } else {
            dividend = 100 + power;
            divisor = 100;
        }

        if (toughness < 0) {
            dividend *= 100 - toughness;
            divisor *= 100;
        } else {
            dividend *= 100;
            divisor *= 100 + toughness;
        }
        return damage * dividend / divisor;
    }

}
