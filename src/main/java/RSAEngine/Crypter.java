package main.java.RSAEngine;

import java.math.BigInteger;

public class Crypter {
	/* Primitive RSA Encryption and Decryption
	 *************************************************************************/
	
	public BigInteger RSAEncryptPrimitive(BigInteger m, BigInteger e, BigInteger n) {
		return m.modPow(e, n);
	}

	public BigInteger RSADecryptPrimitive(BigInteger c, BigInteger d, BigInteger n) {
		return c.modPow(d, n);
	}
	
	/* Utility functions
	 *************************************************************************/
	
	public BigInteger OS2IP(byte[]X){
		//OS2IP converts an octet string to a nonnegative integer.
		//See RSA section 4.2
		
		BigInteger out = new BigInteger("0");
		BigInteger twofiftysix = new BigInteger("256");
		
		for(int i = 1; i <= X.length; i++){
			out = out.add((BigInteger.valueOf(X[X.length - i])).multiply(twofiftysix.pow(X.length-i)));
		}
		//x = x(xLen–1)^256xLen–1 + x(xLen–2)^256xLen–2 + … + x(1)^256 + x0
		
		return out;
	}

	public String I2OSP(BigInteger X, int XLen){
		//I2OSP converts a nonnegative integer to an octet string of a specified length.
		//See RSA section 4.1
		
		BigInteger twofiftysix = new BigInteger("256");
		byte[] out = new byte[XLen];
		BigInteger[] cur;
		
		if(X.compareTo(twofiftysix.pow(XLen)) >= 0){
			System.out.println("integer too large");
			return "integer too large";
		}
		for(int i = 1; i <= XLen; i++){
			cur = X.divideAndRemainder(twofiftysix.pow(XLen-i));
			X = cur[1];
			out[XLen - i] = cur[0].byteValue();
		}
		//basically the inverse of the above
		//Cur is an array of two bigints, with cur[0]=X/256^(XLen-i) and cur[1]=X/256^[XLen-i]
		
		String rv = new String(out);
		
		return rv;
	}

	public int OctetLengthOfN(BigInteger n) {
		int K = n.bitLength()/8;
		int extrabit = n.bitLength()%8; //Set length K as defined in RSA 7.2.1
		if (extrabit > 0){
			K = K + 1;
		}

		return K;
	}

	/* RSAES-PKCS1-V1_5 Encryption Scheme
	 *************************************************************************/
	
	public boolean CheckEncryptionLength(String plaintext, BigInteger n) {
		int K = OctetLengthOfN(n);
		
		if (plaintext.length() > (K - 11)){
			return false;
		}
		else {
			return true;
		}
	}
	
	public byte[] EMEPKCS1Encode(String plaintext, BigInteger n) {
		//EME-PKCS1-v1_5 ENCODING
		int K = OctetLengthOfN(n);

		byte[] PS = new byte[K - plaintext.length() - 3]; //Set up random padding
		for(int i = 0; i < PS.length; i++){
			PS[i] = (byte) ((Math.random())*254+1);
		}
		byte[] EM = new byte[K]; //Splice EM together
		EM[0] = 0;
		EM[1] = 2;
		for(int i = 0; i < PS.length; i++){
			EM[2+i] = PS[i];
		}
		EM[2+PS.length] = 0;
		byte[] m = plaintext.getBytes();
		for(int i = 0; i < m.length; i++){
			EM[3+PS.length+i] = m[i];
		}
		
		return EM;
	}

	public String RSAEncrypt(byte[] EM, BigInteger e, BigInteger n) {
		//RSA ENCRYPTION
		
		int K = OctetLengthOfN(n);

		BigInteger littlem = OS2IP(EM);
		BigInteger littlec = RSAEncryptPrimitive(littlem, e, n);
		
		return I2OSP(littlec, K);
	}

	public String RSAESPKCS1Encrypt(String plaintext, BigInteger e, BigInteger n) {
		//See RSA 7.2.1
		
		if (!CheckEncryptionLength(plaintext, n)) {
			return "message too long";
		}

		byte[] EM = EMEPKCS1Encode(plaintext, n);
		
		return RSAEncrypt(EM, e, n);
	}

	/* RSAES-PKCS1-V1_5 Decryption Scheme
	 *************************************************************************/
	
	public boolean CheckDecryptionLength(String ciphertext, BigInteger n) {
		//LENGTH CHECKING
		int K = OctetLengthOfN(n);

		if ((ciphertext.length() != K) || (K < 11)){
			return false;
		}
		else {
			return true;
		}
	}
	
	public String RSADecrypt(String ciphertext, BigInteger d, BigInteger n) {
		//RSA DECRYPTION
		
		int K = OctetLengthOfN(n);
		
		BigInteger c = OS2IP(ciphertext.getBytes());
		BigInteger m = RSADecryptPrimitive(c, d, n); //Spec says error output by RSADecryptPrimitive is possible?
		String EM = I2OSP(m, K);

		return EM;
	}

	public String EMEPKCS1Decode(String EM) {
		//EME-PKCS1-v1_5 DECODING
		if(((byte)(EM.charAt(0)) != 0) || ((byte)(EM.charAt(1)) != 2) || (EM.indexOf(0, 1) == -1)){ //Check that whatever we got is valid
			return "decryption error";
		}
		else if(EM.substring(3, EM.indexOf(0, 1)).length() < 8){
			return "decryption error";
		}

		return EM.substring(EM.indexOf(0, 1)); //throw away padding
	}

	public String DecryptString(String ciphertext, BigInteger d, BigInteger n) {
		//See RSA 7.2.2
		
		if (!CheckDecryptionLength(ciphertext, n)) {
			return "decryption error";
		}
	
		String EM = RSADecrypt(ciphertext, d, n);
		
		return EMEPKCS1Decode(EM);
	}

}