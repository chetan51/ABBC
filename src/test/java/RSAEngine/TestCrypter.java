package test.java.RSAEngine;

import junit.framework.TestCase;
import java.math.BigInteger;
import main.java.RSAEngine.*;

public class TestCrypter extends TestCase {

    public void testRSAEncryptPrimitive_with_m_5_e_13_n_667()
    {
		Crypter c = new Crypter();
        assertEquals(c.RSAEncryptPrimitive(BigInteger.valueOf(5), BigInteger.valueOf(13), BigInteger.valueOf(667)), BigInteger.valueOf(412));
    }
    
	public void testRSAEncryptPrimitive_with_m_7_e_13_n_667()
    {
		Crypter c = new Crypter();
        assertEquals(c.RSAEncryptPrimitive(BigInteger.valueOf(7), BigInteger.valueOf(13), BigInteger.valueOf(667)), BigInteger.valueOf(112));
    }
    
	public void testRSADecryptPrimitive_with_c_412_d_237_n_667()
    {
		Crypter c = new Crypter();
        assertEquals(c.RSADecryptPrimitive(BigInteger.valueOf(412), BigInteger.valueOf(237), BigInteger.valueOf(667)), BigInteger.valueOf(5));
    }
    
	public void testRSADecryptPrimitive_with_m_112_d_237_n_667()
    {
		Crypter c = new Crypter();
        assertEquals(c.RSADecryptPrimitive(BigInteger.valueOf(112), BigInteger.valueOf(237), BigInteger.valueOf(667)), BigInteger.valueOf(7));
    }

    public void testI2OSP_with_6382179_length_3() {
        System.out.println(new String("abc"));
		Crypter c = new Crypter();
        System.out.println(new String(c.I2OSP(BigInteger.valueOf(6382179), 3)));
        assertEquals(new String(c.I2OSP(BigInteger.valueOf(6382179), 3)), new String("abc"));
    }

    public void testI2OSP_with_6382179_length_4() {
        System.out.println(new String("abc"));
		Crypter c = new Crypter();
        System.out.println(new String(c.I2OSP(BigInteger.valueOf(6382179), 4)));
        assertEquals(new String(c.I2OSP(BigInteger.valueOf(6382179), 4)), new String((char)(0) + "abc"));
    }

    public void testI2OSPandOS2IP_with_97() {
		Crypter c = new Crypter();
        assertEquals(c.OS2IP(c.I2OSP(BigInteger.valueOf(97), 1)), BigInteger.valueOf(97));
    }

    public void testOS2IP_with_large_string() {
		String testString = new String(new BigInteger("1A6820F8546A1F114727D6151B58AD87D77E49C0AABC9B779F30285B65E590E42FC3F2A5A9A03A5A07AC1141FE3E6F2C1E78AAE9ECBCB1527FAA273BFDB12679D534446F457781E55754C837945926B7418FD2502D2AB4F96E317A1212741A0F6D7886279BE27B73492DB9BEEBEFEB4BC01C1EFDCC5A8BD8B19A36008A4FF338", 16).toByteArray());

		BigInteger testInt = new BigInteger("1A6820F8546A1F114727D6151B58AD87D77E49C0AABC9B779F30285B65E590E42FC3F2A5A9A03A5A07AC1141FE3E6F2C1E78AAE9ECBCB1527FAA273BFDB12679D534446F457781E55754C837945926B7418FD2502D2AB4F96E317A1212741A0F6D7886279BE27B73492DB9BEEBEFEB4BC01C1EFDCC5A8BD8B19A36008A4FF338", 16);

		Crypter c = new Crypter();

        assertEquals(c.OS2IP(testString.getBytes()), testInt);
    }

