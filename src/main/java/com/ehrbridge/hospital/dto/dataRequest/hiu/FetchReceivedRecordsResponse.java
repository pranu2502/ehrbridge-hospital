package com.ehrbridge.hospital.dto.dataRequest.hiu;

import java.util.List;

import com.ehrbridge.hospital.entity.ReceivedPatientRecords;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FetchReceivedRecordsResponse {
    private List<ReceivedPatientRecords> patientData;
}
