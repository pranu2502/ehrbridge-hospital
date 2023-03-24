package com.ehrbridge.hospital.dto.Patient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FetchPatientResponse {
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneString;
    private String gender;
    private String address;
    private String ehrbID;
}
