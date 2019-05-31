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

    protected abstract IntEntityQuery createInstance(Map<Integer, Integer> map);

    @Test
    public void count_IntPredicate() {
        IntEntityQuery query = createInstance(new MapBuilder<Integer, Integer>()
                .with(0, 7)
                .with(1, 7)
                .build());
        Assert.assertEquals(1, query.count(x -> x == 0));
    }

    @Test
    public void count() {
        IntEntityQuery query = createInstance(new MapBuilder<Integer, Integer>()
                .with(0, 7)
                .with(1, 7)
                .build());
        Assert.assertEquals(2, query.count());
    }

    @Test
    public void exists_IntPredicate() {
        IntEntityQuery query = createInstance(new MapBuilder<Integer, Integer>()
                .with(0, 7)
                .with(1, 7)
                .build());
        Assert.assertTrue(query.exists(x -> x == 0));
        Assert.assertFalse(query.exists(x -> x == 2));
    }

    @Test
    public void exists() {
        IntEntityQuery query = createInstance(new MapBuilder<Integer, Integer>()
                .with(0, 7)
                .with(1, 7)
                .build());
        Assert.assertTrue(query.exists());
    }

    @Test
    public void sum_IntPredicate() {
        IntEntityQuery query = createInstance(new MapBuilder<Integer, Integer>()
                .with(0, 5)
                .with(1, 7)
                .with(2, 19)
                .build());
        Assert.assertEquals(12, query.sumValues(x -> x != 2));
    }

    @Test
    public void sum() {
        IntEntityQuery query = createInstance(new MapBuilder<Integer, Integer>()
                .with(0, 5)
                .with(1, 7)
                .build());
        Assert.assertEquals(12, query.sumValues());
    }

    @Test
    public void unique_IntPredicate() {
        IntEntityQuery query = createInstance(new MapBuilder<Integer, Integer>()
                .with(1, 7)
                .build());
        Assert.assertFalse(query.unique(x -> x == 0).isPresent());
    }

    @Test
    public void unique() {
        IntEntityQuery query = createInstance(new MapBuilder<Integer, Integer>()
                .with(1, 7)
                .build());
        Assert.assertEquals(1, query.unique().getAsInt());
    }

    @Test(expected = IllegalStateException.class)
    public void unique_failure() {
        IntEntityQuery query = createInstance(new MapBuilder<Integer, Integer>()
                .with(0, 7)
                .with(1, 7)
                .build());
        query.unique();
    }

    @Test
    public void list_IntPredicate() {
        IntEntityQuery query = createInstance(new MapBuilder<Integer, Integer>()
                .with(1, 7)
                .with(0, 7)
                .with(2, 7)
                .build());
        Assert.assertEquals(Arrays.asList(0, 1), query.list(x -> x != 2).boxed());
    }

    @Test
    public void list() {
        IntEntityQuery query = createInstance(new MapBuilder<Integer, Integer>()
                .with(1, 7)
                .with(0, 7)
                .build());
        Assert.assertEquals(Arrays.asList(0, 1), query.list().boxed());
    }

}
