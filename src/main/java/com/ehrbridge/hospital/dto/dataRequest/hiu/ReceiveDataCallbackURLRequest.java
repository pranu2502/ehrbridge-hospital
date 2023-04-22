package com.ehrbridge.hospital.dto.dataRequest.hiu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import com.ehrbridge.hospital.entity.PatientRecords;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReceiveDataCallbackURLRequest {
    private List<PatientRecords> patientRecords;
    private String ehrbID;
}
