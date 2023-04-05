package com.ehrbridge.hospital.dto.Patient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class GetPatientRecordRequest {
    private String patientIDString; 
    
}
