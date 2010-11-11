package main.java.Client;

import main.java.RSAEngine.Crypter;
import main.java.CryptoEngine.Signature;

import java.math.BigInteger;

import java.security.NoSuchAlgorithmException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;

import javax.crypto.*;
import javax.crypto.spec.*;

import org.json.JSONObject;
import org.json.JSONException;

public class MessageController {

    public static boolean processMessage(JSONObject message) throws NoSuchAlgorithmException {
 		/* Setup 
		/* A message is invalid if:
		 * 1) it is not intended for you
		 * 2) invalid AES key
		 * 3) for wall posts, the sender is not in your list of friends
		 *    (ie check the signature in the message)
		 * 4) invalid message signature (must decrypt the aes key first)
		 * 5) unrecognized command
		*/
		try {
			// Check 1
			if (message.get("to_user") != DataController.getUsername()){
				return false;
			}
		
			// Setup parts for decryption
			Crypter c = new Crypter();
			JSONObject cert = new JSONObject(DataController.getCertificate());
			BigInteger n = new BigInteger((String) cert.get("modulus"), 16);
			BigInteger d = DataController.getPrivateKey();

			String eak_string = (String) message.get("encrypted_aes_key");
			String eak = new String(new BigInteger(eak_string, 16).toByteArray());
		
			// Decrypt the aes key
			String aes_key_string = c.RSADecrypt(eak, d, n);
			BigInteger aes_key_int = new BigInteger(aes_key_string, 16);
		
			// Check 2
			if (aes_key_int.bitLength() != 128) {
				return false;
			}
		
			// Set up AES decryption
			Cipher aesCipher;
			try {
				aesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			} catch (NoSuchAlgorithmException e) {
				return false;
			} catch (NoSuchPaddingException e) {
				return false;
			}
			SecretKeySpec aesKeyObj = new SecretKeySpec(aes_key_int.toByteArray(), "AES");
			
			try {
				aesCipher.init(aesCipher.DECRYPT_MODE, aesKeyObj);
			} catch (InvalidKeyException e) {
				return false;
			}
		
			// Decrypt the payload
			byte[] encrypted_signature = new BigInteger((String) message.get("encrypted_signature"), 16).toByteArray();
			String raw_signature;
			
			try {
				raw_signature = new String(aesCipher.doFinal(encrypted_signature));
			} catch (IllegalBlockSizeException e) {
				return false;
			} catch (BadPaddingException e) {
				return false;
			}
		
			// Check 3
			JSONObject signature = new JSONObject(raw_signature);
			if (!DataController.isFriend((String) signature.get("username"))) {
				return false;
			}
		
			// Grab the message
			JSONObject payload = new JSONObject(signature.get("message"));
			String commandType = (String) payload.get("command_type");
			
			// Process the signature's message:
			if (commandType == "wall_post") {
				DataController.addWallPost((String) signature.get("username"), (String) payload.get("command_args"));
				return true;
			} else if (commandType == "friend_request") {
				// Setup up signature verification
				JSONObject friend_signed_cert = new JSONObject(payload.get("command_args"));
				JSONObject friend_unsigned_cert = new JSONObject(friend_signed_cert.get("message"));
				Signature s = new Signature();
		
				// Check 4
				boolean isVerified = s.verifySignature((String) payload.get("command_args"), new BigInteger((String) friend_unsigned_cert.get("public_exponent"), 16), new BigInteger((String) friend_unsigned_cert.get("modulus"), 16));
				if (!isVerified) {
					return false;
				} else {
					DataController.addFriend((String) friend_unsigned_cert.get("realname"), (String) friend_unsigned_cert.get("username"), friend_unsigned_cert);
					return true;
				}
			} else {
				// Check 5
				return false;
			}
		} catch (JSONException e) {
			return false;
		}
    }

	public static String createMessage(String recipient, String cmd, String cmdargs){

		Crypter a = new Crypter();					//Init crypter
		
		if(!DataController.isFriend(recipient)){
			System.out.println(recipient + " IS NOT A FRIEND");		//check if user is friend
			return null;
		}
		
		JSONObject sigmsg = new JSONObject();		//build signature's message
		try {
			sigmsg.put("command_type", cmd);
			sigmsg.put("command_args", cmdargs);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return null;
		}
		JSONObject message = new JSONObject();
		try {
			Signature s = new Signature();
			JSONObject MyCert = new JSONObject(DataController.getCertificate());
			BigInteger[] key = new BigInteger[3];
			key[0] = DataController.getPrivateKey();
			key[1] = new BigInteger((String)MyCert.get("modulus"), 16);
			key[2] = new BigInteger((String)MyCert.get("public_exponent"), 16);
			String signature = s.generateSignature(DataController.getUsername(), sigmsg.toString(), key[0], key[1]);			//build signature
		
			KeyGenerator kgen = KeyGenerator.getInstance("AES");				//AES init, session key generation
			kgen.init(128);
			SecretKey skey = kgen.generateKey();
		    byte[] raw = skey.getEncoded();
		    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
	    
		    byte[] encsig = cipher.doFinal(signature.getBytes());				//encrypt sig with AES
	    
		    JSONObject recipientCert = new JSONObject(DataController.getCertificate(recipient).toString());
		    BigInteger hise = new BigInteger(recipientCert.getString("public_exponent"), 16);
	    	BigInteger hisn = new BigInteger(recipientCert.getString("modulus"), 16);
	    	String keystr = new String(skeySpec.getEncoded());
	    	String enckeystr = a.RSAESPKCS1Encrypt(keystr, hise, hisn);			//encrypt session key
	    
	    	message = new JSONObject();								//build message
	    	message.put("to_user", recipient);
	    	message.put("encrypted_aes_key", new BigInteger(1, enckeystr.getBytes()).toString(16));
	    	message.put("encrypted_signature", new BigInteger(1, encsig).toString(16));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
        }
		return message.toString();
	}
	
