package com.ehrbridge.hospital.auth.controller;

import com.ehrbridge.hospital.auth.dto.*;
import com.ehrbridge.hospital.auth.service.DoctorAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
}
