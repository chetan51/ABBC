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

    static Mongo m;
    static DB db;

    /*
     *  Initializer
     *
     *  Opens a connection to the database for the given user
    ------------------------------------------------------------------------ 
     */

    public static void initialize(String username, String password) throws Throwable{
        // Initialize database connection
        m = new Mongo("localhost", 27017);

        // Get database for specified user
        db = m.getDB("abbc_" + username);
        
        // Authenticate user
        boolean auth = db.authenticate(username, password.toCharArray());

        if (!auth) {
            throw new Throwable("Unable to authenticate database connection");
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
        return true;
    }

    /*
     *  Friends
     -----------------------------------------------------------------------
     */

    public static boolean addFriend(  String firstName,
                        String lastName,
                        String username,
                        JSONObject certificate) {
        return true;
    }

    /*
     *  Wall Posts
     -----------------------------------------------------------------------
     */

    public static boolean addWallPost( String username,
                        String message) {
        return true;
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
        return null;
    }

    public static JSONObject getCertificate() {
        return null;
    }

    public static String getUsername() {
        return null;
    }

    /*
     *  Friends
     -----------------------------------------------------------------------
     */

    public static JSONObject[] getFriends() {
        return null;
    }

    public static JSONObject getCertificate(String username) {
        return null;
    }

    public static boolean isFriend(String username) {
        return true;
    }

    /*
     *  Wall Posts
     -----------------------------------------------------------------------
     */

    public static JSONObject getWallPosts() {
        return null;
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