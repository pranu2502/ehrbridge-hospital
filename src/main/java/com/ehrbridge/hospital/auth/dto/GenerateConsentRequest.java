package com.ehrbridge.hospital.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class GenerateConsentRequest {
    private ConsentObjectRequest consentObject;
}
