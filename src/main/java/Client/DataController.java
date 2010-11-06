package main.java.Client;

import java.math.BigInteger;

import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.Mongo;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;

public class DataController {

    /*
     *  Class variables
     */

    Mongo m;
    DB db;

    /*
     *  Initializer
     *
     *  Opens a connection to the database for the given user
    ------------------------------------------------------------------------ 
     */

    public static void initialize(String username, String password) {
        // Initialize database connection
        m = new Mongo("localhost", 27017);

        // Get database for specified user
        db = m.getDB("abbc_" + username);
        
        // Authenticate user
        boolean auth = db.authenticate(username, password);

        if (!auth) {
            throw AuthenticationException;
        }
    }

    /*
     *  Storing
     ************************************************************************
     */

    /*
     *  Client
     -----------------------------------------------------------------------
     */

    public static boolean registerClient( String username,
                            String password,
                            JSONObject certificate,
                            BigInteger privateKey) {

    }

    /*
     *  Friends
     -----------------------------------------------------------------------
     */

    public static boolean addFriend(  String firstName,
                        String lastName,
                        String username,
                        JSONObject certificate) {

    }

    /*
     *  Wall Posts
     -----------------------------------------------------------------------
     */

    public static boolean addWallPost( String username,
                        String message) {

    }

    /*
     *  Retrieving
     ************************************************************************
     */

    /*
     *  Client
     -----------------------------------------------------------------------
     */

    public static BigInteger[] getPrivateKey() {

    }

    public static JSONObject getCertificate() {

    }

    public static String getUsername() {

    }

    /*
     *  Friends
     -----------------------------------------------------------------------
     */

    public static JSONObject[] getFriends() {

    }

    public static JSONObject getCertificate(String username) {

    }

    public static boolean isFriend(String username) {

    }

    /*
     *  Wall Posts
     -----------------------------------------------------------------------
     */

    public static JSONObject getWallPosts() {

    }

    /*
     *  Printing
     ************************************************************************
     */

    /*
     *  Friends
     -----------------------------------------------------------------------
     */

    public static void printFriends() {

    }

    /*
     *  Wall Posts
     -----------------------------------------------------------------------
     */

    public static void printWallPosts() {

    }

}