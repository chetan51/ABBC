package test.java.Client;

import junit.framework.TestCase;
import java.math.BigInteger;

import java.security.NoSuchAlgorithmException;

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

public class TestMessageController extends TestCase {

    MessageController m;
	
	static Mongo mon;
    static DB db;
    
    DataController d;
 
    String username;
    String password;
	String friendRequest;

    public void setUp() throws Exception {
        m = new MessageController();
        username = "testuser";
        password = "test";

        d = new DataController();
        
		// Initialize database connection
        mon = new Mongo("localhost", 27017);

        // Get database for specified user
        db = mon.getDB("abbc_" + username);
        
        // Authenticate user
        boolean auth = db.authenticate(username, password.toCharArray());

        if (!auth) {
            throw new Exception("Unable to authenticate database connection");
        }
		
        KeyGen k = new KeyGen();
        BigInteger[] key = k.GenerateKey(256);
        CertificateGenerator cg = new CertificateGenerator("Test", "Client", "testclient", "test@test.com", "note");
        String certificate = cg.generate(key[2], key[0]);
		
		d.initialize(username, password);

        d.registerClient(username, password, certificate, key[1]);
    }

    public void tearDown() {
		// Get the wallposts collection
    	DBCollection coll = db.getCollection("wallposts");
		DBCollection friendcoll = db.getCollection("friends");
        // Drop the collection for a fresh start
        coll.drop();
		friendcoll.drop();
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
		
		String username2 = "testuser";
        String password2 = "test";

        KeyGen k2 = new KeyGen();
        BigInteger[] key2 = k2.GenerateKey(256);
        CertificateGenerator cg2 = new CertificateGenerator("Test", "Client", "testclient", "test@test.com", "note");
        String certificate2 = cg2.generate(key[2], key[0]);
        
        friendRequest = m.createMessage("friend1", "friend_request", certificate2);
        System.out.println("message for friend request to friend1");
		System.out.println(friendRequest);
	}
	
	public void testGenerateMessage(){
		String username = "cs161";
		
		CertificateGenerator cg = new CertificateGenerator("CS", "TAs", "cs161", "test@test.com", "note");
		BigInteger hise = new BigInteger("5", 16);
	    BigInteger hisn = new BigInteger("969a193f7f95592941653b50e55deea362254d71ceac43466219fe20abfebe6313b909eed1d3665b87e7223cc6a2596b23f5dd1e91dac4fcd7388437421eac483f6f468b11050a1b4ad70ab7ca918ada85735caf180b5d50faa1f8be14f3e720452749f067d6db30a45480f818e2b158d7e4d36baa98ab5af1f4e235a48456dde9fe5396ddc3f1aadc2de9b652e7e477f4e3ba11d75997eb06f82ddb5709f8a780a20357878321ac3ef11c2774592a5c6845df3ae4fe622427b6d6ac420e9630afa44aef9be96e7b25596b8bb707d72bc8f2a321868e7f26edeacd7228efe4ed9b3688c888638f9d3a5b36d96012296ea9bbba5eb80b436c5ce190324cb53c2d", 16);
	    	
		String certificate = cg.generate(hisn, hise);
		d.addFriend("CS TAs", username, certificate);
		
		String fr = m.createMessage(username, "friend_request", "{"message":"{\"not_before\":1288833383449,\"username\":\"bchen\",\"email\":\"bchen311@gmail.com\",\"public_exponent\":\"5\",\"realname\":\"Chen, Brandon\",\"notes\":\"cool\",\"modulus\":\"57133a2b206e0e95aca52c385e87040b3db0fea5ad35cd73b6c1bceb24dfbbe7711113d52101478c3d1807b9b680cc5f66db535efd9162c787491dcfbaf7bfd0a5f1d781b6e7b4546bd966d72ff739a630c1ea31f6be64c4b692e01d491c4eef59f379c9877b1fe894d9f70c85f5d6a310eee6cabc5affbed709d7f41699d264fbb28b1e4a835720243c4b7e9b493c83f0119a6fa2f493d71233df1aeec0cae1011dcd758d9a6b900cdcd15aca684776ab31b6909a93c69a1ca68cd71a983c8504b0fffaaebf36cfb9e827508640fbad638a029b7b8cc3f5135d723f695233b784e6bcc42525461ece7b9fc8ee9feacca4d32fbc3f5c0698311ae97cac445071\",\"not_after\":1288864919449,\"version\":1}","username":"bchen","signature":"5125070ef661c7ac9bfdcc105ae343944d023acf8325f87cbb0f02dd893efd158824a0ad80683e7ad96a7095c0fa3e0e8664aa11877e0f54d0a0d742ebe74ba13573cd4c3f5f7d9088d4b422ef76b521c41ef71c1bd0eab01778f55e43ead285573c150065d5d30919d53ac5dc78b6560f4dad0e3c9b7de76e9993e2748f48310b6c8e26072b5441aae1a263fff0a36efe55f82f34355d7872513ac2f068f9a4264ef60bc5c4f1cba3023b048e6822af795765aa11dd930e414095fad757f8317da2d0f8df38005879a70b316f07aaf021ee51e8cb9d003b962f1a46e876eee0c371764a6055d51a718d5f32931721cb73c466498a2f7f117f21edb71ba4e0e5"}");
		System.out.println("our friend request:");
		System.out.println(fr);
	}
	
	public void testProcessMessage() throws JSONException{
		
		/*String username = "friend1";
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
        }*/
	}
}