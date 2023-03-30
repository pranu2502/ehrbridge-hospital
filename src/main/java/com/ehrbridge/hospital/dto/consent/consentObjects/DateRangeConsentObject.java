package com.ehrbridge.hospital.dto.consent.consentObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DateRangeConsentObject {
    private String from;
    private String to;
}
