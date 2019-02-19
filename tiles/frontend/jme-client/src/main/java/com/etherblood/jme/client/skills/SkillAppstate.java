package com.etherblood.jme.client.skills;

import com.etherblood.collections.IntArrayList;
import com.etherblood.collections.IntHashSet;
import com.etherblood.entities.ComponentMeta;
import com.etherblood.events.Event;
import com.etherblood.jme.client.pathfinding.Pathfinder;
import com.etherblood.jme.client.util.VersionedReference;
import com.etherblood.rules.GameContext;
import com.etherblood.rules.game.setup.config.CharacterConfig;
import com.etherblood.rules.game.setup.config.SkillConfig;
import com.etherblood.rules.util.Coordinates;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Plane;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.component.ColoredComponent;
import com.simsilica.lemur.component.IconComponent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.OptionalInt;

public class SkillAppstate extends BaseAppState {

    private final Container skillbarContainer;
    private final VersionedReference<CharacterConfig> selectedActor;
    private final VersionedReference<SkillConfig> selectedAction;
    private final VersionedReference<IntHashSet> targetableTiles;
    private final GameContext context;
    private final Button[] skillIconLabels = new Button[10];
    private final String icon404Path, iconEmptyPath;

    @SuppressWarnings("unchecked")
    public SkillAppstate(GameContext context, Container skillbarContainer, VersionedReference<CharacterConfig> selectedActor, VersionedReference<SkillConfig> selectedAction, VersionedReference<IntHashSet> targetableTiles, String icon404Path, String iconEmptyPath) {
        this.skillbarContainer = skillbarContainer;
        this.selectedActor = selectedActor;
        this.selectedAction = selectedAction;
        this.context = context;
        this.targetableTiles = targetableTiles;
        this.icon404Path = icon404Path;
        this.iconEmptyPath = iconEmptyPath;
        for (int i = 0; i < skillIconLabels.length; i++) {
            skillIconLabels[i] = new Button("");
            skillIconLabels[i].addClickCommands(skillButtonClickCommand(i));
            skillbarContainer.addChild(skillIconLabels[i], i);
        }
    }

    private Command<Button> skillButtonClickCommand(int index) {
        return button -> selectAction(index);
    }

    @Override
    public void update(float tpf) {
        if (selectedActor.update()) {
            if (selectedActor.get() != null) {
                selectedAction.set(selectedActor.get().walkSkill);

                for (int i = 0; i < skillIconLabels.length; i++) {
                    Button skillIconLabel = skillIconLabels[i];
                    List<SkillConfig> skills = selectedActor.get().skills;
                    SkillConfig skill = skills.size() > i ? skills.get(i) : null;
                    skillIconLabel.setEnabled(skill != null);
                    String iconPath = skill != null ? skill.icon : iconEmptyPath;
                    if (iconPath == null) {
                        iconPath = icon404Path;
                    }
                    IconComponent icon = new IconComponent(iconPath);
                    icon.setIconScale(1 / 8f);
                    skillIconLabel.setIcon(icon);
                }
            } else {
                selectedAction.set(null);
            }
        }
        if (selectedAction.update() && selectedAction.get() != null) {
            int index = selectedActor.get().skills.indexOf(selectedAction.get());
            for (int i = 0; i < skillIconLabels.length; i++) {
                Button skillIconLabel = skillIconLabels[i];
                ((ColoredComponent) skillIconLabel.getIcon()).setColor(index == i ? ColorRGBA.Yellow : ColorRGBA.White);
            }
        }
    }

