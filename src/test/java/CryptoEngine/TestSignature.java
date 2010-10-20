package test.java.CryptoEngine;

import junit.framework.TestCase;
import java.math.BigInteger;
import main.java.CryptoEngine.*;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;

public class TestSignature extends TestCase {

    private Signature s;

    private String username1;
    private String message1;

    private BigInteger e1;
    private BigInteger d1;
    private BigInteger n1;

    private BigInteger d2;
    private BigInteger n2;
    private String signed_message2;
    private String signed_certificate2;

    public void setUp() {
         try {
			s = new Signature();
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

         username1 = "chetan51";
         message1 = "This is a the message to be signed (a String)";

         n1 = new BigInteger("A9E167983F39D55FF2A093415EA6798985C8355D9A915BFB1D01DA197026170FBDA522D035856D7A986614415CCFB7B7083B09C991B81969376DF9651E7BD9A93324A37F3BBBAF460186363432CB07035952FC858B3104B8CC18081448E64F1CFB5D60C4E05C1F53D37F53D86901F105F87A70D1BE83C65F38CF1C2CAA6AA7EB", 16);
         e1 = new BigInteger("67CD484C9A0D8F98C21B65FF22839C6DF0A6061DBCEDA7038894F21C6B0F8B35DE0E827830CBE7BA6A56AD77C6EB517970790AA0F4FE45E0A9B2F419DA8798D6308474E4FC596CC1C677DCA991D07C30A0A2C5085E217143FC0D073DF0FA6D149E4E63F01758791C4B981C3D3DB01BDFFA253BA3C02C9805F61009D887DB0319", 16);
         d1 = BigInteger.valueOf(65537);

         n2 = new BigInteger("969a193f7f95592941653b50e55deea362254d71ceac43466219fe20abfebe6313b909eed1d3665b87e7223cc6a2596b23f5dd1e91dac4fcd7388437421eac483f6f468b11050a1b4ad70ab7ca918ada85735caf180b5d50faa1f8be14f3e720452749f067d6db30a45480f818e2b158d7e4d36baa98ab5af1f4e235a48456dde9fe5396ddc3f1aadc2de9b652e7e477f4e3ba11d75997eb06f82ddb5709f8a780a20357878321ac3ef11c2774592a5c6845df3ae4fe622427b6d6ac420e9630afa44aef9be96e7b25596b8bb707d72bc8f2a321868e7f26edeacd7228efe4ed9b3688c888638f9d3a5b36d96012296ea9bbba5eb80b436c5ce190324cb53c2d", 16);
         d2 = BigInteger.valueOf(5);

         signed_message2 = "{\"username\": \"cs161\", \"message\": \"hello world! (and we really mean it)\", \"signature\": \"4ee6a652369ba2445af5497cf13343d432c70faaa1f654996deedb544d9db38ec27c05dfd80685247fdc4214dfdd5842ad16a69c569de02faf9f4ee1a7819dee91176b9bb1c993635752a6938f7fbe79016a752fe87cbd9a5ac8b9fd235d2b57505779244a26c57356a44f2ad354c6933212194ccbf09cb975879d83fba69b3d863d1ab4bbc45110d8c8b549035e0b39f9541c49bc8fc74017b10ec4d8b09daea7081fb581c8adfc38ab5a86f6391867f0c1dd66205fb44202f783d2b4fbaa8b845bbb82d92042b1cc558b863ad1939888b673ff846a51649cb84b0c8e1d803c48f00e072900703bc9ac09bcaac67e0035dd39015019baafb0653cff5e04781d\"}";

         signed_certificate2 = "{\"username\": \"cs161\", \"message\": \"{\\\"username\\\": \\\"cs161\\\", \\\"public_exponent\\\": \\\"5\\\", \\\"version\\\": 1, \\\"realname\\\": \\\"Your friendly TAs\\\", \\\"not_after\\\": 1317624343067, \\\"notes\\\": \\\"\\\", \\\"modulus\\\": \\\"969a193f7f95592941653b50e55deea362254d71ceac43466219fe20abfebe6313b909eed1d3665b87e7223cc6a2596b23f5dd1e91dac4fcd7388437421eac483f6f468b11050a1b4ad70ab7ca918ada85735caf180b5d50faa1f8be14f3e720452749f067d6db30a45480f818e2b158d7e4d36baa98ab5af1f4e235a48456dde9fe5396ddc3f1aadc2de9b652e7e477f4e3ba11d75997eb06f82ddb5709f8a780a20357878321ac3ef11c2774592a5c6845df3ae4fe622427b6d6ac420e9630afa44aef9be96e7b25596b8bb707d72bc8f2a321868e7f26edeacd7228efe4ed9b3688c888638f9d3a5b36d96012296ea9bbba5eb80b436c5ce190324cb53c2d\\\", \\\"email\\\": \\\"cs161.proj@gmail.com\\\", \\\"not_before\\\": 1286952289632}\", \"signature\": \"196744f81e674820ad77c06da531833e12f924e124abe51f91e7e5b6b6973bb8bbc5fe7648d6581266efa8f60dd5aed5f5a74e48f0acaa8409df1f715a9cb3b41ddda8b27cc1a2cc859ad05044bdb5a1b5f075df393bf24483b6f27304f710307a565c0c0d87c04681a54e9785630ee41f683b3801707bd7c82c14b778b8d74407436b28079a69433ddb0a16f050278232852d61059e3fc4fa0e129d9f133d7a063dd5f9f25ffd3bf6074cfc5b7009a91b1e53199ffde1f0158c016974b78926cd63706bb47d2b21d5e355a79ac377e36ac95631236e0411a7210c002b6e1878ac6c1c719240eb7262dc74f9dad1fc6497447d4e434596cca2d06e716f9bd478\"}";
    }

    public void testGenerateAndVerifySignature() throws JSONException {
        String signature = s.generateSignature(username1, message1, e1, n1);
        assertEquals(s.verifySignature(signature, d1, n1), true);
    }

    public void testVerifySignedMessage() throws JSONException {
        assertEquals(s.verifySignature(signed_message2, d2, n2), true);
    }

    public void testVerifySignedCertificate() throws JSONException {
        assertEquals(s.verifySignature(signed_certificate2, d2, n2), true);
    }

}