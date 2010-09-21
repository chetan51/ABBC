package test.java.RSAEngine;

import junit.framework.TestCase;
import java.math.BigInteger;
import main.java.RSAEngine.*;

public class TestCrypter extends TestCase {

    public void testEncryptInteger_with_m_5_e_13_n_667()
    {
		Crypter c = new Crypter();
        assertEquals(c.EncryptInteger(BigInteger.valueOf(5), BigInteger.valueOf(13), BigInteger.valueOf(667)), BigInteger.valueOf(412));
    }
    
	public void testEncryptInteger_with_m_7_e_13_n_667()
    {
		Crypter c = new Crypter();
        assertEquals(c.EncryptInteger(BigInteger.valueOf(7), BigInteger.valueOf(13), BigInteger.valueOf(667)), BigInteger.valueOf(112));
    }
    
	public void testDecryptInteger_with_c_412_d_237_n_667()
    {
		Crypter c = new Crypter();
        assertEquals(c.DecryptInteger(BigInteger.valueOf(412), BigInteger.valueOf(237), BigInteger.valueOf(667)), BigInteger.valueOf(5));
    }
    
	public void testDecryptInteger_with_m_112_d_237_n_667()
    {
		Crypter c = new Crypter();
        assertEquals(c.DecryptInteger(BigInteger.valueOf(112), BigInteger.valueOf(237), BigInteger.valueOf(667)), BigInteger.valueOf(7));
    }
}