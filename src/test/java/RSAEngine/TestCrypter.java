package test.java.RSAEngine;

import junit.framework.TestCase;
import java.math.BigInteger;
import main.java.RSAEngine.*;

public class TestCrypter extends TestCase {

    private Crypter c;

    private BigInteger n;
    private BigInteger d;
    private BigInteger e;

    private String plaintext;
    private String ciphertext;

    private BigInteger ciphertext_int;
    private BigInteger plaintext_int;

    private byte[] EM;

    private String mystery;

    public void setUp() {
        c = new Crypter();

		n = new BigInteger("A9E167983F39D55FF2A093415EA6798985C8355D9A915BFB1D01DA197026170FBDA522D035856D7A986614415CCFB7B7083B09C991B81969376DF9651E7BD9A93324A37F3BBBAF460186363432CB07035952FC858B3104B8CC18081448E64F1CFB5D60C4E05C1F53D37F53D86901F105F87A70D1BE83C65F38CF1C2CAA6AA7EB", 16);
		d = new BigInteger("67CD484C9A0D8F98C21B65FF22839C6DF0A6061DBCEDA7038894F21C6B0F8B35DE0E827830CBE7BA6A56AD77C6EB517970790AA0F4FE45E0A9B2F419DA8798D6308474E4FC596CC1C677DCA991D07C30A0A2C5085E217143FC0D073DF0FA6D149E4E63F01758791C4B981C3D3DB01BDFFA253BA3C02C9805F61009D887DB0319", 16);
		e = BigInteger.valueOf(65537);

		String plaintext_string = "4E636AF98E40F3ADCFCCB698F4E80B9F";
		plaintext = new String(new BigInteger(plaintext_string, 16).toByteArray());
        plaintext_int = new BigInteger(plaintext_string, 16);

        String ciphertext_string = "3D2AB25B1EB667A40F504CC4D778EC399A899C8790EDECEF062CD739492C9CE58B92B9ECF32AF4AAC7A61EAEC346449891F49A722378E008EFF0B0A8DBC6E621EDC90CEC64CF34C640F5B36C48EE9322808AF8F4A0212B28715C76F3CB99AC7E609787ADCE055839829E0142C44B676D218111FFE69F9D41424E177CBA3A435B";
		ciphertext = new String(new BigInteger(ciphertext_string, 16).toByteArray());
		ciphertext_int = new BigInteger(ciphertext_string, 16);

        EM = new BigInteger("0002257F48FD1F1793B7E5E02306F2D3228F5C95ADF5F31566729F132AA12009E3FC9B2B475CD6944EF191E3F59545E671E474B555799FE3756099F044964038B16B2148E9A2F9C6F44BB5C52E3C6C8061CF694145FAFDB24402AD1819EACEDF4A36C6E4D2CD8FC1D62E5A1268F496004E636AF98E40F3ADCFCCB698F4E80B9F", 16).toByteArray();

		mystery = new String(new BigInteger("1A6820F8546A1F114727D6151B58AD87D77E49C0AABC9B779F30285B65E590E42FC3F2A5A9A03A5A07AC1141FE3E6F2C1E78AAE9ECBCB1527FAA273BFDB12679D534446F457781E55754C837945926B7418FD2502D2AB4F96E317A1212741A0F6D7886279BE27B73492DB9BEEBEFEB4BC01C1EFDCC5A8BD8B19A36008A4FF338", 16).toByteArray());
    }

    public void testRSAEncryptPrimitive_with_m_5_e_13_n_667()
    {
        assertEquals(c.RSAEncryptPrimitive(BigInteger.valueOf(5), BigInteger.valueOf(13), BigInteger.valueOf(667)), BigInteger.valueOf(412));
    }
    
	public void testRSAEncryptPrimitive_with_m_7_e_13_n_667()
    {
        assertEquals(c.RSAEncryptPrimitive(BigInteger.valueOf(7), BigInteger.valueOf(13), BigInteger.valueOf(667)), BigInteger.valueOf(112));
    }
    
