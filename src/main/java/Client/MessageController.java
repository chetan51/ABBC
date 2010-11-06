package main.java.Client;

import java.math.BigInteger;
import org.JSON.JSONObject;
import org.JSON.JSONException;

public class MessageController {

    boolean processMessage(JSONObject message) {

    }

	public static String createMessage(String recipient, String cmd, String cmdargs){
		Crypter a = new Crypter();					//Init crypter
		
		if(!DataController.isFriend(recipient)){
			System.out.println(recipient + " IS NOT A FRIEND");		//check if user is friend
			return null;
		}
		
		JSONObject sigmsg = new JSONObject();		//build signature's massage
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
			BigInteger[] key = DataController.getPrivateKey();
			String signature = s.generateSignature("bolotov", sigmsg.toString(), key[0], key[1]);			//build signature
		
			KeyGenerator kgen = KeyGenerator.getInstance("AES");				//AES init, session key generation
			kgen.init(256);
			SecretKey skey = kgen.generateKey();
		    byte[] raw = skey.getEncoded();
		    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		    Cipher cipher = Cipher.getInstance("AES");
		    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
	    
		    byte[] encsig = cipher.doFinal(signature.getBytes());				//encrypt sig with AES
	    
		    JSONObject recipientCert = new JSONObject(DataController.getCertificate(recipient));
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
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message.toString();
	}

}