package com.ehrbridge.hospital.config;

import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.context.annotation.Configuration;

@Configuration
public class RSAHelperConfig {
    public static RSAPublicKey RSA_PUB;
    public static RSAPrivateKey RSA_PRIV;
    public static SecretKey AES_SECRET;
    public static void generateAndSetKeys() {
        try {
            KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
            gen.initialize(4096);
            KeyPair keyPair = gen.generateKeyPair();
            RSAPublicKey pubKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey prvKey = (RSAPrivateKey) keyPair.getPrivate();
            
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(128); // The AES key size in number of bits
            SecretKey secKey = generator.generateKey();

            RSA_PUB = pubKey;
            RSA_PRIV = prvKey;
            AES_SECRET = secKey;
        } catch (NoSuchAlgorithmException e) {
            System.out.println("[Hospital] ERR: failed to generate Keys - NoSuchAlgorithmException");
        }
    }

    public static String rsaPublicKeyObjectToPEM(RSAPublicKey key) {
        try {
            KeyFactory f = KeyFactory.getInstance("RSA");
            BigInteger modulus = key.getModulus();
            BigInteger exp = new BigInteger("10001", 16);
            RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exp);
            PublicKey pub = f.generatePublic(spec);
            byte[] data = pub.getEncoded();
            String base64encoded = new String(Base64.getEncoder().encode(data));
            return base64encoded;
        } catch (Exception e) {
            System.out.println("[Hospital] ERR: failed to generate key PEM");
        }

        return "";
    }

    public static String rsaPrivateKeyObjectToPEM(RSAPrivateKey key) {
        try {
            KeyFactory f = KeyFactory.getInstance("RSA");
            BigInteger modulus = key.getModulus();
            BigInteger exp = new BigInteger("10001", 16);
            RSAPrivateKeySpec spec = new RSAPrivateKeySpec(modulus, exp);
            PrivateKey priv = f.generatePrivate(spec);
            byte[] data = priv.getEncoded();
            String base64encoded = new String(Base64.getEncoder().encode(data));
            return base64encoded;
        } catch (Exception e) {
            System.out.println("[Hospital] ERR: failed to generate key PEM");
        }

        return "";
    }

    public static RSAPublicKey rsaPEMToPublicKeyObject(String pem) {
        try {
            byte[] encoded = Base64.getDecoder().decode(pem);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            System.out.println("[Hospital] ERR: failed to parse key PEM");
        }

        return null;
    }

    public static RSAPrivateKey rsaPEMToPrivateKeyObject(String pem) {
        try {
            byte[] encoded = Base64.getDecoder().decode(pem);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            System.out.println("[Hospital] ERR: failed to parse key PEM");
        }

        return null;
    }

    public static String aesKeyToString(SecretKey secretKey) {
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        return encodedKey;
    }

    public static SecretKey aesBAToKey(byte[] decodedKey) {
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    public static SecretKey aesStringToKey(String keyString) {
        byte[] decodedKey = Base64.getDecoder().decode(keyString);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES"); 
        return originalKey;
    }

    public static byte[] aesKeytoBA(SecretKey secretKey) {
        return secretKey.getEncoded();
    }
}
