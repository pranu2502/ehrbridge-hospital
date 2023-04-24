package com.ehrbridge.hospital.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ehrbridge.hospital.dto.hospital.FetchAllHospitalResponse;
import com.ehrbridge.hospital.dto.hospital.Hospital;
import com.ehrbridge.hospital.service.HospitalService;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/hospitals")
public class HospitalController {
    @Autowired
    private HospitalService hospitalService;

    @GetMapping("/fetch-all")
    public ResponseEntity<FetchAllHospitalResponse> fetchAllHospitals(){
        return hospitalService.fetchHospitals();
    }
    
    @GetMapping("/fetch")
    public ResponseEntity<Hospital> fetchHospital(@RequestParam String hospitalID){
        return hospitalService.fetchHospital(hospitalID);
    }

    
}
