package com.ehrbridge.hospital.dto.consent.GenerateConsent;

import com.ehrbridge.hospital.dto.consent.consentObjects.ConsentObjectRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class GenerateConsentRequest {
    private ConsentObjectRequest consent_object;
}
