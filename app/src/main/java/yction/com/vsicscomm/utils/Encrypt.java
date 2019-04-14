package yction.com.vsicscomm.utils;

import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by yww08 on 2017-12-09.
 * 加密解密类
 */

public class Encrypt {

    public static byte[] EncryptDes(byte[] pToEncrypt, byte[] iv) throws Exception {
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec key=new SecretKeySpec(iv,"DES");
        Cipher cipher= Cipher.getInstance("DES/CBC/PKCS5Padding","BC");   //PKCS7Padding  DES/ECB/ZeroBytePadding
        cipher.init(Cipher.ENCRYPT_MODE,key,zeroIv);
        return cipher.doFinal(pToEncrypt);
    }

    public static byte[] DecryptDes(byte[] pToDecrypt, byte[] iv) throws Exception {
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec key=new SecretKeySpec(iv,"DES");
        Cipher cipher= Cipher.getInstance("DES/CBC/PKCS5Padding","BC");
        cipher.init(Cipher.DECRYPT_MODE,key,zeroIv);
        return cipher.doFinal(pToDecrypt);
    }

    public static String EncodeBase64(byte[] data){
        //return new String(data);
        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    public static byte[] DecodeBase64(String data){
        //return data.getBytes();
        return Base64.decode(data, Base64.DEFAULT);
    }

    static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                StringBuilder h = new StringBuilder(Integer.toHexString(0xFF & aMessageDigest));
                while (h.length() < 2)
                    h.insert(0, "0");
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    private final static char[] hexArray = "0123456789abcdef".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
