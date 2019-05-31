package com.etherblood.core;

public class Action {

    private final int targetSkill;
    private final Integer targetPosition;

    public Action(int targetSkill, Integer targetPosition) {
        this.targetSkill = targetSkill;
        this.targetPosition = targetPosition;
    }

    public int getTargetSkill() {
        return targetSkill;
    }

    public Integer getTargetPosition() {
        return targetPosition;
    }

    @Override
    public String toString() {
        return "Action{" + "targetSkill=" + targetSkill + ", targetPosition=" + targetPosition + '}';
    }

    public static Builder builder(int targetSkill) {
        return new Builder(targetSkill);
    }

    public static class Builder {

        private final int targetSkill;
        private Integer targetPosition;

        private Builder(int targetSkill) {
            this.targetSkill = targetSkill;
        }

        public Builder withTargetPosition(Integer targetPosition) {
            this.targetPosition = targetPosition;
            return this;
        }

        public Action build() {
            return new Action(targetSkill, targetPosition);
        }
    }
}
