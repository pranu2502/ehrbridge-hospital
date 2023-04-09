package com.ehrbridge.hospital.controller;

import java.util.Optional;

import com.ehrbridge.hospital.dto.Doctor.FetchAllDoctorsResponse;
import com.ehrbridge.hospital.dto.auth.patient.PatientRegisterRequest;
import com.ehrbridge.hospital.dto.auth.patient.PatientRegisterResponse;
import com.ehrbridge.hospital.dto.Doctor.FetchDoctorByIDRequest;
import com.ehrbridge.hospital.dto.auth.doctor.LoginRequest;
import com.ehrbridge.hospital.dto.auth.doctor.LoginResponse;
import com.ehrbridge.hospital.dto.auth.doctor.RegisterRequest;
import com.ehrbridge.hospital.dto.auth.doctor.RegisterResponse;
import com.ehrbridge.hospital.service.DoctorAuthService;
import com.ehrbridge.hospital.service.PatientRecordService;
import com.ehrbridge.hospital.service.DoctorFetchService;
import com.ehrbridge.hospital.entity.Doctor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/doctor")
@RequiredArgsConstructor
public class DoctorDetailsContoller {

    @Autowired
    private final DoctorFetchService doctorFetchService;

    @GetMapping("/get-by-id")
    public ResponseEntity<Optional<Doctor>> getDoctorByID(@RequestParam String doctorID) {
        // System.out.println(doctorID);
        return doctorFetchService.getDoctorByID(new FetchDoctorByIDRequest(doctorID));
    }

    @GetMapping("/get-all")
    public ResponseEntity<FetchAllDoctorsResponse> getAllDoctors() {
        return doctorFetchService.getAllDoctors();
    }
}
