package com.ehrbridge.hospital.service;

import com.ehrbridge.hospital.dto.consent.CMConsentObject;
import com.ehrbridge.hospital.dto.consent.ConsentJSONObj;
import com.ehrbridge.hospital.dto.dataRequest.hip.DataRequestHIPRequest;
import com.ehrbridge.hospital.dto.dataRequest.hip.DataRequestHIPResponse;
import com.ehrbridge.hospital.dto.dataRequest.hiu.DataRequestHIURequest;
import com.ehrbridge.hospital.dto.dataRequest.hiu.DataRequestHIUResponse;
import com.ehrbridge.hospital.dto.dataRequest.hiu.ReceiveDataCallbackURLRequest;
import com.ehrbridge.hospital.dto.dataRequest.hiu.ReceiveDataCallbackURLResponse;
import com.ehrbridge.hospital.dto.gateway.DataRequestGatewayRequest;
import com.ehrbridge.hospital.dto.gateway.DataRequestGatewayResponse;
import com.ehrbridge.hospital.entity.DataRequestHIP;
import com.ehrbridge.hospital.entity.DataRequestsHIU;
import com.ehrbridge.hospital.repository.ConsentObjectHIPRepository;
import com.ehrbridge.hospital.repository.ConsentTransactionRepository;
import com.ehrbridge.hospital.repository.DataRequestsHIPRepository;
import com.ehrbridge.hospital.repository.DataRequestsHIURepository;
import com.ehrbridge.hospital.repository.PatientRecordsRepository;
import com.ehrbridge.hospital.repository.PatientRepository;
import com.ehrbridge.hospital.repository.ReceivedDataRecordsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ehrbridge.hospital.entity.Patient;
import com.ehrbridge.hospital.entity.PatientRecords;
import com.ehrbridge.hospital.entity.ReceivedPatientRecords;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ehrbridge.hospital.entity.ConsentObjectHIP;
import com.ehrbridge.hospital.entity.ConsentObjectHIU;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
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
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;


@Service
@RequiredArgsConstructor
public class DataRequestService {

    private final DataRequestsHIURepository dataRequestsHIURepository;
    private final DataRequestsHIPRepository dataRequestsHIPRepository;
    private final ConsentObjectHIPRepository consentObjectHIPRepository;
    private final ConsentTransactionRepository consentTransactionRepository;
    private final PatientRecordsRepository patientRecordsRepository;
    private final PatientRepository patientRepository;

    @Value("${ehrbridge.gateway.host}")
    private String GATEWAY_HOST;

    @Value("${ehrbridge.gateway.data-request.endpoint}")
    private String GATEWAY_DATA_REQ_ENDPOINT;

    @Value("${server.port}")
    private String PORT;


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

