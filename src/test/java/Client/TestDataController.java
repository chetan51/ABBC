package test.java.Client;

import junit.framework.TestCase;
import java.math.BigInteger;

import main.java.Client.*;

/*
 *  Note: The database needs to be set up as specified in the
 *  README for these tests to properly work.
 */

public class TestDataController extends TestCase {

    DataController d;

    public void setUp() {
        d = new DataController();
    }

    public void testInitializeWithInvalidUser() {
        boolean testPassed = true;

        try {
            d.initialize("nouser", "testfails");
        }
        catch (Throwable e) {
            testPassed = false;
        }

        assertEquals(testPassed, false);
    }

    public void testInitializeWithValidUser() {
        boolean testPassed = true;

        try {
            d.initialize("testuser", "test");
        }
        catch (Throwable e) {
            testPassed = false;
        }

        assertEquals(testPassed, true);
    }

}