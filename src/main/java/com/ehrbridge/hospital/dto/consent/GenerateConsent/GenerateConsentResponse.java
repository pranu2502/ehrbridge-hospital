package com.ehrbridge.hospital.dto.consent.GenerateConsent;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenerateConsentResponse {
    private String consent_request_id;
    private String message;
}
