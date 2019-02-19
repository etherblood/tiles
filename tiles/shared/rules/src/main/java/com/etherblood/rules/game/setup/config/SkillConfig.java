package com.etherblood.rules.game.setup.config;

import java.util.HashMap;
import java.util.Map;

public class SkillConfig {

    public transient int entity;
    
    public String name;
    public String description;
    public String icon;
    
    public Integer healthCost;
    public Integer movePointsCost;
    public Integer actionPointsCost;
    public Integer cooldown;
    
    public Integer targetingSelf;
    public Integer targetingAlly;
    public Integer targetingEnemy;
    public Integer targetingEmpty;
    public Integer targetingRequiresSight;
    public Integer targetingRequiresPath;
    public Integer targetingManhattanRange;
    
    public Integer effectPassTurn;
    public Integer effectWalkToTarget;
    public Integer effectFireDamageToTarget;
    public Integer effectWaterDamageToTarget;
    public Integer effectEarthDamageToTarget;
    public Integer effectAirDamageToTarget;
    
    public Map<String, Integer> components = new HashMap<>();
}
