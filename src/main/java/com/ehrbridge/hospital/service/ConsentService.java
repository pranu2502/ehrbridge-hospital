package com.ehrbridge.hospital.service;

import com.ehrbridge.hospital.dto.consent.FetchConsentObjsResponse;
import com.ehrbridge.hospital.dto.consent.FetchConsentReqsResponse;
import com.ehrbridge.hospital.dto.consent.FetchConsentTransactionResponse;
import com.ehrbridge.hospital.dto.consent.GenerateConsent.GenerateConsentRequest;
import com.ehrbridge.hospital.dto.consent.GenerateConsent.GenerateConsentResponse;
import com.ehrbridge.hospital.dto.consent.HookConsent.HookConsentRequestHIP;
import com.ehrbridge.hospital.dto.consent.HookConsent.HookConsentRequestHIU;
import com.ehrbridge.hospital.dto.gateway.GenConsentResponse;
import com.ehrbridge.hospital.entity.ConsentObjectHIP;
import com.ehrbridge.hospital.entity.ConsentObjectHIU;
import com.ehrbridge.hospital.entity.ConsentTransaction;
import com.ehrbridge.hospital.entity.Doctor;
import com.ehrbridge.hospital.repository.ConsentObjectHIPRepository;
import com.ehrbridge.hospital.repository.ConsentObjectHIURepository;
import com.ehrbridge.hospital.repository.ConsentTransactionRepository;
import com.ehrbridge.hospital.repository.DoctorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.apache.catalina.connector.Response;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConsentService {

    @Value("${ehrbridge.gateway.host}")
    private String GATEWAY_HOST;

    @Value("${ehrbridge.gateway.consent-request.endpoint}")
    private String GATEWAY_CONSENT_REQ_ENDPOINT;

    @Autowired
    private RestTemplate rest;

    @Autowired
    private HttpHeaders headers;

    private final ConsentObjectHIURepository consentObjectRepository;
    private final ConsentObjectHIPRepository consentObjectHIPRepository;
    private final ConsentTransactionRepository consentTransactionRepository;
    private final DoctorRepository doctorRepository;

    public ResponseEntity<GenerateConsentResponse> generateConsent(GenerateConsentRequest request) throws JSONException, ParseException {
        var consentObject = ConsentObjectHIU.builder()
                .patient_ehrb_id(request.getConsent_object().getEhrbID())
                .hiu_id(request.getConsent_object().getHiuID())
                .hip_id(request.getConsent_object().getHipID())
                .doctor_ehrb_id(request.getConsent_object().getDoctorID())
                .hi_type(Arrays.toString(request.getConsent_object().getHiType()))
                .departments(Arrays.toString(request.getConsent_object().getDepartments()))
                .date_from(new SimpleDateFormat("yyyy-mm-dd").parse(request.getConsent_object().getPermission().getDateRange().getFrom()))
                .date_to(new SimpleDateFormat("yyyy-mm-dd").parse(request.getConsent_object().getPermission().getDateRange().getTo()))
                .validity(new SimpleDateFormat("yyyy-mm-dd").parse(request.getConsent_object().getPermission().getConsent_validity()))
                .build();

        try {
            consentObjectRepository.save(consentObject);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<GenerateConsentResponse>(GenerateConsentResponse.builder().message("Unable to register consent request").build(), HttpStatusCode.valueOf(500));
        }

        ResponseEntity<GenConsentResponse> gatewayResponse = pushConsentRequestToGateway(request);
        if(gatewayResponse == null){
            var consent_transaction = ConsentTransaction.builder()
                        .consent_status("FAILED")
                        .consent_object_id(consentObject)
                        .build();
            consentTransactionRepository.save(consent_transaction);
            String consentRequestId = consent_transaction.getConsent_request_id();
            return new ResponseEntity<>(GenerateConsentResponse.builder().message("Could not send the consent request to gateway").consent_request_id(consentRequestId).build(), HttpStatusCode.valueOf(501));
        }
        System.out.println(gatewayResponse);
        var consent_transaction = ConsentTransaction.builder()
                .consent_status("PENDING")
                .consent_object_id(consentObject)
                .txnID(gatewayResponse.getBody().getTxnID())
                .build();
        try {
            consentTransactionRepository.save(consent_transaction);
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<>(GenerateConsentResponse.builder().message("Could not store txnID").consent_request_id(consent_transaction.getConsent_request_id()).build(), HttpStatusCode.valueOf(501));
        }
        return new ResponseEntity<GenerateConsentResponse>(GenerateConsentResponse.builder().message("Consent Request Processed Successfully").consent_request_id(consent_transaction.getConsent_request_id()).build(), HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<String> hookConsentHIU(HookConsentRequestHIU request) {
        System.out.println(request);
        ConsentTransaction consentTransaction;
        System.out.println("check1");

        try {
            consentTransaction = consentTransactionRepository.findByTxnID(request.getTxnID()).orElseThrow();
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<>("TxnID is invalid", HttpStatusCode.valueOf(403));
        }
        System.out.println("check2");

        consentTransaction.setConsent_status(request.getConsent_status());
        if(request.getConsent_status().equals("GRANTED"))
        {
            consentTransaction.setSigned_consent_object(request.getSigned_consent_object());
        }

        try {
            consentTransactionRepository.save(consentTransaction);
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<String>("Could not updated consent transaction in the DB!", HttpStatusCode.valueOf(500));
        }

        return new ResponseEntity<>("Consent Object Received Successfully at HIU", HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<String> hookConsentHIP(HookConsentRequestHIP request) {

        var consentObjectHIP = ConsentObjectHIP.builder()
                .signed_consent_object(request.getSigned_consent_obj())
                .public_key(request.getPublic_key())
                .txnID(request.getTxnID())
                .build();

        try {
            consentObjectHIPRepository.save(consentObjectHIP);
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<>("Could not save the consent object in HIP", HttpStatusCode.valueOf(500));
        }

        return new ResponseEntity<String>("Consent Object Received Successfully at HIP", HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<GenConsentResponse> pushConsentRequestToGateway(GenerateConsentRequest consent_object){
        final String GATEWAY_REQ_ENDPOINT = GATEWAY_HOST + GATEWAY_CONSENT_REQ_ENDPOINT;
        System.out.println(GATEWAY_REQ_ENDPOINT);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            String jsonConsentObj = ow.writeValueAsString(consent_object);
            HttpEntity<String> requestEntity = new HttpEntity<String>(jsonConsentObj, headers);
            System.out.println(requestEntity.getHeaders());
            ResponseEntity<GenConsentResponse> responseEntity = rest.exchange(GATEWAY_REQ_ENDPOINT, HttpMethod.POST, requestEntity, GenConsentResponse.class);
            System.out.println(responseEntity.getStatusCode());
            if(responseEntity.getStatusCode().value() == 200){
                return responseEntity;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }

    public ResponseEntity<Optional<ConsentTransaction>> getConsentTransaction(String consentObjectID) {
        List<ConsentTransaction> consentTransactions = consentTransactionRepository.findAll();
        ConsentTransaction transaction = null;
        for (ConsentTransaction consentTransaction : consentTransactions) {
            if ((consentTransaction.getConsent_object_id().getConsent_object_id()).equals(consentObjectID)) {
                transaction = consentTransaction;
            }
        }
        Optional<ConsentTransaction> transactionFound = Optional.of(transaction);
        return new ResponseEntity<Optional<ConsentTransaction>>(transactionFound, HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<Optional<ConsentObjectHIU>> getConsentObjectHIU(String consentObjectID) {
        List<ConsentObjectHIU> consentObjects = consentObjectRepository.findAll();
        ConsentObjectHIU consentObjectFound = null;
        for (ConsentObjectHIU consentObject : consentObjects) {
            if (consentObject.getConsent_object_id().equals(consentObjectID)) {
                consentObjectFound = consentObject;
            }
        }
        Optional<ConsentObjectHIU> consentObjectReturn = Optional.of(consentObjectFound);
        return new ResponseEntity<Optional<ConsentObjectHIU>>(consentObjectReturn, HttpStatusCode.valueOf(200));
    }


        public ResponseEntity<FetchConsentReqsResponse> fetchAllConsentReqs(){
        try {
            List<ConsentTransaction> consentReqs = consentTransactionRepository.findAll();
            return new ResponseEntity<FetchConsentReqsResponse>(FetchConsentReqsResponse.builder().consentreqs(consentReqs).build(), HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return new ResponseEntity<FetchConsentReqsResponse>(HttpStatusCode.valueOf(500));
    }

    public ResponseEntity<FetchConsentObjsResponse> fetchConsentsByDoctorID(String doctorID){
        try {
            Optional<Doctor> doctor = doctorRepository.findById(doctorID);
            if(doctor.isPresent()){
                List<ConsentObjectHIU> consentReqs = consentObjectRepository.findByDoctorEhrbID(doctor.get().getDoctorEhrbID());
                return new ResponseEntity<FetchConsentObjsResponse>(FetchConsentObjsResponse.builder().consent_objs(consentReqs).build(), HttpStatusCode.valueOf(200));
            }           
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return new ResponseEntity<FetchConsentObjsResponse>(HttpStatusCode.valueOf(500));

    }

    public ResponseEntity<FetchConsentObjsResponse> fetchConsentsByDoctorEhrbID(String doctorEhrbID){
        try {
            List<ConsentObjectHIU> consentReqs = consentObjectRepository.findByDoctorEhrbID(doctorEhrbID);
            return new ResponseEntity<FetchConsentObjsResponse>(FetchConsentObjsResponse.builder().consent_objs(consentReqs).build(), HttpStatusCode.valueOf(200));          
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return new ResponseEntity<FetchConsentObjsResponse>(HttpStatusCode.valueOf(500));

    }

    public ResponseEntity<FetchConsentTransactionResponse> fetchConsentTransactionsByDoctorID(String doctorID){
        try {
            Optional<Doctor> doctor = doctorRepository.findById(doctorID);
            System.out.println(doctor.get());
            if(doctor.isPresent()){
                List<ConsentObjectHIU> consentReqs = consentObjectRepository.findByDoctorEhrbID(doctor.get().getDoctorEhrbID());
                System.out.print(consentReqs);
                List<String> consentIDs = new ArrayList<>(consentReqs.size());
                for(ConsentObjectHIU consentObj: consentReqs){
                    consentIDs.add(consentObj.getConsent_object_id());
                }
                List<ConsentTransaction> consentTransactions = consentTransactionRepository.findAllByConsentObjectID(consentIDs);
                return new ResponseEntity<FetchConsentTransactionResponse>(FetchConsentTransactionResponse.builder().consentTxns(consentTransactions).build(), HttpStatusCode.valueOf(200));
            } 
        } catch (Exception e) {
            
        }
        return new ResponseEntity<FetchConsentTransactionResponse>(HttpStatusCode.valueOf(500));
    }
}

