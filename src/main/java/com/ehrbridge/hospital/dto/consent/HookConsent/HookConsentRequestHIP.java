package com.ehrbridge.hospital.dto.consent.HookConsent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HookConsentRequestHIP {

    private String txnID;
    private String public_key;
    private String encrypted_consent_obj;
}
