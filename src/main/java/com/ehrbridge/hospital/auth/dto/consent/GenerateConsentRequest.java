package com.ehrbridge.hospital.auth.dto.consent;

import com.ehrbridge.hospital.auth.dto.consent.ConsentObjectRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class GenerateConsentRequest {
    private ConsentObjectRequest consent_object;
}
