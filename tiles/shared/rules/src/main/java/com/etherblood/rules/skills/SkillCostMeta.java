package com.etherblood.rules.skills;

public class SkillCostMeta {

    public final String statName;
    public final int cost, stat;

    public SkillCostMeta(String statName, int cost, int stat) {
        this.statName = statName;
        this.cost = cost;
        this.stat = stat;
    }
}