    @Override
    protected void initialize(Application app) {
        getApplication().getInputManager().addMapping("click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        getApplication().getInputManager().addListener(new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if (isPressed) {
                    return;
                }
                Vector2f cursor = getApplication().getInputManager().getCursorPosition();
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

    public void selectAction(int skillIndex) {
        SkillConfig skill = selectedActor.get().skills.get(skillIndex);
        if (skill == null || Objects.equals(skill, selectedAction.get())) {
            skill = selectedActor.get().walkSkill;
        }
        selectedAction.set(skill);
        System.out.println("selected: " + skill.name);
    }

    private void passTurn() {
        if (selectedActor.get() == null) {
            System.out.println("cant pass turn, no actor selected.");
            return;
        }
        System.out.println("pass turn of #" + selectedActor.get());
        context.action(context.eventDefs.useSkill.create(selectedActor.get().passTurnSkill.entity));
    }

    private void click(int coordinates) {
        System.out.println("clicked " + Coordinates.x(coordinates) + ", " + Coordinates.y(coordinates));
        if (selectedActor.get() == null) {
            System.out.println("no action for click, no actor selected.");
            return;
        }
        if (!targetableTiles.get().hasKey(coordinates)) {
            System.out.println("no action for click on invalid target tile.");
            return;
        }
        context.action(context.eventDefs.useTargetedSkill.create(selectedAction.get().entity, coordinates));
//        List<Event> events = createEvents(coordinates);
//        for (Event event : events) {
//            if (context.getActions().availableActions(selectedActor.get()).stream().map(x -> x.event).anyMatch(event::equals)) {
//                System.out.println("applying " + context.eventMetaList.get(event.getId()).toString(event));
//                context.action(event);
//                selectedAction.set(defaultAction);
//            } else {
//                System.out.println("illegal");
//                break;
//            }
//        }
    }

//    private List<Event> createEvents(int coordinates) {
//        if (selectedAction.get() == context.componentDefs.walkSkill.id) {
//            System.out.println("walk");
//            Pathfinder pathfinder = new Pathfinder(context.mapWidth(), context.mapHeight());
//            int actorCoordinates = context.getData().get(selectedActor.get(), context.componentDefs.position.id);
//            IntHashSet obstacles = new IntHashSet();
//            for (int obstacle : context.getData().query(context.componentDefs.position.id).list()) {
//                obstacles.set(context.getData().get(obstacle, context.componentDefs.position.id));
//            }
//            IntArrayList path = pathfinder.findPath(actorCoordinates, coordinates, obstacles);
//            int movePoints = context.getData().getOptional(selectedActor.get(), context.componentDefs.movePoints.active.id).orElse(0);
//            if (path.size() > movePoints) {
//                System.out.println("not enough movePoints (" + path.size() + "/" + movePoints + ")");
//                return Collections.emptyList();
//            }
//            List<Event> result = new ArrayList<>();
//            int current = actorCoordinates;
//            for (int next : path) {
//                result.add(context.eventDefs.walkAction.create(selectedActor.get(), current, next));
//                current = next;
//            }
//            return result;
//        }
//        if (selectedAction.get() == context.componentDefs.razorleafSkill.id) {
//            System.out.println("razorLeaf");
//            OptionalInt entity = entityByCoordinates(coordinates);
//            if (entity.isPresent()) {
//                return Arrays.asList(context.eventDefs.razorleafAction.create(selectedActor.get(), entity.getAsInt()));
//            }
//        }
//        return Collections.emptyList();
//    }
//    private OptionalInt entityByCoordinates(int coordinates) {
//        return context.getData().query(context.componentDefs.position.id).unique(x -> context.getData().hasValue(x, context.componentDefs.position.id, coordinates));
//    }

    @Override
    protected void cleanup(Application app) {
    }

    @Override
    protected void onEnable() {
        skillbarContainer.setCullHint(Spatial.CullHint.Dynamic);
    }

    @Override
    protected void onDisable() {
        skillbarContainer.setCullHint(Spatial.CullHint.Never);
    }

    private void addActionMapping(int index) {
        keyMapping("skill" + index, KeyInput.KEY_1 + index, () -> selectAction(index));
    }

    private void keyMapping(String name, int key, Runnable listener) {
        getApplication().getInputManager().addMapping(name, new KeyTrigger(key));
        getApplication().getInputManager().addListener(new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if (!isPressed) {
                    listener.run();
                }
            }
        }, name);
    }

}
