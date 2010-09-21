package main.java.RSAEngine;

import java.math.BigInteger;

public class Crypter {
	public BigInteger EncryptInteger(BigInteger m, BigInteger e, BigInteger n) {
		return m.modPow(e, n);
	}

	public BigInteger DecryptInteger(BigInteger c, BigInteger d, BigInteger n) {
		return c.modPow(d, n);
	}
}