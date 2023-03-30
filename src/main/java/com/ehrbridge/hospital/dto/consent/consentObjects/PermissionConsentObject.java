package com.ehrbridge.hospital.dto.consent.consentObjects;

import com.ehrbridge.hospital.dto.consent.consentObjects.DateRangeConsentObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionConsentObject {
    private DateRangeConsentObject dateRange;
    private String consent_validity;
}
