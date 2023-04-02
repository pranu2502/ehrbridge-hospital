package com.ehrbridge.hospital.dto.Doctor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FetchDoctorByIDRequest {
    private String doctorID;
    
}
