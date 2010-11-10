package test.java.Client;

import junit.framework.TestCase;
import java.math.BigInteger;

import java.security.NoSuchAlgorithmException;

import org.json.JSONException;
import org.json.JSONObject;

import main.java.RSAEngine.*;
import main.java.CryptoEngine.*;
import main.java.Client.*;

public class TestMessageController extends TestCase {

    MessageController m;
    
    DataController d;
 
    String username;
    String password;

    public void setUp() throws Exception {
        m = new MessageController();
        username = "testuser";
        password = "test";

        d = new DataController();
        
        KeyGen k = new KeyGen();
        BigInteger[] key = k.GenerateKey(256);
        CertificateGenerator cg = new CertificateGenerator("Test", "Client", "testclient", "test@test.com", "note");
        String certificate = cg.generate(key[2], key[0]);
		
		d.initialize(username, password);

        d.registerClient(username, password, certificate, key[1]);
    }

    public void tearDown() {

    }
	
	public void testCreateMessage() throws JSONException, NoSuchAlgorithmException{
		String username = "friend1";
		String password = "password";

		KeyGen k = new KeyGen();
		BigInteger[] key = k.GenerateKey(256);
		CertificateGenerator cg = new CertificateGenerator("Test", "Client", "friend1", "test@test.com", "note");
		String certificate = cg.generate(key[2], key[0]);
	
		JSONObject j = new JSONObject(certificate);
		d.addFriend("friend1", username, j);
		
		String wall = m.createMessage("friend1", "wall_post", "sup");
		System.out.println("message for wallpost to friend1");
		System.out.println(wall);
		
		String username2 = "testclient";
        String password2 = "password";

        KeyGen k2 = new KeyGen();
        BigInteger[] key2 = k2.GenerateKey(256);
        CertificateGenerator cg2 = new CertificateGenerator("Test", "Client", "testclient", "test@test.com", "note");
        String certificate2 = cg2.generate(key[2], key[0]);
        
        String friendRequest = m.createMessage("friend1", "friend_request", certificate2);
        System.out.println("message for friend request to friend1");
		System.out.println(friendRequest);
	}
	
	public void testProcessMessage() throws JSONException{
		
		String username = "friend1";
		String password = "password";

		KeyGen k = new KeyGen();
		BigInteger[] key = k.GenerateKey(256);
		CertificateGenerator cg = new CertificateGenerator("Test", "Client", "friend1", "test@test.com", "note");
		String certificate = cg.generate(key[2], key[0]);
	
		JSONObject j = new JSONObject(certificate);
		d.addFriend("friend1", username, j);
		
		String username2 = "testclient";
        String password2 = "password";

        KeyGen k2 = new KeyGen();
        BigInteger[] key2 = k2.GenerateKey(256);
        CertificateGenerator cg2 = new CertificateGenerator("Test", "Client", "testclient", "test@test.com", "note");
        String certificate2 = cg2.generate(key[2], key[0]);
        
        String friendRequest = m.createMessage("friend1", "friend_request", certificate2);
        JSONObject j2 = new JSONObject(friendRequest);
        
        try{
        	m.processMessage(j2);
			System.out.println("Should print one friend:");
			d.printFriends();
        }
        catch (Exception e){
        	return;
        }
	}
}