package test.java.Client;

import junit.framework.TestCase;
import java.math.BigInteger;

import main.java.Client.*;

public class TestDataController extends TestCase {

    DataController d;

    public void setUp() {
        d = new DataController();
    }

    public void testInitialize() {
        d.initialize("test", "fails");
    }

}