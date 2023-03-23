package com.ehrbridge.hospital.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ehrbridge.hospital.dto.Patient.FetchPatientRequest;
import com.ehrbridge.hospital.dto.Patient.PatientRegisterRequest;
import com.ehrbridge.hospital.dto.Patient.PatientRegisterResponse;
import com.ehrbridge.hospital.entity.Patient;
import com.ehrbridge.hospital.service.Patient.PatientRecordService;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/patient")
@RequiredArgsConstructor
public class PatientController {

    @Autowired
    private final PatientRecordService patientRecordService;
    
    @PostMapping("/register")
    public ResponseEntity<PatientRegisterResponse> registerPatient(@RequestBody PatientRegisterRequest request){
        return ResponseEntity.ok(patientRecordService.RegisterPatient(request));
    }

    @GetMapping("/get-details")
    public ResponseEntity<Optional<Patient>> fetchPatient(@RequestParam String ehrbID){
        Optional<Patient> patient  = patientRecordService.FetchPatient(new FetchPatientRequest(ehrbID));
        return ResponseEntity.ok(patient);
    }

}
