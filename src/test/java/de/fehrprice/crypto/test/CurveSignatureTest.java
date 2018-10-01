package de.fehrprice.crypto.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.fehrprice.crypto.Curve25519;
import de.fehrprice.crypto.Ed25519;

/**
 * test data here: http://ed25519.cr.yp.to/software.html
 *
 */
public class CurveSignatureTest {
	
    public static boolean disableLongRunningTest = true;
	
	private static Ed25519 ed;
	
	@BeforeAll
	public static void setUp() throws Exception {
		ed = new Ed25519();
	}

	@AfterAll
	public static void tearDown() throws Exception {
	}

	/*
	 * String API tests
	 */

/*
ALGORITHM:
Ed25519
SECRET KEY:
9d61b19deffd5a60ba844af492ec2cc4
4449c5697b326919703bac031cae7f60
PUBLIC KEY:
d75a980182b10ab7d54bfed3c964073a
0ee172f3daa62325af021a68f707511a
MESSAGE (length 0 bytes):
SIGNATURE:
e5564300c360ac729086e2cc806e828a
84877f1eb8e5d974d873e06522490155
5fb8821590a33bacc61e39701cf9b46b
d25bf5f0595bbe24655141438e7a100b
 */
	/**
	 * Test ed25519 according to RFC 8032, section 7.1. test vectors
	 * Test single ed25519 examples
	 */
	@Test
	public void Test1() {
		String secretKeyString, publicKeyString, messageString, signatureString;
		secretKeyString = "9d61b19deffd5a60ba844af492ec2cc44449c5697b326919703bac031cae7f60";
		publicKeyString = "d75a980182b10ab7d54bfed3c964073a0ee172f3daa62325af021a68f707511a";
		messageString   = "";
		signatureString = "e5564300c360ac729086e2cc806e828a84877f1eb8e5d974d873e065224901555fb8821590a33bacc61e39701cf9b46bd25bf5f0595bbe24655141438e7a100b";
		//String pubk = ed.publicKey(null);
		String pubk = ed.publicKey(secretKeyString);
		assertEquals(publicKeyString, pubk);
		String s = ed.signature(messageString,secretKeyString,pubk);
		assertEquals(signatureString, s);
		assertTrue(ed.checkvalid(s,messageString,publicKeyString));
	}

/*
  ALGORITHM:   Ed25519
   SECRET KEY:   4ccd089b28ff96da9db6c346ec114e0f   5b8a319f35aba624da8cf6ed4fb8a6fb
   PUBLIC KEY:   3d4017c3e843895a92b70aa74d1b7ebc   9c982ccf2ec4968cc0cd55f12af4660c
   MESSAGE (length 1 byte):   72
   SIGNATURE:   92a009a9f0d4cab8720e820b5f642540   a2b27b5416503f8fb3762223ebdb69da   085ac1e43e15996e458f3613d0f11d8c   387b2eaeb4302aeeb00d291612bb0c00
 */
	/**
	 * Test ed25519 according to RFC 8032, section 7.1. test vectors
	 * Test single ed25519 examples
	 */
	@Test
	public void Test2() {
		String secretKeyString, publicKeyString, messageString, signatureString;
		secretKeyString = "4ccd089b28ff96da9db6c346ec114e0f5b8a319f35aba624da8cf6ed4fb8a6fb";
		publicKeyString = "3d4017c3e843895a92b70aa74d1b7ebc9c982ccf2ec4968cc0cd55f12af4660c";
		messageString   = "72";
		signatureString = "92a009a9f0d4cab8720e820b5f642540a2b27b5416503f8fb3762223ebdb69da085ac1e43e15996e458f3613d0f11d8c387b2eaeb4302aeeb00d291612bb0c00";
		//String pubk = ed.publicKey(null);
		String pubk = ed.publicKey(secretKeyString);
		assertEquals(publicKeyString, pubk);
		String s = ed.signature(messageString,secretKeyString,pubk);
		assertEquals(signatureString, s);
		assertTrue(ed.checkvalid(s,messageString,publicKeyString));
	}

	// line 108: 
	@Test
	public void Test108() {
		String secretKeyString, publicKeyString, messageString, signatureString;
		secretKeyString = "50414cf549bcc55b5b6b75ea3782b2ea7c087b6a0106175e469ca2cc764aeb01";
		publicKeyString = "d0f30c12e997f96e7aeecd1bff6a012ec388ebf8f3f4af664804d1638e4c346a";
		messageString   = "75ad77e8c54b0b05fb2d162e7cadb8a7528081b863f76a441b374469413e5714edf54f800496af0157c17e425583414d4361f2134171c0b87c22ce6820a4850ab49d99a9badce9e36110e7f3060118b3590f82b43771e9fbb081afe62227e024d98de6cdec028d7c49490d";
		signatureString = "b309800160de43a63a89a0acb8a6050059589b3eaecac20b256fece438042f69415d8a56883ee3836d3134a7fc1de64fa8c8cecc3ce27589f606058820857a0c";
		//String pubk = ed.publicKey(null);
		String pubk = ed.publicKey(secretKeyString);
		assertEquals(publicKeyString, pubk);
		String s = ed.signature(messageString,secretKeyString,pubk);
		assertEquals(signatureString, s);
		assertTrue(ed.checkvalid(s,messageString,publicKeyString));
	}
	
	
	@Test
	void testConstants() {
		assertEquals("46316835694926478169428394003475163141307993866256225615783033603165251855960", ed.By.toString());
		assertEquals("15112221349535400772501151409588531511454012693041857206046113283949847762202", ed.Bx.toString());
	}
	
	@Test
	void validateWithFile() {
		// fields on each input line: sk, pk, m, sm
		// each field hex
		// each field colon-terminated
		// sk includes pk at end
		// sm includes m at end
		//System.out.println("pwd = " + System.getProperty("user.dir"));
		String filename = System.getProperty("user.dir") + "/src/test/resources/sign.input";
		try {
			int linenum = 0;
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line = reader.readLine();
			while (line != null && linenum < 100) {
				//System.out.println(line);
				testLine(line);
				linenum++;
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
		}  
	}

	private void testLine(String line) {
		String x[] = line.split(":");
		String secretKey = x[0].substring(0, 64);
		String publicKey = x[1];
		String messageHexString = x[2];
		String signatureString = x[3].substring(0, 128);
		String pubk = ed.publicKey(secretKey);
		assertEquals(publicKey, pubk);
		String s = ed.signature(messageHexString,secretKey,pubk);
		assertEquals(signatureString, s);
		assertTrue(ed.checkvalid(s,messageHexString,publicKey));
		
//		System.out.println("secret " + secretKey);
//		System.out.println("public " + publicKey);
//		System.out.println("message " + messageHexString);
//		System.out.println("signature " + signatureString);
	}
}

