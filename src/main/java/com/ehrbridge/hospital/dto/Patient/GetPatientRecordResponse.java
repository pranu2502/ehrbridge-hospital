package com.ehrbridge.hospital.dto.Patient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import com.ehrbridge.hospital.entity.PatientRecords;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class GetPatientRecordResponse {
    private List<PatientRecords> records;
}