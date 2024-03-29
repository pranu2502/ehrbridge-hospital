package com.ehrbridge.hospital.dto.auth.patient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientRegisterResponse {
    private String msg;
    private String patientID;
}
