package com.ehrbridge.hospital.dto.hospital;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientServerHospitalsResponse {
    private List<Discovery> hospitals; 
}