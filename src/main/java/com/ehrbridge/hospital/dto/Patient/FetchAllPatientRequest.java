package com.ehrbridge.hospital.dto.Patient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FetchAllPatientRequest {
    private String patientID;
}
