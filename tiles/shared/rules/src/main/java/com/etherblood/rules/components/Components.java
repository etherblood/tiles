package com.etherblood.rules.components;

import com.etherblood.entities.ComponentDefinition;
import com.etherblood.entities.ComponentDefinitionBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;

/**
 *
 * @author Philipp
 */
public class Components {

    public static ComponentDefinition[] DEFINITIONS;

    static {
        List<ComponentDefinition> definitions = new ArrayList<>();
        ToIntFunction<ComponentDefinitionBuilder> generator = builder -> {
            int id = definitions.size();
            builder.withId(id);
            definitions.add(builder.build());
            return id;
        };
        Stats.init(generator);
        Abilities.init("Ability", generator);
        Arena.init("Arena", generator);
        CONTROLLED_BY = generator.applyAsInt(ComponentDefinition.builder().withName("ControlledBy"));
        NEXT_TEAM = generator.applyAsInt(ComponentDefinition.builder().withName("NextTeam"));
        POSITION = generator.applyAsInt(ComponentDefinition.builder().withName("Position"));
        BUFF_ON = generator.applyAsInt(ComponentDefinition.builder().withName("BuffOn"));
        ACTIVE_TURN = generator.applyAsInt(ComponentDefinition.builder().withName("ActiveTurn"));
        SPRITE = generator.applyAsInt(ComponentDefinition.builder().withName("Sprite"));
        MEMBER_OF = generator.applyAsInt(ComponentDefinition.builder().withName("MemberOf"));

        DEFINITIONS = definitions.toArray(new ComponentDefinition[definitions.size()]);
    }

    public static class Stats {

        private static void init(ToIntFunction<ComponentDefinitionBuilder> generator) {
            Health.init("Health", generator);
            ActionPoints.init("ActionPoints", generator);
            MovePoints.init("MovePoints", generator);
            Power.init("Power", generator);
            Toughness.init("Toughness", generator);
        }

        public static class Power {

            private static void init(String baseName, ToIntFunction<ComponentDefinitionBuilder> generator) {
                Fire.init("Fire" + baseName, generator);
                Water.init("Water" + baseName, generator);
                Air.init("Air" + baseName, generator);
                Earth.init("Earth" + baseName, generator);
            }

            public static class Fire {

                public static int BASE;
                public static int ADDITIVE;
                public static int ACTIVE;
                public static int BUFFED;

                private static void init(String baseName, ToIntFunction<ComponentDefinitionBuilder> generator) {
                    BASE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Base"));
                    ADDITIVE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Additive"));
                    ACTIVE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Active"));
                    BASE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Base"));
                }

            }

            public static class Water {

                public static int BASE;
                public static int ADDITIVE;
                public static int ACTIVE;
                public static int BUFFED;

                private static void init(String baseName, ToIntFunction<ComponentDefinitionBuilder> generator) {
                    BASE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Base"));
                    ADDITIVE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Additive"));
                    ACTIVE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Active"));
                    BASE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Base"));
                }

            }

            public static class Earth {

                public static int BASE;
                public static int ADDITIVE;
                public static int ACTIVE;
                public static int BUFFED;

                private static void init(String baseName, ToIntFunction<ComponentDefinitionBuilder> generator) {
                    BASE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Base"));
                    ADDITIVE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Additive"));
                    ACTIVE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Active"));
                    BASE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Base"));
                }

            }

            public static class Air {

                public static int BASE;
                public static int ADDITIVE;
                public static int ACTIVE;
                public static int BUFFED;

                private static void init(String baseName, ToIntFunction<ComponentDefinitionBuilder> generator) {
                    BASE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Base"));
                    ADDITIVE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Additive"));
                    ACTIVE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Active"));
                    BASE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Base"));
                }

            }
        }

        public static class Toughness {

            private static void init(String baseName, ToIntFunction<ComponentDefinitionBuilder> generator) {
                Fire.init("Fire" + baseName, generator);
                Water.init("Water" + baseName, generator);
                Air.init("Air" + baseName, generator);
                Earth.init("Earth" + baseName, generator);
            }

            public static class Fire {

                public static int BASE;
                public static int ADDITIVE;
                public static int ACTIVE;
                public static int BUFFED;

                private static void init(String baseName, ToIntFunction<ComponentDefinitionBuilder> generator) {
                    BASE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Base"));
                    ADDITIVE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Additive"));
                    ACTIVE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Active"));
                    BASE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Base"));
                }

            }

            public static class Water {

                public static int BASE;
                public static int ADDITIVE;
                public static int ACTIVE;
                public static int BUFFED;

                private static void init(String baseName, ToIntFunction<ComponentDefinitionBuilder> generator) {
                    BASE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Base"));
                    ADDITIVE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Additive"));
                    ACTIVE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Active"));
                    BASE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Base"));
                }

            }

            public static class Earth {

                public static int BASE;
                public static int ADDITIVE;
                public static int ACTIVE;
                public static int BUFFED;

                private static void init(String baseName, ToIntFunction<ComponentDefinitionBuilder> generator) {
                    BASE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Base"));
                    ADDITIVE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Additive"));
                    ACTIVE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Active"));
                    BASE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Base"));
                }

            }

            public static class Air {

                public static int BASE;
                public static int ADDITIVE;
                public static int ACTIVE;
                public static int BUFFED;

                private static void init(String baseName, ToIntFunction<ComponentDefinitionBuilder> generator) {
                    BASE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Base"));
                    ADDITIVE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Additive"));
                    ACTIVE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Active"));
                    BASE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Base"));
                }

            }
        }

        public static class Health {

            public static int BASE;
            public static int ADDITIVE;
            public static int ACTIVE;
            public static int BUFFED;

            private static void init(String baseName, ToIntFunction<ComponentDefinitionBuilder> generator) {
                BASE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Base"));
                ADDITIVE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Additive"));
                ACTIVE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Active"));
                BASE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Base"));
            }

        }

        public static class ActionPoints {

            public static int BASE;
            public static int ADDITIVE;
            public static int ACTIVE;
            public static int BUFFED;

            private static void init(String baseName, ToIntFunction<ComponentDefinitionBuilder> generator) {
                BASE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Base"));
                ADDITIVE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Additive"));
                ACTIVE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Active"));
                BASE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Base"));
            }

        }

        public static class MovePoints {

            public static int BASE;
            public static int ADDITIVE;
            public static int ACTIVE;
            public static int BUFFED;

            private static void init(String baseName, ToIntFunction<ComponentDefinitionBuilder> generator) {
                BASE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Base"));
                ADDITIVE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Additive"));
                ACTIVE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Active"));
                BASE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Base"));
            }

        }
    }

    public static class Abilities {

        public static int PASS_TURN;
        public static int WALK;
        public static int RAZORLEAF;

        private static void init(String baseName, ToIntFunction<ComponentDefinitionBuilder> generator) {
            PASS_TURN = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "PassTurn"));
            WALK = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Walk"));
            RAZORLEAF = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Razorleaf"));
        }
    }

    public static class Arena {

        public static int SIZE;
        public static int OBSTACLE;

        private static void init(String baseName, ToIntFunction<ComponentDefinitionBuilder> generator) {
            SIZE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Size"));
            OBSTACLE = generator.applyAsInt(ComponentDefinition.builder().withName(baseName + "Obstacle"));
        }

    }

    public static int CONTROLLED_BY;
    public static int NEXT_TEAM;
    public static int POSITION;
    public static int BUFF_ON;
    public static int ACTIVE_TURN;
    public static int SPRITE;
    public static int MEMBER_OF;
}