package com.ehrbridge.hospital.config;

import com.ehrbridge.hospital.dto.consent.CMConsentObject;
import com.ehrbridge.hospital.dto.dataRequest.hiu.EncryptedPatientDataObject;
import com.ehrbridge.hospital.dto.dataRequest.hiu.ReceiveDataCallbackURLRequest;
import com.ehrbridge.hospital.repository.DataRequestsHIPRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.stream.JsonReader;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

@Configuration
public class RSACryptHelper {
    public static EncryptedPatientDataObject encryptCallbackData(ReceiveDataCallbackURLRequest receiveDataCallbackURLRequest, RSAPublicKey rsaPublicKey, SecretKey aesSecretKey) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        EncryptedPatientDataObject epdo = new EncryptedPatientDataObject();
        try {
            // encrypt the object using AES key
            String objStringRaw = ow.writeValueAsString(receiveDataCallbackURLRequest);
            System.out.println("OBHJECT RAWWWWW");
            System.out.println(objStringRaw);
            byte[] objString = objStringRaw.getBytes();
            byte[] encryptedObjectString = AESEncryptString(objString, aesSecretKey);
            epdo.setEncrypted_data_object(encryptedObjectString);

            // encrypt the AES key with RSA
            byte[] stringAESKey = RSAHelperConfig.aesKeytoBA(aesSecretKey);
            byte[] encryptedAESKey = RSAEncryptString(stringAESKey, rsaPublicKey);

            epdo.setAes_secret(encryptedAESKey);
            return epdo;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static ReceiveDataCallbackURLRequest decryptCallbackData(EncryptedPatientDataObject encryptedPatientDataObject) {
        byte[] encryptedAESKey = encryptedPatientDataObject.getAes_secret();
        SecretKey aesKey = RSAHelperConfig.aesBAToKey(RSADecryptString(encryptedAESKey, RSAHelperConfig.RSA_PRIV));

        byte[] encryptedMessage = encryptedPatientDataObject.getEncrypted_data_object();
        byte[] decryptedString = AESDecryptString(encryptedMessage, aesKey);
        System.out.println(new String(decryptedString));
        ObjectMapper mapper = new ObjectMapper();
        try {
            Object dataObject = mapper.readValue(new String(decryptedString), Object.class);
            String dataJson = new String(decryptedString);
            System.out.println(dataObject);
            Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, (JsonDeserializer) (json, typeOfT, context) -> new Date(json.getAsLong())).create();
            JsonReader reader = new JsonReader(new StringReader(dataJson));
            reader.setLenient(true);
            ReceiveDataCallbackURLRequest receiveDataCallbackURLRequest = gson.fromJson(reader, ReceiveDataCallbackURLRequest.class);
            System.out.println(receiveDataCallbackURLRequest);
            return receiveDataCallbackURLRequest;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] RSADecryptString(byte[] encryptedMessage, RSAPrivateKey rsaPrivateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey);
            byte[] decryptedMessageBytes = cipher.doFinal(encryptedMessage);
            return decryptedMessageBytes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] RSAEncryptString(byte[] message, RSAPublicKey rsaPublicKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);
            byte[] secretMessageBytes = message; //message.getBytes(StandardCharsets.UTF_8);
            byte[] encryptedMessageBytes = cipher.doFinal(secretMessageBytes);
            return encryptedMessageBytes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] AESEncryptString(byte[] plainText, SecretKey secKey) {
        try {
            Cipher aesCipher = Cipher.getInstance("AES");
            aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
            byte[] byteCipherText = aesCipher.doFinal(plainText);
            return byteCipherText;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] AESDecryptString(byte[] cipherText, SecretKey secKey) {
        try {
            Cipher aesCipher = Cipher.getInstance("AES");
            aesCipher.init(Cipher.DECRYPT_MODE, secKey);
            byte[] byteCipherText = cipherText;
            byte[] bytePlainText = aesCipher.doFinal(byteCipherText);
            return bytePlainText;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