    public ResponseEntity<DataRequestHIUResponse> requestDataHIU(DataRequestHIURequest request)
    {
        var dataRequest = DataRequestsHIU.builder()
                .ehbrID(request.getEhrbID())
                .txnID(request.getTxnID())
                .hipID(request.getHipID())
                .request_message(request.getRequest_msg())
                .dateFrom(request.getDateFrom())
                .dateTo(request.getDateTo())
                .build();

        try {
            dataRequestsHIURepository.save(dataRequest);
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<DataRequestHIUResponse>(DataRequestHIUResponse.builder().message("Could not save data request to the hospitalDB").build(), HttpStatusCode.valueOf(500));
        }


        String data_request_id = dataRequest.getData_request_id();
        var transaction = consentTransactionRepository.findByTxnID(request.getTxnID()).orElseThrow();
        String callBackURL = "https://localhost:" + PORT + "/api/v1/data/receive-data-hiu";
        DataRequestGatewayRequest gatewayRequest = DataRequestGatewayRequest
                .builder()
                .signed_consent_object(transaction.getEncrypted_consent_object())
                .requestID(data_request_id)
                .doctorID(request.getDoctorID())
                .callbackURL(callBackURL)
                .ehrbID(request.getEhrbID())
                .hiuID("dshjkjfdhjvf")
                .hipID(request.getHipID())
                .request_msg(request.getRequest_msg())
                .txnID(request.getTxnID())
                .dateFrom(request.getDateFrom())
                .dateTo(request.getDateTo())
                .build();
        ResponseEntity<DataRequestGatewayResponse> gatewayResponse = pushConsentRequestToGateway(gatewayRequest);

        if(gatewayResponse == null)
        {
            return new ResponseEntity<DataRequestHIUResponse>(DataRequestHIUResponse.builder().data_request_id(data_request_id).message("Failed to Send Data Request to Gateway").build(), HttpStatusCode.valueOf(501));
        }
        else
        {
            if(gatewayResponse.getStatusCode().value() != 200)
            {
                return new ResponseEntity<DataRequestHIUResponse>(DataRequestHIUResponse.builder().data_request_id(data_request_id).message("Failed to send Data Request to HIP").build(), HttpStatusCode.valueOf(503));
            }
            else {
                return new ResponseEntity<DataRequestHIUResponse>(DataRequestHIUResponse.builder().data_request_id(data_request_id).message("Data Request Generated").build(), HttpStatusCode.valueOf(200));
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

    public ResponseEntity<DataRequestHIPResponse> requestDataHIP(DataRequestHIPRequest request) {
        var dataRequest = DataRequestHIP.builder()
                .encrypted_consent_object(request.getEncrypted_consent_object())
                .txnID(request.getTxnID())
                .requestID(request.getRequestID())
                .ehbrID(request.getEhrbID())
                .hiuID(request.getHiuID())
                .request_message(request.getRequest_msg())
                .callback_url(request.getCallbackURL())
                .dateFrom(request.getDateFrom())
                .dateTo(request.getDateTo())
                .build();
        try {
            dataRequestsHIPRepository.save(dataRequest);
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<DataRequestHIPResponse>(DataRequestHIPResponse.builder().message("Could not save data request to HIP database").build(), HttpStatusCode.valueOf(500));
        }
        // fetch the encrypted consent object from ConsentObjectHIP
        Optional<ConsentObjectHIP> consentObjectHIP = consentObjectHIPRepository.findByTxnID(request.getTxnID());

        if(!consentObjectHIP.isPresent()){
            return new ResponseEntity<DataRequestHIPResponse>(DataRequestHIPResponse.builder().message("TxnID in data request is invalid!").build(), HttpStatusCode.valueOf(403));
        }

        RSAPublicKey publicKey = rsaPEMToPublicKeyObject(consentObjectHIP.get().getPublic_key());
        if(publicKey == null){
            return new ResponseEntity<DataRequestHIPResponse>(DataRequestHIPResponse.builder().message("Unable to parse public key recieved from gateway").build(), HttpStatusCode.valueOf(501));
        }

        String signed_object_gateway = consentObjectHIP.get().getEncrypted_consent_object();
        String signed_object_hiu = request.getEncrypted_consent_object();
        if(matchConsentObjects(signed_object_hiu, signed_object_gateway, publicKey)== false){
            return new ResponseEntity<DataRequestHIPResponse>(DataRequestHIPResponse.builder().message("Consent object from HIU, does not match with consent object received from the gateway").build(), HttpStatusCode.valueOf(403));

        }

        //TODO: Send FHIR via the call back link provided.

        String ehrbID = request.getEhrbID();
        String patientID = null;
        List<Patient> patients = patientRepository.findAll();
        for (Patient patient : patients) {
            if (patient.getEhrbID().equals(ehrbID)) {
                patientID = patient.getId();
            }
        }

        if(patientID == null) {
            return new ResponseEntity<DataRequestHIPResponse>(DataRequestHIPResponse.builder().message("Patient could not be found").build(), HttpStatusCode.valueOf(200));
        }

        // Compare dates with consent object
        // Convert string to string array
        //Iterate through hiTypes and departments
        String[] hiTypesRequest = this.splitToArray(request.getHiType());
        String[] departmentsRequest = this.splitToArray(request.getDepartments());

        List<PatientRecords> patientRecords = patientRecordsRepository.findAll();
        List<PatientRecords> patientRecordsForID = new ArrayList<PatientRecords>();

        for (PatientRecords record : patientRecords) {
            if (record.getPatientID().equals(patientID)) {
                if (record.getTimeStamp().compareTo(request.getDateFrom()) >= 0){
                    if(record.getTimeStamp().compareTo(request.getDateTo()) <= 0) {
                        for(String hiT : hiTypesRequest) {
                            if (record.getHiType().equals(hiT)) {
                                for (String dep : departmentsRequest) {
                                    if(record.getDepartment().equals(dep)) {
                                        patientRecordsForID.add(record);
                                    }
                                }
                            }
                        }
                        
                    }
                }

            }
        }


        ReceiveDataCallbackURLRequest receiveDataCallbackURLRequest = new ReceiveDataCallbackURLRequest(patientRecordsForID, ehrbID, request.getTxnID());


        String callbackURL = request.getCallbackURL();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            String jsonConsentObj = ow.writeValueAsString(receiveDataCallbackURLRequest);
            HttpEntity<String> requestEntity = new HttpEntity<String>(jsonConsentObj, headers);
            ResponseEntity<ReceiveDataCallbackURLResponse> responseEntity = rest.exchange(callbackURL, HttpMethod.POST, requestEntity, ReceiveDataCallbackURLResponse.class);
            if(responseEntity.getStatusCode().value() == 200) {
                // return responseEntity;
                return new ResponseEntity<DataRequestHIPResponse>(DataRequestHIPResponse.builder().message("Consent objects matched, Data transfer succesful").build(), HttpStatusCode.valueOf(200));
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return new ResponseEntity<DataRequestHIPResponse>(DataRequestHIPResponse.builder().message("Consent objects matched but data transfer failed").build(), HttpStatusCode.valueOf(500));


    }

    private String[] splitToArray(String strArr) {
        strArr = strArr.substring(1, strArr.length() - 1);
        String[] strs = strArr.split(",");
        return strs;
    }

    private bool makeSubset(DataRequestHIPRequest request, CMConsentObject consentObject) {
        if (request.getDateFrom().compareTo(consentObject.getconsentFrom) < 0) {
            request.setDateFrom(consentFrom);
        }
        if (request.getDateTo().compareTo(consentTo) > 0) {
            request.setDateTo(consentTo);
        }



}
