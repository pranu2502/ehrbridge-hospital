package com.ehrbridge.hospital.dto.consent.HookConsent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HookConsentRequestHIU {
    private String txnID;
    private String consent_status;
    private String encrypted_consent_obj;
}
