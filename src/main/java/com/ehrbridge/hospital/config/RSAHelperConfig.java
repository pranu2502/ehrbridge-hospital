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

import org.springframework.context.annotation.Configuration;

@Configuration
public class RSAHelperConfig {
    public static RSAPublicKey RSA_PUB;
    public static RSAPrivateKey RSA_PRIV;
    public static void generateAndSetKeys() {
        try {
            KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
            gen.initialize(2048);
            KeyPair keyPair = gen.generateKeyPair();
            RSAPublicKey pubKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey prvKey = (RSAPrivateKey) keyPair.getPrivate();

            RSA_PUB = pubKey;
            RSA_PRIV = prvKey;
        } catch (NoSuchAlgorithmException e) {
            System.out.println("[ConsentManager] ERR: failed to generate Keys - NoSuchAlgorithmException");
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
            System.out.println("[ConsentManager] ERR: failed to generate key PEM");
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
            System.out.println("[ConsentManager] ERR: failed to generate key PEM");
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
            System.out.println("[ConsentManager] ERR: failed to parse key PEM");
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
            System.out.println("[ConsentManager] ERR: failed to parse key PEM");
        }

        return null;
    }
}
