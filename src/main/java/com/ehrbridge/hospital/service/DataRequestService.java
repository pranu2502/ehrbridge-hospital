package com.ehrbridge.hospital.service;

import com.ehrbridge.hospital.dto.consent.ConsentJSONObj;
import com.ehrbridge.hospital.dto.dataRequest.hip.DataRequestHIPRequest;
import com.ehrbridge.hospital.dto.dataRequest.hip.DataRequestHIPResponse;
import com.ehrbridge.hospital.dto.dataRequest.hiu.DataRequestHIURequest;
import com.ehrbridge.hospital.dto.dataRequest.hiu.DataRequestHIUResponse;
import com.ehrbridge.hospital.dto.gateway.DataRequestGatewayRequest;
import com.ehrbridge.hospital.dto.gateway.DataRequestGatewayResponse;
import com.ehrbridge.hospital.entity.DataRequestHIP;
import com.ehrbridge.hospital.entity.DataRequestsHIU;
import com.ehrbridge.hospital.repository.ConsentObjectHIPRepository;
import com.ehrbridge.hospital.repository.ConsentTransactionRepository;
import com.ehrbridge.hospital.repository.DataRequestsHIPRepository;
import com.ehrbridge.hospital.repository.DataRequestsHIURepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ehrbridge.hospital.entity.ConsentObjectHIP;

import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.web.client.RestTemplate;

import java.security.*;
import java.security.interfaces.RSAPublicKey;
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
    private final ConsentTransactionRepository consentTransactionRepository;
    
    @Value("${ehrbridge.gateway.host}")
    private String GATEWAY_HOST;

    @Value("${ehrbridge.gateway.data-request.endpoint}")
    private String GATEWAY_DATA_REQ_ENDPOINT;

    @Autowired
    private RestTemplate rest;

    @Autowired
    private HttpHeaders headers;


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

        String data_request_id = dataRequest.getData_request_id();
        var transaction = consentTransactionRepository.findByTxnID(request.getTxnID()).orElseThrow();

        DataRequestGatewayRequest gatewayRequest = DataRequestGatewayRequest
                .builder()
                .signed_consent_object(transaction.getEncrypted_consent_object())
                .requestID(data_request_id)
                .doctorID(request.getDoctorID())
                .callbackURL("dmjdkjdj")
                .ehrbID(request.getEhrbID())
                .hiuID("dshjkjfdhjvf")
                .hipID(request.getHipID())
                .txnID(request.getTxnID())
                .build();
        ResponseEntity<DataRequestGatewayResponse> gatewayResponse = pushConsentRequestToGateway(gatewayRequest);

        if(gatewayResponse == null)
        {
            return DataRequestHIUResponse.builder().data_request_id(data_request_id).message("Failed to Send Data Request to Gateway").build();
        }
        else
        {
            if(gatewayResponse.getBody().getStatus().equals("FAILED"))
            {
                return DataRequestHIUResponse.builder().data_request_id(data_request_id).message("Failed to send Data Request to HIP").build();
            }
            else {
                return DataRequestHIUResponse.builder().data_request_id(data_request_id).message("Data Request Generated").build();
            }
        }


    }

    private ResponseEntity<DataRequestGatewayResponse> pushConsentRequestToGateway(DataRequestGatewayRequest request) {
        final String GATEWAY_REQ_ENDPOINT = GATEWAY_HOST + GATEWAY_DATA_REQ_ENDPOINT;
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            String jsonConsentObj = ow.writeValueAsString(request);
            HttpEntity<String> requestEntity = new HttpEntity<String>(jsonConsentObj, headers);
            ResponseEntity<DataRequestGatewayResponse> responseEntity = rest.exchange(GATEWAY_REQ_ENDPOINT, HttpMethod.POST, requestEntity, DataRequestGatewayResponse.class);
            if(responseEntity.getStatusCode().value() == 200){
                return responseEntity;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
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
