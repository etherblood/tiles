package com.etherblood.jme.client;

import com.simsilica.lemur.Container;
import com.simsilica.lemur.Label;

/**
 *
 * @author Philipp
 */
public class ActorDetailsContainer extends Container {

    private final Label actorName, health, actionPoints, movePoints;

    public ActorDetailsContainer() {
        super("ActorDetailsNode");
        this.actorName = new Label(null);
        this.health = new Label(null);
        this.actionPoints = new Label(null);
        this.movePoints = new Label(null);

        addChild(actorName);
        addChild(health);
        addChild(actionPoints);
        addChild(movePoints);
    }

    public void setActorName(String name) {
        this.actorName.setText(name);
    }

    public void setHealth(int active, int max) {
        this.health.setText(active + "/" + max + " health");
    }

    public void setActionPoints(int active, int max) {
        this.actionPoints.setText(active + "/" + max + " action points");
    }

    public void setMovePoints(int active, int max) {
        this.movePoints.setText(active + "/" + max + " move points");
    }
}
