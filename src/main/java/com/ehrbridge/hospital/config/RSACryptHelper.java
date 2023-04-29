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
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

@Configuration
public class RSACryptHelper {
    public static String encryptCallbackData(ReceiveDataCallbackURLRequest receiveDataCallbackURLRequest, RSAPublicKey rsaPublicKey) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            String objString = ow.writeValueAsString(receiveDataCallbackURLRequest);
            return RSAEncryptString(objString, rsaPublicKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static ReceiveDataCallbackURLRequest decryptCallbackData(EncryptedPatientDataObject encryptedPatientDataObject) {
        String encryptedMessage = encryptedPatientDataObject.getEncrypted_data_object();
        String decryptedString = RSADecryptString(encryptedMessage, RSAHelperConfig.RSA_PRIV);
        ObjectMapper mapper = new ObjectMapper();
        try {
            Object dataObject = mapper.readValue(decryptedString, Object.class);
            String dataJson = dataObject.toString();
            System.out.println(dataJson);
            Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, (JsonDeserializer) (json, typeOfT, context) -> new Date(json.getAsLong())).create();
            ReceiveDataCallbackURLRequest receiveDataCallbackURLRequest = gson.fromJson(dataJson, ReceiveDataCallbackURLRequest.class);
            System.out.println(receiveDataCallbackURLRequest);
            return receiveDataCallbackURLRequest;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String RSADecryptString(String encryptedMessage, RSAPrivateKey rsaPrivateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey);
            byte[] decryptedMessageBytes = cipher.doFinal(encryptedMessage.getBytes(StandardCharsets.UTF_8));
            return new String(decryptedMessageBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String RSAEncryptString(String message, RSAPublicKey rsaPublicKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);
            byte[] secretMessageBytes = message.getBytes(StandardCharsets.UTF_8);
            byte[] encryptedMessageBytes = cipher.doFinal(secretMessageBytes);
            return new String(encryptedMessageBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
