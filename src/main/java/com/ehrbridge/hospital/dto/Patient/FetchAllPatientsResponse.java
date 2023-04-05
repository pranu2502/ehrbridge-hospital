package com.ehrbridge.hospital.dto.Patient;

import java.util.List;

import com.ehrbridge.hospital.entity.Patient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FetchAllPatientsResponse {
    private List<Patient> patients;
}
