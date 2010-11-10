package test.java.Client;

import junit.framework.TestCase;
import java.math.BigInteger;

import org.json.JSONException;
import org.json.JSONObject;

import main.java.RSAEngine.*;
import main.java.CryptoEngine.*;
import main.java.Client.*;

public class TestMessageController extends TestCase {

    MessageController m;

    public void setUp() throws Exception {
        m = new MessageController();
    }

    public void tearDown() {

    }

}
