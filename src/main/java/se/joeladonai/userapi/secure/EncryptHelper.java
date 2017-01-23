package se.joeladonai.userapi.secure;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;

public class EncryptHelper {
	
	public static final String generateSalt() {
		SecureRandom secureRandom;
		byte[] salt= new byte[64] ;
		try {
			secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.nextBytes(salt);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}		
		return Base64.encodeBase64String(salt);	
	    
	}
	
	public static final String hashPassword(char[] string,byte[] salt) {
		int iterations = 40000;
		PBEKeySpec spec = new PBEKeySpec(string, salt, iterations, 64*8);
        SecretKeyFactory skf;
        byte[] hashKey = new byte[64]; 
		try {
			skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			hashKey = skf.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return Base64.encodeBase64String(hashKey);
	}

}
