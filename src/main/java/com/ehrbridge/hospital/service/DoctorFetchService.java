package com.ehrbridge.hospital.service;


import java.util.Optional;
import java.util.ArrayList;


import com.ehrbridge.hospital.dto.Doctor.*;
import com.ehrbridge.hospital.entity.Doctor;
import com.ehrbridge.hospital.repository.DoctorRepository;
import java.util.List;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DoctorFetchService {

    private final DoctorRepository doctorRepository;

    public ResponseEntity<Optional<Doctor>> getDoctorByID(FetchDoctorByIDRequest request) {

        Optional<Doctor> doctor = null;
        try {
            doctor = doctorRepository.findById(request.getDoctorID());

        } catch (Exception e) {
            return new ResponseEntity<Optional<Doctor>>(doctor, HttpStatusCode.valueOf(403));
        }
        return new ResponseEntity<Optional<Doctor>>(doctor, HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<FetchAllDoctorsResponse> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        return new ResponseEntity<FetchAllDoctorsResponse>(FetchAllDoctorsResponse.builder().doctors(doctors).build(), HttpStatusCode.valueOf(200));

    }

    
    
}
