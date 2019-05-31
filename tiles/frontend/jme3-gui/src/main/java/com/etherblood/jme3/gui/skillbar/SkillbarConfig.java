package com.etherblood.jme3.gui.skillbar;

import java.util.List;

public class SkillbarConfig {

    public static final int PASS_TURN_ID = 1;
    public static final int WALK_ID = 2;
    public static final int RAZORLEAF_ID = 3;

    public List<SkillConfig> skills;

    public SkillbarConfig(List<SkillConfig> skills) {
        this.skills = skills;
    }

    public SkillConfig findBySkillbarIndex(int index) {
        for (SkillConfig skill : skills) {
            if (skill.index != null && skill.index == index) {
                return skill;
            }
        }
        return null;
    }

    public SkillConfig findBySkillId(int id) {
        for (SkillConfig skill : skills) {
            if (skill.id == id) {
                return skill;
            }
        }
        return null;
    }
}
