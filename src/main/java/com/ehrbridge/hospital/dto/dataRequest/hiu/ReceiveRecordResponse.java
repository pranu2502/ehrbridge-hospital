package com.ehrbridge.hospital.dto.dataRequest.hiu;

import com.ehrbridge.hospital.entity.ReceivedPatientRecords;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ReceiveRecordResponse
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReceiveRecordResponse {
    private ReceivedPatientRecords patientData;
}