package com.ehrbridge.hospital.service;

import com.ehrbridge.hospital.dto.consent.GenerateConsent.GenerateConsentRequest;
import com.ehrbridge.hospital.dto.consent.GenerateConsent.GenerateConsentResponse;
import com.ehrbridge.hospital.dto.consent.HookConsent.HookConsentRequestHIP;
import com.ehrbridge.hospital.dto.consent.HookConsent.HookConsentRequestHIU;
import com.ehrbridge.hospital.dto.gateway.GenConsentResponse;
import com.ehrbridge.hospital.entity.ConsentObjectHIP;
import com.ehrbridge.hospital.entity.ConsentObjectHIU;
import com.ehrbridge.hospital.entity.ConsentTransaction;
import com.ehrbridge.hospital.repository.ConsentObjectHIPRepository;
import com.ehrbridge.hospital.repository.ConsentObjectHIURepository;
import com.ehrbridge.hospital.repository.ConsentTransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import lombok.RequiredArgsConstructor;
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
import java.util.Arrays;

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


    public ResponseEntity<GenerateConsentResponse> generateConsent(GenerateConsentRequest request) throws JSONException, ParseException {
        var consentObject = ConsentObjectHIU.builder()
                .patient_ehbr_id(request.getConsent_object().getEhrbID())
                .hiu_id(request.getConsent_object().getHiuID())
                .hip_id(request.getConsent_object().getHipID())
                .doctor_ehbr_id(request.getConsent_object().getDoctorID())
                .hi_type(Arrays.toString(request.getConsent_object().getHiType()))
                .departments(Arrays.toString(request.getConsent_object().getDepartments()))
                .date_from(new SimpleDateFormat("yyyy-mm-dd").parse(request.getConsent_object().getPermission().getDateRange().getFrom()))
                .date_to(new SimpleDateFormat("yyyy-mm-dd").parse(request.getConsent_object().getPermission().getDateRange().getTo()))
                .validity(new SimpleDateFormat("yyyy-mm-dd").parse(request.getConsent_object().getPermission().getConsent_validity()))
                .build();

        try {
            consentObjectRepository.save(consentObject);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return new ResponseEntity<GenerateConsentResponse>(GenerateConsentResponse.builder().message("Unable to register consent request").build(), HttpStatusCode.valueOf(500));
        }

        ResponseEntity<GenConsentResponse> gatewayResponse = pushConsentRequestToGateway(request);
        if(gatewayResponse == null){
            var consent_transaction = ConsentTransaction.builder()
                        .consent_status("FAILED")
                        .consent_object_id(consentObject)
                        .build();
            String consentRequestId = consent_transaction.getConsent_request_id();
            return new ResponseEntity<GenerateConsentResponse>(GenerateConsentResponse.builder().message("Could not send the consent request to gateway").consent_request_id(consentRequestId).build(), HttpStatusCode.valueOf(501));
        }

        var consent_transaction = ConsentTransaction.builder()
                .consent_status("PENDING")
                .consent_object_id(consentObject)
                .txnID(gatewayResponse.getBody().getTxnID())
                .build();
        try {
            consentTransactionRepository.save(consent_transaction);
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<GenerateConsentResponse>(GenerateConsentResponse.builder().message("Could not store txnID").consent_request_id(consent_transaction.getConsent_request_id()).build(), HttpStatusCode.valueOf(501));
        }
        return new ResponseEntity<GenerateConsentResponse>(GenerateConsentResponse.builder().message("Consent Request Processed Successfully").consent_request_id(consent_transaction.getConsent_request_id()).build(), HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<String> hookConsentHIU(HookConsentRequestHIU request) {
        ConsentTransaction consentTransaction;
        try {
            consentTransaction = consentTransactionRepository.findByTxnID(request.getTxnID()).orElseThrow();
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<String>("TxnID is invalid", HttpStatusCode.valueOf(403));
        }

        consentTransaction.setConsent_status(request.getConsent_status());
        if(request.getConsent_status().equals("GRANTED"))
        {
            consentTransaction.setEncrypted_consent_object(request.getEncrypted_consent_obj());
        }

        try {
            consentTransactionRepository.save(consentTransaction);
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<String>("Could not updated consent transaction in the DB!", HttpStatusCode.valueOf(500));
        }

        return new ResponseEntity<String>("Consent Object Received Successfully at HIU", HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<String> hookConsentHIP(HookConsentRequestHIP request) {
        var consentObjectHIP = ConsentObjectHIP.builder()
                .encrypted_consent_object(request.getEncrypted_consent_obj())
                .public_key(request.getPublic_key())
                .txnID(request.getTxnID())
                .build();

        try {
            consentObjectHIPRepository.save(consentObjectHIP);
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<String>("Could not save the consent object in HIP", HttpStatusCode.valueOf(500));
        }

        return new ResponseEntity<String>("Consent Object Received Successfully at HIP", HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<GenConsentResponse> pushConsentRequestToGateway(GenerateConsentRequest consent_object){
        final String GATEWAY_REQ_ENDPOINT = GATEWAY_HOST + GATEWAY_CONSENT_REQ_ENDPOINT;
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            String jsonConsentObj = ow.writeValueAsString(consent_object);
            HttpEntity<String> requestEntity = new HttpEntity<String>(jsonConsentObj, headers);
            ResponseEntity<GenConsentResponse> responseEntity = rest.exchange(GATEWAY_REQ_ENDPOINT, HttpMethod.POST, requestEntity, GenConsentResponse.class);
            if(responseEntity.getStatusCode().value() == 200){
                return responseEntity;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }
}