    public void testRSAEncryptPrimitive() {
        assertEquals(c.RSAEncryptPrimitive(new BigInteger(EM), e, n), ciphertext_int);
    }

	public void testRSADecryptPrimitive_with_c_412_d_237_n_667()
    {
        assertEquals(c.RSADecryptPrimitive(BigInteger.valueOf(412), BigInteger.valueOf(237), BigInteger.valueOf(667)), BigInteger.valueOf(5));
    }
    
	public void testRSADecryptPrimitive_with_c_112_d_237_n_667()
    {
        assertEquals(c.RSADecryptPrimitive(BigInteger.valueOf(112), BigInteger.valueOf(237), BigInteger.valueOf(667)), BigInteger.valueOf(7));
    }

    public void testRSADecryptPrimitive() {
        assertEquals(c.RSADecryptPrimitive(ciphertext_int, d, n), new BigInteger(EM));
    }

    public void testOctetLengthOfN() {
        assertEquals(c.OctetLengthOfN(n), 128);
    }

    public void testI2OSP_with_6382179_length_3() {
        assertEquals(new String(c.I2OSP(BigInteger.valueOf(6382179), 3)), new String("abc"));
    }

    public void testI2OSP_with_6382179_length_4() {
        assertEquals(new String(c.I2OSP(BigInteger.valueOf(6382179), 4)), new String((char)(0) + "abc"));
    }

    public void testOS2IP_with_plaintext() {
        assertEquals(c.OS2IP(plaintext.getBytes()), plaintext_int);
    }

    public void testOS2IP_with_ciphertext() {
        assertEquals(c.OS2IP(ciphertext.getBytes()), ciphertext_int);
    }

    public void testI2OSPandOS2IP_with_97() {
        assertEquals(c.OS2IP(c.I2OSP(BigInteger.valueOf(97), 1)), BigInteger.valueOf(97));
    }

    public void testI2OSPandOS2IP_with_ciphertext() {
        assertEquals(c.OS2IP(c.I2OSP(ciphertext_int, 128)), ciphertext_int);
    }

    public void testI2OSPandOS2IP_with_EM() {
        assertEquals(c.OS2IP(c.I2OSP(new BigInteger(EM), 128)), new BigInteger(EM));
    }

    public void testEMEPKCS1Encode() {
        byte[] encoded = c.EMEPKCS1Encode(plaintext, n);
        assertEquals(encoded.length, 128);
        assertEquals(encoded[0], 0);
        assertEquals(encoded[1], 2);
        
        boolean valid = false;

        // Make sure that first 0 bit after 2nd bit is at position 111
        for (int i = 2; i < encoded.length; i++) {
            if (encoded[i] == 0) {
                valid = true;

                assertEquals(i, 111);

                byte[] resultPlaintextArray = new byte[encoded.length - (i + 1)];
                System.arraycopy(encoded, i + 1, resultPlaintextArray, 0, encoded.length - (i + 1));
                assertEquals(new String(resultPlaintextArray), plaintext);
            }
        }

        assertEquals(valid, true);
    }

    public void testEMEPKCS1Decode() {
        assertEquals(c.EMEPKCS1Decode((char)0 + new String(EM)), plaintext); // putting EM into a String seems to be stripping off leading 0s
    }

    public void testRSAEncrypt() {
        assertEquals(c.RSAEncrypt(EM, e, n), ciphertext);
    }

    public void testRSADecrypt() {
        assertEquals(c.RSADecrypt(ciphertext, d, n), (char)0 + new String(EM));
    }

    public void testRSAESPKCS1EncryptAndDecrypt() {
        assertEquals(c.RSAESPKCS1Decrypt(c.RSAESPKCS1Encrypt(plaintext, e, n), d, n), plaintext);
    }

    public void testRSAESPKCS1Decrypt_mystery() {
        assertEquals(c.RSAESPKCS1Decrypt(mystery, d, n), "Yeah, you got it. :)");
    }

}