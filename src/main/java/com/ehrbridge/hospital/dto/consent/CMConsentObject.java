package com.ehrbridge.hospital.dto.consent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

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
    public ConsentPermission permission;
    public String consentStatus;
}
