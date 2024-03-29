package com.ehrbridge.hospital.controller;


import com.ehrbridge.hospital.dto.auth.patient.PatientRegisterRequest;
import com.ehrbridge.hospital.dto.auth.patient.PatientRegisterResponse;
import com.ehrbridge.hospital.dto.auth.doctor.LoginRequest;
import com.ehrbridge.hospital.dto.auth.doctor.LoginResponse;
import com.ehrbridge.hospital.dto.auth.doctor.RegisterRequest;
import com.ehrbridge.hospital.dto.auth.doctor.RegisterResponse;
import com.ehrbridge.hospital.service.DoctorAuthService;
import com.ehrbridge.hospital.service.PatientRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    private final DoctorAuthService doctorAuthService;

    @Autowired
    private final PatientRecordService patientRecordService;

    @PostMapping("/register/doctor")
    public ResponseEntity<RegisterResponse> registerDoctor(@RequestBody RegisterRequest request)
    {
        return doctorAuthService.register(request);
    }

    @PostMapping("/login/doctor")
    public ResponseEntity<LoginResponse> loginDoctor(@RequestBody LoginRequest request)
    {
        return doctorAuthService.login(request);
    }

    @PostMapping("/register/patient")
    public ResponseEntity<PatientRegisterResponse> registerPatient(@RequestBody PatientRegisterRequest request){
        return patientRecordService.RegisterPatient(request);
    }

}
