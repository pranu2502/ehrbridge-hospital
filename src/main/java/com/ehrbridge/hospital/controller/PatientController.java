package com.ehrbridge.hospital.controller;

import java.util.Optional;

import com.ehrbridge.hospital.dto.Patient.*;
import com.ehrbridge.hospital.entity.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

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
        return patientRecordService.addRecord(request);
    }

    @GetMapping("/get-details")
    public ResponseEntity<Patient> fetchPatient(@RequestParam String patientID){
       System.out.print(patientID);
       return patientRecordService.FetchPatient(patientID);
    }

    @GetMapping("/get-all-patients")
    public ResponseEntity<FetchAllPatientsResponse> fetchPatientsByPatientID(){
        return patientRecordService.fetchPatients();
    }
    
    @GetMapping("/get-records-by-id")
    public ResponseEntity<List<PatientRecords>> fetchPatientRecordsByID(@RequestParam String patientID) {
        return patientRecordService.getPatientRecordsByID(new GetPatientRecordRequest(patientID));
    }

}
