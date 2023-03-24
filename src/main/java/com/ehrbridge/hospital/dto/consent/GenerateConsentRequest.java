package com.ehrbridge.hospital.dto.consent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class GenerateConsentRequest {
    private ConsentObjectRequest consent_object;
}
