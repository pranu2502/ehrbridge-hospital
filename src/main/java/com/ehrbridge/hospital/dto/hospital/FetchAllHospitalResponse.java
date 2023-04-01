package com.ehrbridge.hospital.dto.hospital;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FetchAllHospitalResponse {
    private List<Hospital> hospitals;
}
