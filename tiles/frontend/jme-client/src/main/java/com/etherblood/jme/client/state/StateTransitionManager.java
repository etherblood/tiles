package com.etherblood.jme.client.state;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class StateTransitionManager {

    private final List<StateTransition> transitionQueue = new ArrayList<>();
    private final Queue<StateTransition> activeTransitions = new PriorityQueue<>(Comparator.comparing(x -> x.getEnd()));
    private final Set<Object> locks = new HashSet<>();
    private TransitionTime currentTime = new TransitionTime(0);

    public void enqueue(StateTransition transition) {
        transitionQueue.add(transition);
    }

    public void update(float seconds) {
        update(currentTime.plusSeconds(seconds));
    }

    private void update(TransitionTime time) {
        activateQueuedTransactions(currentTime);
        while (!activeTransitions.isEmpty() && !activeTransitions.peek().getEnd().isAfter(time)) {
            StateTransition transition = activeTransitions.poll();
            transition.end();
            locks.removeAll(transition.locks());

            activateQueuedTransactions(transition.getEnd());
        }
        for (StateTransition activeTransition : activeTransitions) {
            activeTransition.update(time);
        }
        currentTime = time;
    }

    private void activateQueuedTransactions(TransitionTime time) {
        Set<Object> usedLocks = new HashSet<>(locks);
        for (Iterator<StateTransition> iterator = transitionQueue.iterator(); iterator.hasNext();) {
            StateTransition transition = iterator.next();
            if (transition.locks().stream().anyMatch(usedLocks::contains)) {
                usedLocks.addAll(transition.locks());
                continue;
            }
            iterator.remove();
            usedLocks.addAll(transition.locks());
            transition.start(time);
            activeTransitions.add(transition);
            locks.addAll(transition.locks());
        }
    }
}
