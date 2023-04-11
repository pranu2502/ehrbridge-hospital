package com.ehrbridge.hospital.dto.consent.consentObjects;
import java.util.Date;

import com.ehrbridge.hospital.dto.consent.consentObjects.DateRangeConsentObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionConsentObjectCM {
    private DateRangeConsentObjectCM dateRange;
    private Date consent_validity;
}
