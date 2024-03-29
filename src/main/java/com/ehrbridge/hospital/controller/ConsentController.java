package com.ehrbridge.hospital.controller;

import com.ehrbridge.hospital.dto.consent.FetchConsentObjsResponse;
import com.ehrbridge.hospital.dto.consent.FetchConsentReqsResponse;
import com.ehrbridge.hospital.dto.consent.FetchConsentTransactionResponse;
import com.ehrbridge.hospital.dto.consent.FetchConsentsHipResponse;
import com.ehrbridge.hospital.dto.consent.GenerateConsent.GenerateConsentRequest;
import com.ehrbridge.hospital.dto.consent.GenerateConsent.GenerateConsentResponse;
import com.ehrbridge.hospital.dto.consent.HookConsent.HookConsentRequestHIP;
import com.ehrbridge.hospital.dto.consent.HookConsent.HookConsentRequestHIU;
import java.util.Optional;

import com.ehrbridge.hospital.entity.ConsentObjectHIU;
import com.ehrbridge.hospital.entity.ConsentTransaction;
import com.ehrbridge.hospital.service.ConsentService;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/consent")
@RequiredArgsConstructor
public class ConsentController {
    @Autowired
    private final ConsentService consentService;

    @PostMapping("/generate")
    public ResponseEntity<GenerateConsentResponse> generateConsent(@RequestBody GenerateConsentRequest request) throws JSONException, ParseException { 
        return consentService.generateConsent(request);
    }

    @PostMapping("/recieve-hiu")
    public ResponseEntity<String> hookConsentObjectHIU(@RequestBody HookConsentRequestHIU request)
    {
        return consentService.hookConsentHIU(request);
    }

    @PostMapping("/recieve-hip")
    public ResponseEntity<String> hookConsentObjectHIP(@RequestBody HookConsentRequestHIP request)
    {
        return consentService.hookConsentHIP(request);
    }


    @GetMapping("/consent-transaction")
    public ResponseEntity<Optional<ConsentTransaction>> getConsentTransactions(@RequestParam String consentID) {
        return consentService.getConsentTransaction(consentID);
    }

    @GetMapping("/consent-object")
    public ResponseEntity<Optional<ConsentObjectHIU>> getConsentObjectHIU(@RequestParam String consentID) {
        return consentService.getConsentObjectHIU(consentID);
    }


    @GetMapping("/fetch-transactions-hiu")
    public ResponseEntity<FetchConsentReqsResponse> fetchAllConsentReqs(){
        return consentService.fetchAllConsentReqs();
    }

    @GetMapping("/fetch-id")
    public ResponseEntity<FetchConsentObjsResponse> fetchAllConsentObjsByDoctorID(@RequestParam String doctorID){
        return consentService.fetchConsentsByDoctorID(doctorID);
    }

    @GetMapping("/fetch-ehrb-id")
    public ResponseEntity<FetchConsentObjsResponse> fetchAllConsentObjsByDoctorEhrbID(@RequestParam String doctorEhrbID){
        return consentService.fetchConsentsByDoctorEhrbID(doctorEhrbID);
    }

    @GetMapping("/consent-transaction-by-doctor")
    public ResponseEntity<FetchConsentTransactionResponse> fetchConsentTransactionByDoctorID(@RequestParam String doctorID){
        return consentService.fetchConsentTransactionsByDoctorID(doctorID);
    }


    @GetMapping("/fetch-consents-hip")
        public ResponseEntity<FetchConsentsHipResponse> fetchAllConsentsHIP(){
        return consentService.fetchConsentsHIP();
    }

    @GetMapping("/fetch-consents-hiu")
    public ResponseEntity<FetchConsentObjsResponse> fetchAllConsentsHIU(){
        return consentService.fetchConsentsHIU();
    }
}
