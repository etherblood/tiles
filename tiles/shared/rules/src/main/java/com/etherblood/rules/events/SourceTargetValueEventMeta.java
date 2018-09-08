package com.etherblood.rules.events;

import com.etherblood.events.AbstractEventMeta;

/**
 *
 * @author Philipp
 */
public class SourceTargetValueEventMeta extends AbstractEventMeta<SourceTargetValueEvent> {

    public SourceTargetValueEventMeta(int id, String name) {
        super(id, name);
    }

    public SourceTargetValueEvent create(int source, int target, int value) {
        return new SourceTargetValueEvent(id(), source, target, value);
    }

}
