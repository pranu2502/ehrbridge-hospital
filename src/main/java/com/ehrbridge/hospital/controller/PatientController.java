package com.ehrbridge.hospital.controller;

import java.util.Optional;

import com.ehrbridge.hospital.dto.Patient.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ehrbridge.hospital.entity.Patient;
import com.ehrbridge.hospital.service.PatientRecordService;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/patient")
@RequiredArgsConstructor
public class PatientController {

    @Autowired
    private final PatientRecordService patientRecordService;
    


    @PostMapping("/add-record")
    public ResponseEntity<AddPatientRecordResponse> addPatientDetails(@RequestBody AddPatientRecordRequest request){
        return ResponseEntity.ok(patientRecordService.addRecord(request));
    }

    @GetMapping("/get-details")
    public ResponseEntity<Optional<Patient>> fetchPatient(@RequestParam String ehrbID){
        Optional<Patient> patient  = patientRecordService.FetchPatient(new FetchPatientRequest(ehrbID));
        return ResponseEntity.ok(patient);
    }

}
