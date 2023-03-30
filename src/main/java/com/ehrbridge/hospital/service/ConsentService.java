package com.ehrbridge.hospital.service;

import com.ehrbridge.hospital.dto.consent.GenerateConsentRequest;
import com.ehrbridge.hospital.dto.consent.GenerateConsentResponse;
import com.ehrbridge.hospital.dto.consent.HookConsentRequestHIP;
import com.ehrbridge.hospital.dto.consent.HookConsentRequestHIU;
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
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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
    private String GATEWAY_URL;

    @Value("${ehrbridge.gateway.consent-request.endpoint}")
    private String GATEWAY_CONSENT_REQ_ENDPOINT;

    @Autowired
    private RestTemplate rest;

    @Autowired
    private HttpHeaders headers;

    private final ConsentObjectHIURepository consentObjectRepository;
    private final ConsentObjectHIPRepository consentObjectHIPRepository;
    private final ConsentTransactionRepository consentTransactionRepository;


    public GenerateConsentResponse generateConsent(GenerateConsentRequest request) throws JSONException, ParseException {
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

        consentObjectRepository.save(consentObject);

        
        //TODO: Call ABDM Server and store the response(txn_id) in the table - POST Request with consent_object as body.
        ResponseEntity<GenConsentResponse> gatewayResponse = pushConsentRequestToGateway(request);
        if(gatewayResponse == null){
            var consent_transaction = ConsentTransaction.builder()
                        .consent_status("PENDING")
                        .consent_object_id(consentObject)
                        .build();
            String consentRequestId = consent_transaction.getConsent_request_id();
            return GenerateConsentResponse.builder().consent_request_id(consentRequestId).message("Could not send consent request to gateway").build();
        }
        var consent_transaction = ConsentTransaction.builder()
                .consent_status("PENDING")
                .consent_object_id(consentObject)
                .txnID(gatewayResponse.getBody().getTxnID())
                .build();
        consentTransactionRepository.save(consent_transaction);
        String consentRequestId = consent_transaction.getConsent_request_id();
        return GenerateConsentResponse.builder().consent_request_id(consentRequestId).message("Consent Generated Successfully").build();
    }

    public String hookConsentHIU(HookConsentRequestHIU request) {
        System.out.println(request);
        ConsentTransaction consentTransaction = consentTransactionRepository.findByTxnID(request.getTxnID()).orElseThrow();

        consentTransaction.setConsent_status(request.getConsent_status());
        if(request.getConsent_status().equals("GRANTED"))
        {
            consentTransaction.setEncrypted_consent_object(request.getEncrypted_consent_obj());
        }
        consentTransactionRepository.save(consentTransaction);

        return "Consent Object Received Successfully at HIU";
    }

    public String hookConsentHIP(HookConsentRequestHIP request) {
        var consentObjectHIP = ConsentObjectHIP.builder()
                .encrypted_consent_object(request.getEncrypted_consent_obj())
                .public_key(request.getPublic_key())
                .txnID(request.getTxnID())
                .build();
        consentObjectHIPRepository.save(consentObjectHIP);

        return "Consent Object Received Successfully at HIP";
    }

    public ResponseEntity<GenConsentResponse> pushConsentRequestToGateway(GenerateConsentRequest consent_object){
        final String GATEWAY_REQ_ENDPOINT = GATEWAY_URL + GATEWAY_CONSENT_REQ_ENDPOINT;
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
