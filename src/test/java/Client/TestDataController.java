package test.java.Client;

import junit.framework.TestCase;
import java.math.BigInteger;

import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.Mongo;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;

import main.java.RSAEngine.*;
import main.java.CryptoEngine.*;
import main.java.Client.*;

/*
 *  Note: The database needs to be set up as specified in the
 *  README for these tests to properly work.
 */

public class TestDataController extends TestCase {

    DataController d;

    static Mongo m;
    static DB db;

    String username;
    String password;

    public void setUp() throws Exception {
        username = "testuser";
        password = "test";

        d = new DataController();

        // Initialize database connection
        m = new Mongo("localhost", 27017);

        // Get database for specified user
        db = m.getDB("abbc_" + username);
        
        // Authenticate user
        boolean auth = db.authenticate(username, password.toCharArray());

        if (!auth) {
            throw new Exception("Unable to authenticate database connection");
        }
    
    }

    public void tearDown() {
        // Get the wallposts collection
    	DBCollection coll = db.getCollection("wallposts");

        // Drop the collection for a fresh start
        coll.drop();
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

    public void testRegisterAndGetClientInformation() throws JSONException {
        String username = "testclient";
        String password = "password";

        KeyGen k = new KeyGen();
        BigInteger[] key = k.GenerateKey(256);
        CertificateGenerator cg = new CertificateGenerator("Test", "Client", "testclient", "test@test.com", "note");
        String certificate = cg.generate(key[2], key[0]);

        d.registerClient(username, password, certificate, key[1]);

        assertEquals(d.getPrivateKey(), key[1]);
        assertEquals(d.getCertificate(), certificate);
        assertEquals(d.getUsername(), username);
    }

    public void testAdd0AndGetWallPosts() {
        try {
            assertEquals(d.getWallPosts().length, 0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testAdd2AndGetWallPosts() {
        try {
            d.addWallPost("testuser1", "testmessage1");
            d.addWallPost("testuser2", "testmessage2");

            JSONObject[] wallPosts = d.getWallPosts();

            assertEquals(wallPosts.length, 2);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testPrintWallPosts() {
        try {
            d.addWallPost("testuser1", "testmessage1");
            d.addWallPost("testuser2", "testmessage2");

            JSONObject[] wallPosts = d.getWallPosts();

            // Visually inspect this
            System.out.println("\n\nShould print two wallposts from testuser1 and testuser2:\n");
            d.printWallPosts();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
