package com.etherblood.rules.context;

import com.etherblood.entities.SimpleComponentMap;

/**
 *
 * @author Philipp
 */
public class StatComponentMaps {

    public final SimpleComponentMap base;
    public final SimpleComponentMap additive;
    public final SimpleComponentMap active;
    public final SimpleComponentMap buffed;

    public StatComponentMaps(SimpleComponentMap base, SimpleComponentMap additive, SimpleComponentMap active, SimpleComponentMap buffed) {
        this.base = base;
        this.additive = additive;
        this.active = active;
        this.buffed = buffed;
    }

}
