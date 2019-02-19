package com.etherblood.rules.game.setup.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CharacterConfig {

    public transient int entity;
    public int id;
    public String name;
    public int teamId;

    public Map<String, Integer> components = new HashMap<>();
    public List<SkillConfig> skills = new ArrayList<>();
    public SkillConfig walkSkill;
    public transient SkillConfig passTurnSkill;
}
