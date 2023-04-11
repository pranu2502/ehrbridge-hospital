package com.ehrbridge.hospital.dto.consent;

import java.util.UUID;

import com.ehrbridge.hospital.dto.consent.consentObjects.PermissionConsentObject;
import com.ehrbridge.hospital.dto.consent.consentObjects.PermissionConsentObjectCM;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CMConsentObject {
    public UUID consentID;
    public UUID ehrbID;
    public UUID hiuID;
    public UUID hipID;
    public UUID doctorID;
    public String[] hiType;
    public String[] departments;
    public PermissionConsentObjectCM permission;
    public String consent_status;
}
