package com.ehrbridge.hospital.dto.consent.consentObjects;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DateRangeConsentObjectCM {
    private Date from;
    private Date to;
}
