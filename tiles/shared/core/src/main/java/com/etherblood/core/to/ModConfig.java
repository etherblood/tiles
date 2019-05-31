package com.etherblood.core.to;

public class ModConfig {

    private String modName;

    public ModConfig() {
    }

    public ModConfig(String modName) {
        this.modName = modName;
    }

    public String getModName() {
        return modName;
    }

    @Override
    public String toString() {
        return "ModConfig{" + "modName=" + modName + '}';
    }
}