	public void testRSAESPKCS1EncryptAndDecrypt() {
		// Set up n, e and the plaintext string
		BigInteger n = new BigInteger("A9E167983F39D55FF2A093415EA6798985C8355D9A915BFB1D01DA197026170FBDA522D035856D7A986614415CCFB7B7083B09C991B81969376DF9651E7BD9A93324A37F3BBBAF460186363432CB07035952FC858B3104B8CC18081448E64F1CFB5D60C4E05C1F53D37F53D86901F105F87A70D1BE83C65F38CF1C2CAA6AA7EB", 16);

		BigInteger e = BigInteger.valueOf(65537);

		BigInteger d = new BigInteger("67CD484C9A0D8F98C21B65FF22839C6DF0A6061DBCEDA7038894F21C6B0F8B35DE0E827830CBE7BA6A56AD77C6EB517970790AA0F4FE45E0A9B2F419DA8798D6308474E4FC596CC1C677DCA991D07C30A0A2C5085E217143FC0D073DF0FA6D149E4E63F01758791C4B981C3D3DB01BDFFA253BA3C02C9805F61009D887DB0319", 16);

		String plaintext = new String(new BigInteger("4E636AF98E40F3ADCFCCB698F4E80B9F", 16).toByteArray());
		
		// Test the string encryption
		Crypter c = new Crypter();

        assertEquals(c.RSAESPKCS1Decrypt(c.RSAESPKCS1Encrypt(plaintext, e, n), d, n), plaintext);
	}
	
	public void testRSAESPKCS1Decrypt() {
		// Set up n, d and the ciphertext string
		BigInteger n = new BigInteger("A9E167983F39D55FF2A093415EA6798985C8355D9A915BFB1D01DA197026170FBDA522D035856D7A986614415CCFB7B7083B09C991B81969376DF9651E7BD9A93324A37F3BBBAF460186363432CB07035952FC858B3104B8CC18081448E64F1CFB5D60C4E05C1F53D37F53D86901F105F87A70D1BE83C65F38CF1C2CAA6AA7EB", 16);
		BigInteger d = new BigInteger("67CD484C9A0D8F98C21B65FF22839C6DF0A6061DBCEDA7038894F21C6B0F8B35DE0E827830CBE7BA6A56AD77C6EB517970790AA0F4FE45E0A9B2F419DA8798D6308474E4FC596CC1C677DCA991D07C30A0A2C5085E217143FC0D073DF0FA6D149E4E63F01758791C4B981C3D3DB01BDFFA253BA3C02C9805F61009D887DB0319", 16);

		String ciphertext = new String(new BigInteger("1A6820F8546A1F114727D6151B58AD87D77E49C0AABC9B779F30285B65E590E42FC3F2A5A9A03A5A07AC1141FE3E6F2C1E78AAE9ECBCB1527FAA273BFDB12679D534446F457781E55754C837945926B7418FD2502D2AB4F96E317A1212741A0F6D7886279BE27B73492DB9BEEBEFEB4BC01C1EFDCC5A8BD8B19A36008A4FF338", 16).toByteArray());
		
		// Test the string decryption
		Crypter c = new Crypter();
        
		BigInteger cipherInt = new BigInteger("1A6820F8546A1F114727D6151B58AD87D77E49C0AABC9B779F30285B65E590E42FC3F2A5A9A03A5A07AC1141FE3E6F2C1E78AAE9ECBCB1527FAA273BFDB12679D534446F457781E55754C837945926B7418FD2502D2AB4F96E317A1212741A0F6D7886279BE27B73492DB9BEEBEFEB4BC01C1EFDCC5A8BD8B19A36008A4FF338", 16);

        System.out.println("----------");
        System.out.println(cipherInt.equals(c.OS2IP(ciphertext.getBytes())));
        System.out.println("----------");

        BigInteger m = c.RSADecryptPrimitive(c.OS2IP(ciphertext.getBytes()), d, n);

        System.out.println("----------");
        System.out.println(m);
        System.out.println("----------");

		int K = c.OctetLengthOfN(n);
		String EM = new String(c.I2OSP(m, K));
		String decodedEM = c.EMEPKCS1Decode(EM);

		assertEquals(c.RSAESPKCS1Decrypt(ciphertext, d, n), "Yeah, you got it. :)");
	}

	public void testEMEPKCS1Decode() {
        String EM = new String(new BigInteger("0002257F48FD1F1793B7E5E02306F2D3228F5C95ADF5F31566729F132AA12009E3FC9B2B475CD6944EF191E3F59545E671E474B555799FE3756099F044964038B16B2148E9A2F9C6F44BB5C52E3C6C8061CF694145FAFDB24402AD1819EACEDF4A36C6E4D2CD8FC1D62E5A1268F496004E636AF98E40F3ADCFCCB698F4E80B9F", 16).toByteArray());
    

        System.out.println("////");
        System.out.println((byte)EM.charAt(2));
        System.out.println("////");

		Crypter c = new Crypter();
        c.EMEPKCS1Decode(EM);
    }
}