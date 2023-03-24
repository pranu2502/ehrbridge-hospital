package com.ehrbridge.hospital.dto.consent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class GenerateConsentResponse {
    private Long consent_request_id;
    private String message;

}
