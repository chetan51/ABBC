package main.java.RSAEngine;

import java.math.BigInteger;

public class Crypter {
	public BigInteger EncryptInteger(BigInteger m, BigInteger e, BigInteger n) {
		return m.modPow(e, n);
	}

	public BigInteger DecryptInteger(BigInteger c, BigInteger d, BigInteger n) {
		return c.modPow(d, n);
	}
	
	public BigInteger OS2IP(byte[]X){
		//OS2IP converts an octet string to a nonnegative integer.
		
		BigInteger out = new BigInteger("0");
		BigInteger twofiftysix = new BigInteger("256");
		
		for(int i = 1; i <= X.length; i++){
			out = out.add((BigInteger.valueOf(X[X.length - 1])).multiply(twofiftysix.pow(X.length-i)));
		}
		
		return out;
	}

	public String I2OSP(BigInteger X, int XLen){
		//I2OSP converts a nonnegative integer to an octet string of a specified length.
		
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
			out[XLen - 1] = cur[0].byteValue();
		}
		String rv = new String(out);
		
		return rv;
	}
	
	public String EncryptString(String plaintext, BigInteger e, BigInteger n) {
		
		int K = n.bitLength()/8;
		int extrabit = n.bitLength()%8;
		if (extrabit > 0){
			K = K + 1;
		}
		
		//LENGTH CHECKING

		if (plaintext.length() > (K - 11)){
			System.out.println("message too long");
			return "message too long";
		}
		
		//EME-PKCS1-v1_5 ENCODING
		
		byte[] PS = new byte[K - plaintext.length() - 3];
		for(int i = 0; i < PS.length; i++){
			PS[i] = (byte) ((Math.random())*254+1);
		}
		byte[] EM = new byte[K];
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
		
		//RSA ENCRYPTION
		
		BigInteger littlem = OS2IP(EM);
		BigInteger littlec = EncryptInteger(littlem, e, n);
		
		return I2OSP(littlec, K);
	}
	
	public String DecryptString(String cyphertext, BigInteger d, BigInteger n) {
		
		int K = n.bitLength()/8;
		int extrabit = n.bitLength()%8;
		if (extrabit > 0){
			K = K + 1;
		}
		
		//LENGTH CHECKING
		
		if ((cyphertext.length() != K) || (K < 11)){
			System.out.println("decryption error");
			return "decryption error";
		}
		
		//RSA DECRYPTION
		
		BigInteger c = OS2IP(cyphertext.getBytes());
		BigInteger m = DecryptInteger(c, d, n); //Spec says error output by DecryptInteger is possible?
		String EM = I2OSP(m, K);
		
		//EME-PKCS1-v1_5 DECODING
		if(((byte)(EM.charAt(0)) != 0) || ((byte)(EM.charAt(1)) != 2) || (EM.indexOf(0, 1) == -1)){
			System.out.println("decryption error");
			return "decryption error";
		}
		else if(EM.substring(3, EM.indexOf(0, 1)).length() < 8){
			System.out.println("decryption error");
			return "decryption error";
		}

		return EM.substring(EM.indexOf(0, 1));
	}

}