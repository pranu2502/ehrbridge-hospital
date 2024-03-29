package com.ehrbridge.hospital.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ehrbridge.hospital.dto.hospital.FetchAllHospitalResponse;
import com.ehrbridge.hospital.dto.hospital.Hospital;
import com.ehrbridge.hospital.dto.hospital.PatientServerHospitalsResponse;
import com.ehrbridge.hospital.service.HospitalService;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/hospitals")
@RequiredArgsConstructor
public class HospitalController {
    @Autowired
    private HospitalService hospitalService;

    @GetMapping("/fetch-all")
    public ResponseEntity<PatientServerHospitalsResponse> fetchAllHospitals(@RequestParam String ehrbID){
        System.out.println("yayyy");
        return hospitalService.fetchHospitals(ehrbID);
    }

    @GetMapping("/fetch-all-hospitals")
    public ResponseEntity<FetchAllHospitalResponse> fetchAllHospitals(){
        System.out.println("yayyy");
        return hospitalService.fetchAllHospitals();
    }
    
    @GetMapping("/fetch")
    public ResponseEntity<Hospital> fetchHospital(@RequestParam String hospitalID){
        return hospitalService.fetchHospital(hospitalID);
    }

    
}
