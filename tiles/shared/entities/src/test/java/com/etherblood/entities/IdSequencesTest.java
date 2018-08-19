package com.etherblood.entities;

import com.etherblood.collections.IntSet;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntSupplier;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Philipp
 */
public class IdSequencesTest {

    private static final int ID_COUNT = 10000;
    private static final int ID_RATE = 20;

    public IdSequencesTest() {
    }

    @Test
    public void testIncremental() {
        sequenceTest("incremental", new AtomicInteger(0)::getAndIncrement);
    }

    @Test
    public void testSimple1() {
        sequenceTest("simple1", IdSequences.simple1());
    }

    @Test
    public void testSimple2() {
        sequenceTest("simple2", IdSequences.simple2());
    }

    @Test
    public void testSimple3() {
        sequenceTest("simple3", IdSequences.simple3());
    }

    @Test
    public void testBackedRandom() {
        sequenceTest("backedRandom", IdSequences.backingSet(new Random(5)::nextInt));
    }

    @Test
    public void testBackedSecureRandom() {
        sequenceTest("backedSecureRandom", IdSequences.backingSet(new SecureRandom()::nextInt));
    }

    private void sequenceTest(String name, IntSupplier sequence) {
        System.out.println(name);
        Random random = new Random(ID_COUNT);
        IntSet set = new IntSet();
        for (int i = 0; i < ID_COUNT; i++) {
            while (random.nextInt(ID_RATE) != 0) {
                sequence.getAsInt();
            }
            set.set(sequence.getAsInt());
        }
        System.out.println("quality estimate: " + set.estimateHashQuality());
        assertEquals(ID_COUNT, set.size());
    }

}
