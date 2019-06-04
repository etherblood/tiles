package com.etherblood.jme3.gui;

import com.etherblood.core.AnimationEvent;
import com.etherblood.core.AnimationsController;
import com.etherblood.core.util.Coordinates;
import com.etherblood.jme3.gui.sprites.Sprite;
import com.etherblood.jme3.gui.state.StateTransitionAppstate;
import com.etherblood.jme3.gui.state.transitions.SpriteAnimationTransition;
import com.etherblood.mods.core.game.systems.attack.animation.AttackAnimation;
import com.etherblood.mods.core.game.systems.death.DieAnimation;
import com.etherblood.mods.core.game.systems.walk.animation.PushAnimation;
import com.etherblood.mods.core.game.systems.walk.animation.WalkAnimation;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ActorAnimationsAppstate extends AbstractAppState {

    private final AnimationsController animations;
    private ActorSpritesAppstate actorSpritesAppstate;
    private StateTransitionAppstate transitionAppstate;
    private final Map<Class<? extends AnimationEvent>, Consumer<? extends AnimationEvent>> eventHandlers;
    private int animationIndex = 0;

    public ActorAnimationsAppstate(AnimationsController animations) {
        this.animations = animations;
        eventHandlers = new HashMap<>();
        eventHandlers.put(WalkAnimation.class, (Consumer<WalkAnimation>) this::handleWalkAnimation);
        eventHandlers.put(PushAnimation.class, (Consumer<PushAnimation>) this::handlePushAnimation);
        eventHandlers.put(AttackAnimation.class, (Consumer<AttackAnimation>) this::handleAttackAnimation);
        eventHandlers.put(DieAnimation.class, (Consumer<DieAnimation>) this::handleDieAnimation);
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        actorSpritesAppstate = stateManager.getState(ActorSpritesAppstate.class);
        transitionAppstate = stateManager.getState(StateTransitionAppstate.class);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void update(float tpf) {
        List<AnimationEvent> events = animations.getAnimationEvents();
        for (AnimationEvent event : events.subList(animationIndex, events.size())) {
            Consumer handler = eventHandlers.get(event.getClass());
            handler.accept(event);
        }
        animationIndex = events.size();
    }

    private void handleWalkAnimation(WalkAnimation event) {
        Sprite sprite = actorSpritesAppstate.getActorSprite(event.actor);
        String animation;
        if (sprite.isAnimationSupported("walk0") && sprite.isAnimationSupported("walk1")) {
            animation = "walk" + ((Coordinates.x(event.to) + Coordinates.y(event.to)) & 1);
        } else if (sprite.isAnimationSupported("walk")) {
            animation = "walk";
        } else {
            return;
        }
        transitionAppstate.enqueue(new SpriteAnimationTransition(event.from, event.to, sprite, animation));
    }

    private void handlePushAnimation(PushAnimation event) {
        Sprite sprite = actorSpritesAppstate.getActorSprite(event.actor);
        String animation;
        if (sprite.isAnimationSupported("walk0") && sprite.isAnimationSupported("walk1")) {
            animation = "walk" + ((Coordinates.x(event.to) + Coordinates.y(event.to)) & 1);
        } else if (sprite.isAnimationSupported("walk")) {
            animation = "walk";
        } else {
            return;
        }
        transitionAppstate.enqueue(new SpriteAnimationTransition(event.from, event.to, sprite, animation, SpriteAnimationTransition.frameType(event.to, event.from, sprite)));
    }

    private void handleAttackAnimation(AttackAnimation event) {
        Sprite sprite = actorSpritesAppstate.getActorSprite(event.actor);
        String animation;
        if (sprite.isAnimationSupported("attack")) {
            animation = "attack";
        } else {
            return;
        }
        transitionAppstate.enqueue(new SpriteAnimationTransition(event.from, event.from, sprite, animation, SpriteAnimationTransition.frameType(event.from, event.to, sprite)));
    }

    private void handleDieAnimation(DieAnimation event) {
        Sprite sprite = actorSpritesAppstate.getActorSprite(event.actor);
        String animation;
        if (sprite.isAnimationSupported("die")) {
            animation = "die";
        } else if (sprite.isAnimationSupported("death")) {
            animation = "death";
        } else {
            return;
        }
        transitionAppstate.enqueue(new SpriteAnimationTransition(event.position, event.position, sprite, animation));
    }

}
