package com.etherblood.entities.query;

import com.etherblood.entities.ComponentDefinition;
import java.util.function.IntPredicate;

/**
 *
 * @author Philipp
 */
public interface ComponentPredicate extends IntPredicate {

    ComponentDefinition getComponent();
}
