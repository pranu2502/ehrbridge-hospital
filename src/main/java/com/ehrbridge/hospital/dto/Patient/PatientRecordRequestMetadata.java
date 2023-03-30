package com.ehrbridge.hospital.dto.Patient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientRecordRequestMetadata {
    private String height;
    private String weight;
    private String bp;
    private String heartRate;
}
