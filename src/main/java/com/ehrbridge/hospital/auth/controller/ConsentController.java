package com.ehrbridge.hospital.auth.controller;

import com.ehrbridge.hospital.auth.dto.consent.GenerateConsentRequest;
import com.ehrbridge.hospital.auth.dto.consent.GenerateConsentResponse;
import com.ehrbridge.hospital.auth.service.ConsentService;
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

    @PostMapping( "/generate")
    public ResponseEntity<GenerateConsentResponse> generateConsent(@RequestBody GenerateConsentRequest request) throws JSONException, ParseException {
        return ResponseEntity.ok(consentService.generateConsent(request));
    }

}
