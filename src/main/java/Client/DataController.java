package main.java.Client;

import java.math.BigInteger;
import org.JSON.JSONObject;
import org.JSON.JSONException;

public class DataController {

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

    public static BigInteger getPrivateKey() {

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