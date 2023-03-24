package com.ehrbridge.hospital.auth.controller;

import com.ehrbridge.hospital.auth.dto.auth.LoginRequest;
import com.ehrbridge.hospital.auth.dto.auth.LoginResponse;
import com.ehrbridge.hospital.auth.dto.auth.RegisterRequest;
import com.ehrbridge.hospital.auth.dto.auth.RegisterResponse;
import com.ehrbridge.hospital.auth.service.DoctorAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class DoctorController {
    @Autowired
    private final DoctorAuthService doctorAuthService;

    @PostMapping("/register/doctor")
    public ResponseEntity<RegisterResponse> registerDoctor(@RequestBody RegisterRequest request)
    {
        return ResponseEntity.ok(doctorAuthService.register(request));
    }

    @PostMapping("/login/doctor")
    public ResponseEntity<LoginResponse> loginDoctor(@RequestBody LoginRequest request)
    {
        System.out.println(request);
        LoginResponse loginResponse = doctorAuthService.login(request);
        if(loginResponse.getMessage().equals("Login Successful"))
        {
            return ResponseEntity.ok(loginResponse);
        }
        return new ResponseEntity<LoginResponse>(loginResponse, HttpStatusCode.valueOf(403) );
    }


}
