package com.etherblood.rules.events;

import com.etherblood.events.AbstractEventMeta;

/**
 *
 * @author Philipp
 */
public class VoidEventMeta extends AbstractEventMeta<VoidEvent> {

    public VoidEventMeta(int id, String name) {
        super(id, name);
    }

    public VoidEvent create() {
        return new VoidEvent(id());
    }

}
