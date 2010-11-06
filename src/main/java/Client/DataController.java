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

    boolean registerClient( String username,
                            String password,
                            JSONObject certificate,
                            BigInteger privateKey) {

    }

    /*
     *  Friends
     -----------------------------------------------------------------------
     */

    boolean addFriend(  String firstName,
                        String lastName,
                        String username,
                        JSONObject certificate) {

    }

    /*
     *  Wall Posts
     -----------------------------------------------------------------------
     */

    boolean addWallPost( String username,
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

    BigInteger getPrivateKey() {

    }

    JSONObject getCertificate() {

    }

    String getUsername() {

    }

    /*
     *  Friends
     -----------------------------------------------------------------------
     */

    JSONObject[] getFriends() {

    }

    JSONObject getCertificate(String username) {

    }

    boolean isFriend(String username) {

    }

    /*
     *  Wall Posts
     -----------------------------------------------------------------------
     */

    JSONObject getWallPosts() {

    }

    /*
     *  Printing
     ************************************************************************
     */

    /*
     *  Friends
     -----------------------------------------------------------------------
     */

    void printFriends() {

    }

    /*
     *  Wall Posts
     -----------------------------------------------------------------------
     */

    void printWallPosts() {

    }

}