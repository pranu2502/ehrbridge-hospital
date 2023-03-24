package com.ehrbridge.hospital.auth.dto.consent;

import com.ehrbridge.hospital.auth.dto.consent.DateRangeConsentObject;
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
