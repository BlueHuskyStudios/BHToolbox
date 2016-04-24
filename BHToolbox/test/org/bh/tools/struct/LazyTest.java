package org.bh.tools.struct;

import java.util.function.Supplier;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kyli Rouge
 */
public class LazyTest {

    private static boolean isGood;

    public LazyTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of setSupplier method, of class Lazy.
     */
    @Test
    public void testSetSupplier() {
        System.out.println("setSupplier");
        Supplier bad = () -> "Bad";
        Supplier good = () -> "Good";
        Lazy<String> instance = new Lazy<>(bad);
        instance.setSupplier(good);
        assertEquals(instance.get(), good.get());
    }

    /**
     * Test of get method, of class Lazy.
     */
    @Test
    public void testGet() {
        System.out.println("get");
        Lazy<String> instance = new Lazy<>(() -> "Good");
        Object expResult = "Good";
        Object result = instance.get();
        assertEquals(expResult, result);
    }

    /**
     * Test of clear method, of class Lazy.
     */
    @Test
    public void testClear() {
        System.out.println("clear");
        isGood = false;
        Lazy instance = new Lazy(() -> isGood ? "Good" : "Bad");
        assertEquals(instance.get(), "Bad");
        isGood = true;
        assertEquals(instance.get(), "Bad");
        instance.clear();
        assertEquals(instance.get(), "Good");
    }

    /**
     * Test of isSet method, of class Lazy.
     */
    @Test
    public void testIsSet() {
        System.out.println("isSet");
        Lazy<String> instance = new Lazy<>(() -> "Bad");
        assertEquals(instance.isSet(), false);
        instance.get();
        assertEquals(instance.isSet(), true);
    }

}
