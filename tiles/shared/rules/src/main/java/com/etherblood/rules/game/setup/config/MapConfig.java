package com.etherblood.rules.game.setup.config;

import java.util.Map;

public class MapConfig {

    public String name;
    public int width, height;
    public Map<String, Integer> components;
    public TeamConfig[] teams;
    public ObjectConfig[] objects;
}
