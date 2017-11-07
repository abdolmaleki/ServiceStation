package com.technotapp.servicestation.Infrastructure;

import android.util.Base64;

import com.technotapp.servicestation.application.Constant;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;

public class Encryptor {

    public static String encriptAES(String text, SecretKey secretKey) {
        try {
            IvParameterSpec IV = new IvParameterSpec(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
            String TRANSFORMATION = "AES/CBC/PKCS7Padding";
            String ALGORITHM = "AES";
            Cipher cipher = null;
            //SecretKey key = new SecretKeySpec(Base64.decode("SYpKf0CdFTD/NXAbH46b9g==", Base64.DEFAULT), ALGORITHM);
            try {
                cipher = Cipher.getInstance(TRANSFORMATION);
                cipher.init(ENCRYPT_MODE, secretKey, IV);
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            }

            byte[] encryptedData = null;
            try {

                encryptedData = cipher.doFinal(text.getBytes(Charset.forName("UTF-8")));
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            }
            String encriptString = Base64.encodeToString(encryptedData, Base64.DEFAULT);

            return encriptString;

        } catch (Exception e) {
            return "";
        }
    }

    public static String decriptAES(String aeskey, String text) {

        try {
            byte[] encryted_bytes = Base64.decode(text, Base64.DEFAULT);

            String trueKey = aeskey.substring(0, aeskey.length() - 1);
            IvParameterSpec IV = new IvParameterSpec(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
            String TRANSFORMATION = "AES/CBC/PKCS7Padding";
            String ALGORITHM = "AES";
            Cipher cipher = null;
            SecretKey key = new SecretKeySpec(Base64.decode(trueKey, Base64.DEFAULT), ALGORITHM);
            try {
                cipher = Cipher.getInstance(TRANSFORMATION);
                cipher.init(DECRYPT_MODE, key, IV);
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            }

            byte[] decryptedData = null;
            try {

                decryptedData = cipher.doFinal(encryted_bytes);
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            }

            String result = new String(decryptedData);
            String str = result;
            str += "ff";

            return result;
        } catch (Exception e) {
            AppMonitor.reportBug(e, "Encryptor", "decriptAES");
            return null;
        }
    }

    public static String encriptRSA(String text) {
        try {
            String modulusString = Constant.Encryption.RSA_KEY;
            String publicExponentString = "AQAB";
            BigInteger modulus = new BigInteger(1, Base64.decode(modulusString, Base64.DEFAULT));
            BigInteger publicExponent = new BigInteger(1, Base64.decode(publicExponentString, Base64.DEFAULT));
            RSAPublicKeySpec rsaPubKey = new RSAPublicKeySpec(modulus, publicExponent);
            KeyFactory fact = null;
            try {
                fact = KeyFactory.getInstance("RSA");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            PublicKey pubKey = null;
            try {
                pubKey = fact.generatePublic(rsaPubKey);
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }
            byte[] cipherText = null;
            try {
                final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
                cipher.init(Cipher.ENCRYPT_MODE, pubKey);
                cipherText = cipher.doFinal(text.getBytes(Charset.forName("UTF-8")));
            } catch (Exception e) {
                e.printStackTrace();
            }

            String base64string = Base64.encodeToString(cipherText, Base64.DEFAULT);
            return base64string;
        } catch (Exception e) {
            return "";
        }
    }

    public static String decriptRSA(String text) {
        try {


            String modulusString = Constant.Encryption.RSA_KEY;
            String publicExponentString = "AQAB";
            BigInteger modulus = new BigInteger(1, Base64.decode(modulusString, Base64.DEFAULT));
            BigInteger publicExponent = new BigInteger(1, Base64.decode(publicExponentString, Base64.DEFAULT));
            RSAPublicKeySpec rsaPubKey = new RSAPublicKeySpec(modulus, publicExponent);
            KeyFactory fact = null;
            try {
                fact = KeyFactory.getInstance("RSA");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            PublicKey pubKey = null;
            try {
                pubKey = fact.generatePublic(rsaPubKey);
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }
            byte[] cipherText = null;
            try {
                final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
                cipher.init(Cipher.DECRYPT_MODE, pubKey);
                cipherText = cipher.doFinal(text.getBytes(Charset.forName("UTF-8")));
            } catch (Exception e) {
                e.printStackTrace();
            }

            String base64string = Base64.encodeToString(cipherText, Base64.DEFAULT);
            return base64string;
        } catch (Exception e) {
            return "";
        }
    }

    public static SecretKey generateRandomAESKey() {
        try {
            SecretKey secretKey = null;
            KeyGenerator gen = null;
            try {
                gen = KeyGenerator.getInstance("AES");
                gen.init(256); /* 256-bit AES */
                secretKey = gen.generateKey();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return secretKey;
        } catch (Exception e) {
            return null;
        }
    }

}