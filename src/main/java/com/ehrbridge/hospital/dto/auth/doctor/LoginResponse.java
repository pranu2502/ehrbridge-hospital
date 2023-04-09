package com.ehrbridge.hospital.dto.auth.doctor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String token;
    private String doctorID;
    private String message;
}
