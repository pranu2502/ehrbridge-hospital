package com.ehrbridge.hospital.dto.Patient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientRegisterRequest {
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneString;
    private String gender;
    private String address;
    private String ehrbID;
}
