package com.etherblood.core;

import com.etherblood.core.components.Component;
import com.etherblood.core.components.ComponentRegistry;
import com.etherblood.core.random.HistoryRandom;
import com.etherblood.core.random.RandomObserver;
import com.etherblood.core.to.HistoryAction;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GameContext {

    private final EntityFactory entityFactory;
    private final ComponentRegistry registry;
    private final List<GameSystem> systems;
    private final Map<Class<?>, ?> components;
    private final ActionController controller;
    private final GameResultProvider result;
    private final HistoryRandom random;
    private final AnimationsController animationsController;
    private final ActionGenerator actionGenerator;
    private transient int version = 0;

    public GameContext(EntityFactory entityFactory, ComponentRegistry registry, List<GameSystem> systems, List<?> components, ActionController controller, GameResultProvider result, HistoryRandom random, AnimationsController animationsController, ActionGenerator actionGenerator) {
        this.entityFactory = entityFactory;
        this.registry = registry;
        this.systems = systems;
        this.components = components.stream().collect(Collectors.toMap(x -> x.getClass(), x -> x));
        this.controller = controller;
        this.result = result;
        this.random = random;
        this.animationsController = animationsController;
        this.actionGenerator = actionGenerator;
    }

    public void applyHistoryAction(HistoryAction action) {
        for (int randomResult : action.getRandomHistory()) {
            random.enqueue(randomResult);
        }
        controller.useAction(action.getPlayerIndex(), action.getAction());
        version++;
        assert action.getGameVersion() == version : "HistoryAction applied out of order.";
    }

    public HistoryAction applyAction(int player, Action action) {
        RandomObserver observer = random.createObserver();
        controller.useAction(player, action);
        return new HistoryAction(++version, player, action, observer.getNext().stream().toArray());
    }

    public void setMaster() {
        random.setMasterMode(true);
    }

    public void setSlave() {
        random.setMasterMode(false);
    }

    public EntityFactory getEntityFactory() {
        return entityFactory;
    }

    public ComponentRegistry getRegistry() {
        return registry;
    }

    public List<GameSystem> getSystems() {
        return systems;
    }

    @SuppressWarnings("unchecked")
    public <S> S getSystem(Class<S> systemType) {
        for (GameSystem system : systems) {
            if (systemType.isInstance(system)) {
                return (S) system;
            }
        }
        return null;
    }

    public Map<Class<?>, ?> getComponents() {
        return components;
    }

    @SuppressWarnings("unchecked")
    public <S> S getComponents(Class<S> componentsType) {
        return (S) components.get(componentsType);
    }

    public ActionController getController() {
        return controller;
    }

    public AnimationsController getAnimationsController() {
        return animationsController;
    }

    public HistoryRandom getRandom() {
        return random;
    }

    public GameResultProvider getResult() {
        return result;
    }

    public ActionGenerator getActionGenerator() {
        return actionGenerator;
    }

    public int getVersion() {
        return version;
    }

    public void clear() {
        version = 0;
        animationsController.getAnimationEvents().clear();
        for (Component<?> component : registry.getComponents()) {
            for (int entity : component.query().list()) {
                component.remove(entity);
            }
        }
        random.clear();
        entityFactory.reset();
    }
}
