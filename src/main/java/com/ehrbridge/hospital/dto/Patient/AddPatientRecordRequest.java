package com.ehrbridge.hospital.dto.Patient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddPatientRecordRequest {
    private String patientID;
    private String doctorID;

    private PatientRecordRequestMetadata metaData;
//    private String height;
//    private String weight;
//    private String bp;
    private String department;
    private String hiType;
    private PatientRecordRequestData data;
//    private String problems;
//    private String diagnosis;
//    private String prescription;
}
