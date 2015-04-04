package com.srbodroid.app.utility;

import android.util.Base64;

import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Usage:
 * <pre>
 * String crypto = SimpleCrypto.encrypt(masterpassword, cleartext)
 * ...
 * String cleartext = SimpleCrypto.decrypt(masterpassword, crypto)
 * </pre>
 *
 */
public class SimpleCrypto
{
    private static byte[] ivBytes = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
    public static String encrypt(String key, String text)
    {
        try
        {
            key = key.substring(0, 16);
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
            return Base64.encodeToString(cipher.doFinal(text.getBytes("UTF-8")), Base64.DEFAULT);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static String decrypt(String key, String text)
    {
        try
        {
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
            return new String(cipher.doFinal(Base64.decode(text.getBytes("UTF-8"), Base64.DEFAULT)), "UTF-8");
        }
        catch (Exception e)
        {
            return null;
        }
    }

}

// see http://www.androidsnippets.com/encryptdecrypt-strings