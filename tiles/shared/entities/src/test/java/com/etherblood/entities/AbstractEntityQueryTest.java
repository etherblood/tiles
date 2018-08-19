package com.etherblood.entities;

import com.etherblood.collections.MapBuilder;
import java.util.Arrays;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Philipp
 */
public abstract class AbstractEntityQueryTest {

    protected abstract EntityQuery createInstance(Map<Integer, Integer> map);

    @Test
    public void count_IntPredicate() {
        EntityQuery query = createInstance(new MapBuilder<Integer, Integer>()
                .with(0, 7)
                .with(1, 7)
                .build());
        Assert.assertEquals(1, query.count(x -> x == 0));
    }

    @Test
    public void count() {
        EntityQuery query = createInstance(new MapBuilder<Integer, Integer>()
                .with(0, 7)
                .with(1, 7)
                .build());
        Assert.assertEquals(2, query.count());
    }

    @Test
    public void exists_IntPredicate() {
        EntityQuery query = createInstance(new MapBuilder<Integer, Integer>()
                .with(0, 7)
                .with(1, 7)
                .build());
        Assert.assertTrue(query.exists(x -> x == 0));
        Assert.assertFalse(query.exists(x -> x == 2));
    }

    @Test
    public void exists() {
        EntityQuery query = createInstance(new MapBuilder<Integer, Integer>()
                .with(0, 7)
                .with(1, 7)
                .build());
        Assert.assertTrue(query.exists());
    }

    @Test
    public void aggregate_IntPredicate() {
        EntityQuery query = createInstance(new MapBuilder<Integer, Integer>()
                .with(0, 5)
                .with(1, 7)
                .with(2, 19)
                .build());
        Assert.assertEquals(12, query.aggregate(Integer::sum, x -> x != 2).getAsInt());
    }

    @Test
    public void aggregate() {
        EntityQuery query = createInstance(new MapBuilder<Integer, Integer>()
                .with(0, 5)
                .with(1, 7)
                .build());
        Assert.assertEquals(12, query.aggregate(Integer::sum).getAsInt());
    }

    @Test
    public void unique_IntPredicate() {
        EntityQuery query = createInstance(new MapBuilder<Integer, Integer>()
                .with(1, 7)
                .build());
        Assert.assertFalse(query.unique(x -> x == 0).isPresent());
    }

    @Test
    public void unique() {
        EntityQuery query = createInstance(new MapBuilder<Integer, Integer>()
                .with(1, 7)
                .build());
        Assert.assertEquals(1, query.unique().getAsInt());
    }

    @Test(expected = IllegalStateException.class)
    public void unique_failure() {
        EntityQuery query = createInstance(new MapBuilder<Integer, Integer>()
                .with(0, 7)
                .with(1, 7)
                .build());
        query.unique();
    }

    @Test
    public void list_IntPredicate() {
        EntityQuery query = createInstance(new MapBuilder<Integer, Integer>()
                .with(1, 7)
                .with(0, 7)
                .with(2, 7)
                .build());
        Assert.assertEquals(Arrays.asList(0, 1), query.list(x -> x != 2).boxed());
    }

    @Test
    public void list() {
        EntityQuery query = createInstance(new MapBuilder<Integer, Integer>()
                .with(1, 7)
                .with(0, 7)
                .build());
        Assert.assertEquals(Arrays.asList(0, 1), query.list().boxed());
    }

}
