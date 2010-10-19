package main.java.CryptoEngine;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;
import org.json.JSONObject;

import main.java.RSAEngine.Crypter;

public class Signature {
	
	MessageDigest SHA256;
	Crypter a;
	
	public Signature() throws NoSuchAlgorithmException{
		SHA256 = MessageDigest.getInstance("SHA-256");
		a = new Crypter();
	}
	
	public String generateSignature(String username, String message, BigInteger d, BigInteger n) throws JSONException{
		byte[] h = SHA256.digest(message.getBytes());
		BigInteger hash = new BigInteger(h);
		BigInteger cypherhash = a.RSAEncryptPrimitive(hash, d, n);
		JSONObject signature = new JSONObject();
		signature.put("username", username);
		signature.put("message", message);
		signature.put("signature", cypherhash);
		return signature.toString();
	}
	
	public boolean verifySignature(String signature, BigInteger e, BigInteger n) throws JSONException{
		JSONObject sig = new JSONObject(signature);
		String message = sig.getString("message");
		byte[] h = SHA256.digest(message.getBytes());
		BigInteger hash = new BigInteger(h);
		String cypherhash = (String) sig.getString("signature");
		BigInteger c = new BigInteger(cypherhash);
		BigInteger plainsig = a.RSADecryptPrimitive(c, e, n);
		if(hash.compareTo(BigInteger.ZERO) < 0){
			return plainsig.subtract(n).equals(hash);
		}
		return plainsig.equals(hash);
	}
}
