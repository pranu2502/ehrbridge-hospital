package com.ehrbridge.hospital.dto.consent;

import com.ehrbridge.hospital.dto.consent.consentObjects.PermissionConsentObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CMConsentObject {
    public String consentID;
    public String ehbrID;
    public String hiuID;
    public String hipID;
    public String doctorID;
    public String[] hiType;
    public String[] departments;
    public String consentDescription;
    public PermissionConsentObject permission;
    public String consentStatus;
}
