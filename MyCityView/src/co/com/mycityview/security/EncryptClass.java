package co.com.mycityview.security;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
// CIPHER / GENERATORS

import android.util.Base64;
import android.util.Log;

import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;



/**
 *
 * @author andres.ospina
 */
public class EncryptClass {

    Cipher ecipher;
    Cipher dcipher;

    public EncryptClass(SecretKey key, String algorithm) {
       try {
           ecipher = Cipher.getInstance(algorithm);
           dcipher = Cipher.getInstance(algorithm);
           ecipher.init(Cipher.ENCRYPT_MODE, key);
           dcipher.init(Cipher.DECRYPT_MODE, key);

       } catch (Exception e) {
           Log.e(e.getLocalizedMessage(),e.getMessage(),e);
       }
   }


    public EncryptClass(String passPhrase) {
    	// 8-bytes Salt
        byte[] salt = {             (byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,
        (byte)0x56, (byte)0x34, (byte)0xE3, (byte)0x03        };
        // Iteration count
        int iterationCount = 19;
         try {               KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
         SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

         ecipher = Cipher.getInstance(key.getAlgorithm());
         dcipher = Cipher.getInstance(key.getAlgorithm());
         // Prepare the parameters to the cipthers
         AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
         ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
         dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
         } catch (Exception e) {
             Log.e(e.getLocalizedMessage(), e.getMessage(), e);
         }
    }



    public String encrypt(String str) {
        try {
            // Encode the string into bytes using utf-8
            byte[] utf8 = str.getBytes("UTF8");
            // Encrypt
            byte[] enc = ecipher.doFinal(utf8);
            // Encode bytes to base64 to get a string
            return Base64.encodeToString(enc, Base64.DEFAULT).toString();
        } catch (Exception e) {
            Log.e(e.getLocalizedMessage(), e.getMessage(), e);
        }
        return null;
    }


    
    public static void main(String[] args) {
		EncryptClass encrypt = new EncryptClass("encrip_mycityview"); 
		System.out.println(encrypt.encrypt("usuario.rest:ftd*+55qx:07-09-2015-09-36"));
//		System.out.println(encrypt.decrypt("s3tCeruMSRG8j7eAEDp7FMQZfnCkPoHMga5NqiSS4KOV0Oo4KouBbTENl7jkamFwGo6Nj5kyGSo="));
	}

}
