package com.etherblood.core.components;

import com.etherblood.collections.IntList;
import com.etherblood.entities.EntityQuery;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalInt;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

public class GeneralComponent<T> extends Component<T> {

    private final Class<T> clazz;
    private final Map<Integer, T> map = new HashMap<>();

    public GeneralComponent(int id, String name, Class<T> clazz) {
        super(id, name);
        this.clazz = clazz;
    }

    public void set(int entity, T value) {
        map.put(entity, value);
    }

    @Override
    public void remove(int entity) {
        map.remove(entity);
    }

    @Override
    public boolean has(int entity) {
        return map.containsKey(entity);
    }

    @Override
    public T getGeneric(int entity) {
        return map.get(entity);
    }

    @Override
    public EntityQuery query() {
        return new EntityQuery() {
            @Override
            public int count(IntPredicate predicate) {
                return (int) stream(predicate).count();
            }

            @Override
            public OptionalInt unique(IntPredicate predicate) {
                return stream(predicate).findAny();
            }

            @Override
            public void list(IntList out, IntPredicate predicate) {
                stream(predicate).forEach(out::add);
            }

            private IntStream stream(IntPredicate predicate) {
                return map.keySet().stream().mapToInt(x -> x).filter(predicate);
            }
        };
    }

    @Override
    public Class<T> getComponentType() {
        return clazz;
    }

    @Override
    public void setGeneric(int entity, T value) {
        set(entity, value);
    }

    @Override
    public void clear() {
        map.clear();
    }

}
