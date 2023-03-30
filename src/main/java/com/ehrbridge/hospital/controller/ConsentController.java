package com.ehrbridge.hospital.controller;

import com.ehrbridge.hospital.dto.consent.GenerateConsentRequest;
import com.ehrbridge.hospital.dto.consent.GenerateConsentResponse;
import com.ehrbridge.hospital.dto.consent.HookConsentRequestHIP;
import com.ehrbridge.hospital.dto.consent.HookConsentRequestHIU;
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
        return ResponseEntity.ok(consentService.generateConsent(request));
    }

    @PostMapping("/recieve-hiu")
    public ResponseEntity<String> hookConsentObjectHIU(@RequestBody HookConsentRequestHIU request)
    {
        return ResponseEntity.ok(consentService.hookConsentHIU(request));
    }

    @PostMapping("/recieve-hip")
    public ResponseEntity<String> hookConsentObjectHIP(@RequestBody HookConsentRequestHIP request)
    {
        return ResponseEntity.ok(consentService.hookConsentHIP(request));
    }

}
