package com.etherblood.jme3.gui.skillbar;

import com.etherblood.core.Action;
import com.etherblood.core.util.Coordinates;
import com.etherblood.game.client.GameClient;
import com.etherblood.mods.core.game.components.CoreComponents;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Plane;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.component.IconComponent;
import com.simsilica.lemur.core.VersionedHolder;
import com.simsilica.lemur.core.VersionedReference;
import java.util.Arrays;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.Set;
import java.util.UUID;

public class SkillbarAppstate extends AbstractAppState {

    private final UUID gameId;
    private final GameClient client;
    private final VersionedReference<Set<Integer>> selectableActions;
    private final VersionedHolder<Integer> selectedAction = new VersionedHolder<>();
    private final VersionedReference<Integer> gameVersion;
    private final Container skillbarContainer = new Container();
    private final VersionedReference<Integer> selectedActor;
    private final CoreComponents core;
    private final Button[] skillIconLabels = new Button[10];
    private final String icon404Path, iconEmptyPath;
    private final SkillbarConfig config;
    private InputManager inputManager;

    @SuppressWarnings("unchecked")
    public SkillbarAppstate(UUID gameId, GameClient client, CoreComponents core, VersionedReference<Integer> gameVersion, VersionedReference<Integer> selectedActor, VersionedReference<Set<Integer>> selectableActions) {
        this.gameId = gameId;
        this.client = client;
        this.gameVersion = gameVersion;
        this.selectedActor = selectedActor;
        this.selectableActions = selectableActions;
        this.icon404Path = "test/icons/perspective-dice-six-faces-random.png";
        this.iconEmptyPath = "test/icons/circle.png";
        this.core = core;
        for (int i = 0; i < skillIconLabels.length; i++) {
            skillIconLabels[i] = new Button("");
            skillIconLabels[i].addClickCommands(skillButtonClickCommand(i));
            skillbarContainer.addChild(skillIconLabels[i], i);
        }
        config = new SkillbarConfig(Arrays.asList(
                new SkillConfig(SkillbarConfig.PASS_TURN_ID, null, "pass-turn", null),
                new SkillConfig(SkillbarConfig.WALK_ID, null, "walk", null),
                new SkillConfig(SkillbarConfig.RAZORLEAF_ID, "test/icons/grapple.png", "razorleaf", 0)));
    }

    private Command<Button> skillButtonClickCommand(int index) {
        return button -> selectAction(index);
    }

    @Override
    public void update(float tpf) {
        if (gameVersion.update() | selectableActions.update()) {
            if (!selectableActions.get().contains(selectedAction.getObject())) {
                Integer walk = selectedActor.get() == null ? null : findSkill(selectedActor.get(), SkillbarConfig.WALK_ID);
                selectedAction.updateObject(isSelectable(walk) ? walk : null);
            }

            for (int i = 0; i < skillIconLabels.length; i++) {
                Button skillIconLabel = skillIconLabels[i];
                SkillConfig skill = config.findBySkillbarIndex(i);
                if (skill != null && (selectedActor.get() == null || findSkill(selectedActor.get(), skill.id) == null)) {
                    skill = null;
                }
                skillIconLabel.setEnabled(skill != null && isSelectable(skill.id));
                String iconPath = skill != null ? skill.iconPath : iconEmptyPath;
                if (iconPath == null) {
                    iconPath = icon404Path;
                }
                IconComponent icon = new IconComponent(iconPath);
                icon.setIconScale(1 / 8f);
                skillIconLabel.setIcon(icon);
                final int index = i;
                skillIconLabel.addClickCommands((Command<Button>) (Button source) -> {
                    selectAction(index);
                });
            }
        }
    }

    private Integer findSkill(int actor, int skillId) {
        OptionalInt skill = core.skill.id.query().unique(x -> core.skill.id.get(x) == skillId && core.skill.ofActor.hasValue(x, actor));
        if (skill.isPresent()) {
            return skill.getAsInt();
        }
        return null;
    }

    private boolean isSelectable(Integer action) {
        return action != null && selectableActions.get().contains(action);
    }

    @Override
    public void initialize(AppStateManager appStateManager, Application app) {
        inputManager = app.getInputManager();
        ((SimpleApplication) app).getGuiNode().attachChild(skillbarContainer);
        skillbarContainer.setLocalTranslation(app.getCamera().getWidth() - 640, 64, 0);
        skillbarContainer.setPreferredSize(new Vector3f(640, 64, 0));
        inputManager.addMapping("click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if (isPressed) {
                    return;
                }
                Vector2f cursor = inputManager.getCursorPosition();
                Plane floor = new Plane(Vector3f.UNIT_Y, 0);
                Ray ray = new Ray(app.getCamera().getWorldCoordinates(cursor, 0), app.getCamera().getDirection());
                Vector3f result = new Vector3f();
                if (ray.intersectsWherePlane(floor, result)) {
                    click(Coordinates.of((int) result.getX(), (int) result.getZ()));
                }
            }
        }, "click");
        keyMapping("space", KeyInput.KEY_SPACE, this::passTurn);
        for (int i = 0; i < 10; i++) {
            addActionMapping(i);
        }
    }

    public void selectAction(int skillbarIndex) {
        SkillConfig skillConfig = config.findBySkillbarIndex(skillbarIndex);
        Integer skill = skillConfig == null ? null : findSkill(selectedActor.get(), skillConfig.id);
        if (skill == null || Objects.equals(skill, selectedAction.getObject())) {
            skill = findSkill(selectedActor.get(), SkillbarConfig.WALK_ID);
        }
        if (!isSelectable(skill)) {
            skill = null;
        }
        selectedAction.updateObject(skill);
        System.out.println("selected skill: " + (skill == null ? null : skillConfig.name));
    }

    private void passTurn() {
        if (selectedActor.get() == null) {
            System.out.println("cant pass turn, no actor selected.");
            return;
        }
        System.out.println("pass turn of #" + selectedActor.get());
        int passTurnSkill = findSkill(selectedActor.get(), SkillbarConfig.PASS_TURN_ID);
        applyAction(Action.builder(passTurnSkill).build());
    }

    private void applyAction(Action action) {
        System.out.println("Sent action " + action);
        client.act(gameId, action);
    }

    private void click(int coordinates) {
        System.out.println("clicked " + Coordinates.x(coordinates) + ", " + Coordinates.y(coordinates));
        if (selectedActor.get() == null) {
            System.out.println("no action for click, no actor selected.");
            return;
        }
        if (selectedAction.getObject() == null) {
            System.out.println("no action for click, no action selected.");
            return;
        }
        applyAction(Action.builder(selectedAction.getObject())
                .withTargetPosition(coordinates)
                .build());
    }

    private void addActionMapping(int index) {
        keyMapping("skill" + index, KeyInput.KEY_1 + index, () -> selectAction(index));
    }

    private void keyMapping(String name, int key, Runnable listener) {
        inputManager.addMapping(name, new KeyTrigger(key));
        inputManager.addListener(new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if (!isPressed) {
                    listener.run();
                }
            }
        }, name);
    }

    public VersionedReference<Integer> selectedActionReference() {
        return selectedAction.createReference();
    }

}
