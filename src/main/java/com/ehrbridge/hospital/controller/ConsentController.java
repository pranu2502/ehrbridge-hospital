package com.ehrbridge.hospital.controller;

import com.ehrbridge.hospital.dto.consent.GenerateConsent.GenerateConsentRequest;
import com.ehrbridge.hospital.dto.consent.GenerateConsent.GenerateConsentResponse;
import com.ehrbridge.hospital.dto.consent.HookConsent.HookConsentRequestHIP;
import com.ehrbridge.hospital.dto.consent.HookConsent.HookConsentRequestHIU;
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

}
