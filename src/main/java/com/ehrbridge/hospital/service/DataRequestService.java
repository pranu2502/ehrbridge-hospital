package com.ehrbridge.hospital.service;

import com.ehrbridge.hospital.auth.dto.ConsentObject;
import com.ehrbridge.hospital.dto.consent.CMConsentObject;
import com.ehrbridge.hospital.dto.consent.ConsentJSONObj;
import com.ehrbridge.hospital.dto.dataRequest.DataRequestHIPRequest;
import com.ehrbridge.hospital.dto.dataRequest.DataRequestHIPResponse;
import com.ehrbridge.hospital.dto.dataRequest.DataRequestHIURequest;
import com.ehrbridge.hospital.dto.dataRequest.DataRequestHIUResponse;
import com.ehrbridge.hospital.entity.DataRequestHIP;
import com.ehrbridge.hospital.entity.DataRequestsHIU;
import com.ehrbridge.hospital.repository.ConsentObjectHIPRepository;
import com.ehrbridge.hospital.repository.DataRequestsHIPRepository;
import com.ehrbridge.hospital.repository.DataRequestsHIURepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ehrbridge.hospital.entity.ConsentObjectHIP;

import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

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
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class DataRequestService {

    private final DataRequestsHIURepository dataRequestsHIURepository;
    private final DataRequestsHIPRepository dataRequestsHIPRepository;
    private final ConsentObjectHIPRepository consentObjectHIPRepository;
    
    @Value("${ehrbridge.gateway.host}")
    private String GATEWAY_HOST;

    @Value("${ehrbridge.gateway.data-request.endpoint}")
    private String GATEWAY_DATA_REQ_ENDPOINT;
    // @Value("${ehrbridge.gateway.datarequest.endpoint}")
    // private final String GATEWAY_DATA_REQ_ENDPOINT;


    public static RSAPublicKey rsaPEMToPublicKeyObject(String pem) {
        try {
            byte[] encoded = Base64.getDecoder().decode(pem);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            System.out.println("ERR: failed to parse key PEM");
        }

        return null;
    }

    public static boolean matchConsentObjects(String signed_obj_hiu, String signed_obj_gateway, RSAPublicKey public_key){
        Algorithm algorithm = Algorithm.RSA256(public_key);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded_obj_gateway = verifier.verify(signed_obj_gateway);
        DecodedJWT decoded_obj_hiu = verifier.verify(signed_obj_hiu);
        String jsonStrGateway = decoded_obj_gateway.getClaim("consent_obj").toString();
        String jsonStrHIU  = decoded_obj_hiu.getClaim("consent_obj").toString();
        ObjectMapper mapper = new ObjectMapper();
        try {
            ConsentJSONObj consentObjGateway = mapper.readValue(jsonStrGateway, ConsentJSONObj.class);
            ConsentJSONObj consentObjHIU = mapper.readValue(jsonStrHIU, ConsentJSONObj.class);
            if(Objects.deepEquals(consentObjGateway, consentObjHIU)){
               return true; 
            }
            return false;

        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public DataRequestHIUResponse requestDataHIU(DataRequestHIURequest request)
    {
        var dataRequest = DataRequestsHIU.builder()
                .ehbrID(request.getEhrbID())
                .txnID(request.getTxnID())
                .hipID(request.getHipID())
                .request_message(request.getRequest_msg())
                .build();

        dataRequestsHIURepository.save(dataRequest);

        Long data_request_id = dataRequest.getData_request_id();
        //TODO: Send Request to ABDM
    

        return DataRequestHIUResponse.builder().data_request_id(data_request_id).message("Data Request Generated").build();
    }

    public DataRequestHIPResponse requestDataHIP(DataRequestHIPRequest request) {
        var dataRequest = DataRequestHIP.builder()
                .encrypted_consent_object(request.getEncrypted_consent_object())
                .txnID(request.getTxnID())
                .requestID(request.getRequestID())
                .ehbrID(request.getEhrbID())
                .hiuID(request.getHiuID())
                .request_message(request.getRequest_msg())
                .callback_url(request.getCallbackURL())
                .build();

        dataRequestsHIPRepository.save(dataRequest);
        // fetch the encrypted consent object from ConsentObjectHIP
        System.out.println(request.getTxnID());
        Optional<ConsentObjectHIP> consentObjectHIP = consentObjectHIPRepository.findByTxnID(request.getTxnID());

        if(!consentObjectHIP.isPresent()){
            return DataRequestHIPResponse.builder().message("TxnID invalid!").build();
        }


        RSAPublicKey publicKey = rsaPEMToPublicKeyObject(consentObjectHIP.get().getPublic_key());
        if(publicKey == null){
            return DataRequestHIPResponse.builder().message("Unable to parse public key recieved from gateway").build();
        }
        String signed_object_gateway = consentObjectHIP.get().getEncrypted_consent_object();
        String signed_object_hiu = request.getEncrypted_consent_object();
        if(matchConsentObjects(signed_object_hiu, signed_object_gateway, publicKey)){
            return DataRequestHIPResponse.builder().message("Consent objects matched, sending data to HIU on callbackurl").build();
        }

        //TODO: Send FHIR via the call back link provided.
        return DataRequestHIPResponse.builder().message("Consent Objects do not match!").build();
    }


}
