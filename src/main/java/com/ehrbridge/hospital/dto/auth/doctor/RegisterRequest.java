package com.ehrbridge.hospital.dto.auth.doctor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String password;
    private String phoneString;
    private String gender;
    private String address;
    private String Department;
    private String ehrbID;

}