    /*
     * Used the method below for sending friend request to cs161
     */

/*public static String createFriendReq(String cmd, String cmdargs){

        Crypter a = new Crypter();                    //Init crypter
        
        JSONObject sigmsg = new JSONObject();        //build signature's message
        try {
            sigmsg.put("command_type", cmd);
            sigmsg.put("command_args", cmdargs);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            return null;
        }
        JSONObject message = new JSONObject();
        try {
            Signature s = new Signature();
            //JSONObject MyCert = new JSONObject(DataController.getCertificate());
            BigInteger[] key = new BigInteger[2];
            key[0] = new BigInteger("22d47daad9c59f6f11dbab49bf6934d14be065dbdee2522e491a4b9141f317f62d3a07eeda008304e53ccfe3e299eb595c57baf2cbd3c11c9c83a5864ac97fed0f2d896715f64821c4bd5c56132fb0a8e04d90e0c918f51b7c3ac00bb6d81f92f0c7ca509c97a65d08572f9e9bfbef746d2c5c511824664c5603effb3c3d875b1f7d20fbf814e434a9dceb6e38f65f1a71a50216a7632323119971ea1b37f9ae690ef5dcdc25d8ff5fffb8c1cd6d9c340c9b7396b7b77b08b6ee4f6df0fbfd45ba8efdd2a3f7687ef068a1a029644c1f42d3f0bf02c5be5e11eaeef94d4e7e8e668270f8836b8a13548db75d6be8d459e5ae999da05d8ba48fec6c9c3b394a95", 16);
            key[1] = new BigInteger("57133a2b206e0e95aca52c385e87040b3db0fea5ad35cd73b6c1bceb24dfbbe7711113d52101478c3d1807b9b680cc5f66db535efd9162c787491dcfbaf7bfd0a5f1d781b6e7b4546bd966d72ff739a630c1ea31f6be64c4b692e01d491c4eef59f379c9877b1fe894d9f70c85f5d6a310eee6cabc5affbed709d7f41699d264fbb28b1e4a835720243c4b7e9b493c83f0119a6fa2f493d71233df1aeec0cae1011dcd758d9a6b900cdcd15aca684776ab31b6909a93c69a1ca68cd71a983c8504b0fffaaebf36cfb9e827508640fbad638a029b7b8cc3f5135d723f695233b784e6bcc42525461ece7b9fc8ee9feacca4d32fbc3f5c0698311ae97cac445071", 16);
            String signature = s.generateSignature("bchen311", sigmsg.toString(), key[0], key[1]);            //build signature
        
            KeyGenerator kgen = KeyGenerator.getInstance("AES");                //AES init, session key generation
            kgen.init(128);
            SecretKey skey = kgen.generateKey();
            byte[] raw = skey.getEncoded();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        
            byte[] encsig = cipher.doFinal(signature.getBytes());                //encrypt sig with AES
        
            //JSONObject recipientCert = new JSONObject(DataController.getCertificate(recipient).toString());
            BigInteger hise = new BigInteger("5", 16);
            BigInteger hisn = new BigInteger("969a193f7f95592941653b50e55deea362254d71ceac43466219fe20abfebe6313b909eed1d3665b87e7223cc6a2596b23f5dd1e91dac4fcd7388437421eac483f6f468b11050a1b4ad70ab7ca918ada85735caf180b5d50faa1f8be14f3e720452749f067d6db30a45480f818e2b158d7e4d36baa98ab5af1f4e235a48456dde9fe5396ddc3f1aadc2de9b652e7e477f4e3ba11d75997eb06f82ddb5709f8a780a20357878321ac3ef11c2774592a5c6845df3ae4fe622427b6d6ac420e9630afa44aef9be96e7b25596b8bb707d72bc8f2a321868e7f26edeacd7228efe4ed9b3688c888638f9d3a5b36d96012296ea9bbba5eb80b436c5ce190324cb53c2d", 16);
            String keystr = new String(skeySpec.getEncoded());
            String enckeystr = a.RSAESPKCS1Encrypt(keystr, hise, hisn);            //encrypt session key
        
            message = new JSONObject();                                //build message
            message.put("to_user", "cs161");
            message.put("encrypted_aes_key", new BigInteger(1, enckeystr.getBytes()).toString(16));
            message.put("encrypted_signature", new BigInteger(1, encsig).toString(16));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return message.toString();
    }*/

}