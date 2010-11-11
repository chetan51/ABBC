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
        username = "bolotov";
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
        BigInteger[] key = k.GenerateKey(512);
        CertificateGenerator cg = new CertificateGenerator("Alex", "Bolotov", "bolotov", "bolotov@berkeley.edu", "hello");
        key[0] = new BigInteger("7", 16);
        key[1] = new BigInteger("7fdb7d98a1d2daa6bb31c5cb8462fe807cf5e5c0a35315fd6026cc189e9068fd631ba809cfe088ac1b4035b31cf0ed0afa11187428130fb7ea474f62d8403480a80af786336aaabefc31273ea6548d2e7e1b107ffb39051d70ff95e35213dd73a199663aeeca0a078547ecbe3c68bbcbbc39a27303ac7a772fb8b6298c9e7e3677454f37e01267aea7e1d85e688ef7f02acff5b07b7aeef1202333ae7e72fc1c75cbfc94d95d5f2a138eec7f3a80c9bcb53cb93905b7f89249a9da2ba8870dd6bd7350e9f0f13d3769206e7a94407d2cb8e1facca8872e73bc0b725ca75599fae181ec0d16c7313db260a43bddaea275c7a2df9a3f7301ee31d4fbf3724b0a47", 16);
        key[2] = new BigInteger("952abd321220a9c2850f66c2c51e28eb3c7436b613e0eefcf02d43720e5325279e4aeeb61d309f7375203ea64c6e69e223be9c8784163d013bfddc9dfc4ae7eb6eb7761c9151c73426395873c20d4f60e874e8954fc285f7ae7f8433dfc1d7b191dda1ef6bebb65e1b7e9433467a306db0ede830d99e8ee062577f3079639341136703abcba1e0a4e8b2ba74770c62bda18e47b641de128e57c2fdcde8861ab1ffb588c6dc7c2527512016cd70af25eb24ea98cc9dd39fa6bcbd24e2457462ba75645aa1a8744fb78b8030219b7a15c7519b03a22af0d80493ae03f918535e3a2c185dd1759ae9a251fd1e75c3501bea1d3d67146f1acd4c18644221f12246a7", 16);
        String certificate = cg.generate(key[2], key[0]);
		
		d.initialize(username, password);

        d.registerClient(username, password, certificate, key[1]);

		String username = "cs161";
		
		CertificateGenerator cg2 = new CertificateGenerator("CS", "TAs", "cs161", "test@test.com", "note");
		BigInteger hise = new BigInteger("5", 16);
	    BigInteger hisn = new BigInteger("969a193f7f95592941653b50e55deea362254d71ceac43466219fe20abfebe6313b909eed1d3665b87e7223cc6a2596b23f5dd1e91dac4fcd7388437421eac483f6f468b11050a1b4ad70ab7ca918ada85735caf180b5d50faa1f8be14f3e720452749f067d6db30a45480f818e2b158d7e4d36baa98ab5af1f4e235a48456dde9fe5396ddc3f1aadc2de9b652e7e477f4e3ba11d75997eb06f82ddb5709f8a780a20357878321ac3ef11c2774592a5c6845df3ae4fe622427b6d6ac420e9630afa44aef9be96e7b25596b8bb707d72bc8f2a321868e7f26edeacd7228efe4ed9b3688c888638f9d3a5b36d96012296ea9bbba5eb80b436c5ce190324cb53c2d", 16);
	    	
		String certificate2 = cg2.generate(hisn, hise);
		d.addFriend("CS TAs", username, new JSONObject(certificate2));
    }

    public void tearDown() {
		// Get the wallposts collection
    	DBCollection coll = db.getCollection("wallposts");
		DBCollection friendcoll = db.getCollection("friends");
        // Drop the collection for a fresh start
        coll.drop();
		friendcoll.drop();
    }
	
	/*public void testCreateMessage() throws JSONException, NoSuchAlgorithmException{
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
	}*/
	
	public void testGenerateMessage() throws JSONException {
		
		String fr = m.createMessage("cs161", "friend_request", "{\"username\": \"cs161\", \"message\": \"{\\\"not_before\\\":1288998011600,\\\"username\\\":\\\"bolotov\\\",\\\"email\\\":\\\"bolotov@berkeley.edu\\\",\\\"public_exponent\\\":\\\"7\\\",\\\"realname\\\":\\\"Bolotov, Alexander\\\",\\\"notes\\\":\\\"\\\",\\\"modulus\\\":\\\"952abd321220a9c2850f66c2c51e28eb3c7436b613e0eefcf02d43720e5325279e4aeeb61d309f7375203ea64c6e69e223be9c8784163d013bfddc9dfc4ae7eb6eb7761c9151c73426395873c20d4f60e874e8954fc285f7ae7f8433dfc1d7b191dda1ef6bebb65e1b7e9433467a306db0ede830d99e8ee062577f3079639341136703abcba1e0a4e8b2ba74770c62bda18e47b641de128e57c2fdcde8861ab1ffb588c6dc7c2527512016cd70af25eb24ea98cc9dd39fa6bcbd24e2457462ba75645aa1a8744fb78b8030219b7a15c7519b03a22af0d80493ae03f918535e3a2c185dd1759ae9a251fd1e75c3501bea1d3d67146f1acd4c18644221f12246a7\\\",\\\"not_after\\\":1289029547600,\\\"version\\\":1}\", \"signature\": \"56ff9d4649df3499a3d1a09f858f54f8998ee1286f3584ad99ccf79db6d8f65f6243eb64a51519bc5c221fe0f08f14c7b6172d71eb443641d2b4cc25b7f4d96926f0646733bf7518da2d61ebad7361b84975ce235a4be2700fc693885a462d9da9a8b445e12d32e9a71aeacb6e10d33c772ab2bc7a7548515488c0c851da9512e38b6c2ef3f5464256d53325336d2eec931c066a27aa6103dc7134e9f90571e161ac4540bd49736ebde0f04124c51ed1188c8e5a77575a8a14d34689d204570b88309a3631f37ce32c870eeb61f6ec3f416304f16d60e79ab3ec8533fca161b9cabc3947800f2a57098bb20135b7bd044d984130d57d8703d4aa8f89fcfb3b2d\"}");

		/*String fr = m.createMessage("cs161", "friend_request", "{\"username\": \"cs161\", \"message\": \"{\\\"not_before\\\":1288833587980,\\\"username\\\":\\\"bernardj\\\",\\\"email\\\":\\\"bjjulve@berkeley.edu\\\",\\\"public_exponent\\\":\\\"5\\\",\\\"realname\\\":\\\"Julve, Bernard\\\",\\\"notes\\\":\\\"wassup\\\",\\\"modulus\\\":\\\"6d935a9a6cb3dbc403997afb486574bcde500b10c0254cf4c7245447960e63c89cc40281e2b0a3731b5d8c3fdb4415b7834c2696b192ff552e0c7ac77819a1e402e004d53286d6c517912e6a4794a18501ada71396f57222cea92cae7adb2213f8c7dc00e8c5533de0da1e1236133de0e1f28b57488eb4c63ea2388a287395027e5d8f5b60707fe8dd9fc6579706032897f272d35b9c1d86b48aabac3f66d6c8593d60abd14dfc4a680137b5a12247d7c75d09732223c3a2c2ce9d9c83b38aff095ed74b1a67cf081abdfa357fdb8a70421d926231a4d8ba2307dfb5c831edb092c178f619ab2536ea177309adb271047b23a54cc8a7dead491b2f605f07d75b\\\",\\\"not_after\\\":1288865123980,\\\"version\\\":1}\", \"signature\": \"0463bfcc1d44fd63a8f16e699890f828ba131703888d669dda6156a8e15d275dcb97118a3efc62e6230b0c86be9d00d68a58bab6ec3a1339e9e0666ece3383f33563ac40b3fa5bc1da45998f1c6360496b2050a5f1a36b091c60b22b7f95d81fde34648fca33c0b96ffabd25bedf9e8a0324960a96edea205baa80d0a5bd589208155d15299a7446c6e7dcef4599c88bbd4d9b41ae3a614ac99898ecbfc0f1712154ea4c403de41da23011d03a0f4b58fcbf29f278a53fa55057876f27d3fe6b3b8599a200bc3817d18ff0ef417b2394df45fc25dcac1b9e542614c39f8a6a6273bb605d7ea246cd5fbec48fe0232cfdd6b91e7b56f13705e7714fcff18cabc7\"}");*/


		/*String fr = m.createMessage(username, "friend_request", "{\"username\": \"cs161\", \"message\": \"{\\\"not_before\\\":1287617539583,\\\"username\\\":\\\"chetan51\\\",\\\"email\\\":\\\"chetan.surpur@gmail.com\\\",\\\"public_exponent\\\":\\\"5\\\",\\\"realname\\\":\\\"Surpur, Chetan\\\",\\\"notes\\\":\\\"hello!\\\",\\\"modulus\\\":\\\"ad6beffe545fea39af3cba22c691828c0522b035ced65124d8c280ba6dfe4cfaf8b2e4df6210fb86f76fbb1467acb00072a27eb4233505e84935c9577e594873061fe673f6d983be53ebcb212a60bafa0d5acf38ead0d87e466ebf3837a21e4f14869cb54bf7030957d7c3f994c44b8371d43498d49625c066772a6df73d8631bbadd8b187684e0080ac86c2f6219e74158683e585dc60179ad820594d534bb4bb04b562b362ad60ef4429a201e6bc873a7e691c9b73fc64ceb60d54bcbfcbdaadc5944a427afa315c3478bb42887a2ed72fad38b61d3d6ad959576d385b2c9e0b7ad215bdc1009834987f949f6e2532050c9c6f05b7e1e2f2b1f4edc811f015\\\",\\\"not_after\\\":1287649075583,\\\"version\\\":1}\", \"signature\": \"04f3bfcd3ddf7e450bd00ce549f5d1a629b3448dc7d65161c0471773c54031f128de20040c49942131d64fc7b05206fd40930bb71afdb18a6c5d2e2f895db785dd6970808e65a4b8a869b89deefeb88d8c685d7eccf6510e266aea6f777bb6eaeb7e494baa6bd22582c668d4e432695e5c9bdc4407f6a98d55457367f05cbfc2b31862d5480ca233382febfe9b92f8fde478e524508a966eb765e2a1d4e92ef1454778e813f549524575ab0d4f10670fbdcdd91561733c78bf0a84e960b4cd73bbd414973fafc0e7f697dd94026a852fe6487d1e9a785526a543b1fd8e29eba79b61e3e0b62106c056f24c35b9a073e0e7215ae8a449dc82c6db4e525a079981\"}");*/

		System.out.println("our friend request:");
		System.out.println(fr);
        
	}

    public void testCreateWallPost() throws JSONException {

		String fr = m.createMessage("cs161", "wall_post", "my group mates are awesome");

		System.out.println("our wall post:");
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