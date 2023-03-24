package com.ehrbridge.hospital.auth.controller;

import com.ehrbridge.hospital.auth.dto.GenerateConsentRequest;
import com.ehrbridge.hospital.auth.dto.GenerateConsentResponse;
import com.ehrbridge.hospital.auth.service.ConsentService;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/api/v1/consent")
@CrossOrigin
@RequiredArgsConstructor
public class ConsentController {

    private final ConsentService consentService;

    @PostMapping("/generate")
    public ResponseEntity<GenerateConsentResponse> generateConsent(GenerateConsentRequest request) throws JSONException, ParseException {
        System.out.println("checkkkk");
        return ResponseEntity.ok(consentService.generateConsent(request));
    }

}
