package com.etherblood.events;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Philipp
 */
public class EventDefinition {

    private final String name;
    private final int id;
    private final ArgumentDefinition[] arguments;

    EventDefinition(String name, int id, ArgumentDefinition... args) {
        this.name = name;
        this.id = id;
        this.arguments = args;
    }

    public static EventDefinitionBuilder builder() {
        return new EventDefinitionBuilder();
    }

    public String getName() {
        return name;
    }

    public int id() {
        return id;
    }
    
    public int index() {
        return eventIndex(id);
    }
    
    public int argumentCount() {
        return eventArgumentCount(id);
    }

    public ArgumentDefinition[] getArguments() {
        return arguments;
    }

    public Object lazyString(int... args) {
        assert args.length == arguments.length : "length mismatch for " + name + ", actual: " + args.length + ", expected: " + arguments.length;
        return new Object() {
            @Override
            public String toString() {
                String string = name + "{";
                for (int i = 0; i < args.length; i++) {
                    if (i != 0) {
                        string += ", ";
                    }
                    ArgumentDefinition argument = arguments[i];
                    string += argument.getName();
                    string += '=';
                    string += argument.toReadable(args[i]);
                }
                return string + '}';
            }

        };
    }

    public static class EventDefinitionBuilder {

        private String name;
        private int index;
        private final List<ArgumentDefinition> arguments = new ArrayList<>();

        public EventDefinitionBuilder name(String name) {
            this.name = name;
            return this;
        }

        public EventDefinitionBuilder index(int index) {
            this.index = index;
            return this;
        }

        public EventDefinitionBuilder withSimpleArgument(String argumentName) {
            return withArgument(new ArgumentDefinition(argumentName, false));
        }

        public EventDefinitionBuilder withEntityArgument(String argumentName) {
            return withArgument(new ArgumentDefinition(argumentName, true));
        }

        public EventDefinitionBuilder withArgument(ArgumentDefinition argument) {
            this.arguments.add(argument);
            return this;
        }

        public EventDefinition build() {
            return new EventDefinition(name, eventId(index, arguments.size()), arguments.toArray(new ArgumentDefinition[arguments.size()]));
        }

    }
    
    public static int eventIndex(int eventId) {
        return eventId & 0xFFFF;
    }
    
    public static int eventArgumentCount(int eventId) {
        return eventId >> 16;
    }
    
    public static int eventId(int index, int argumentCount) {
        return index | (argumentCount << 16);
    }

}
