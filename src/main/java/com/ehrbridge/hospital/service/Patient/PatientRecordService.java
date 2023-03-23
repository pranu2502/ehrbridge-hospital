package com.ehrbridge.hospital.service.Patient;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ehrbridge.hospital.dto.Patient.FetchPatientRequest;
import com.ehrbridge.hospital.dto.Patient.FetchPatientResponse;
import com.ehrbridge.hospital.dto.Patient.PatientRegisterRequest;
import com.ehrbridge.hospital.dto.Patient.PatientRegisterResponse;
import com.ehrbridge.hospital.entity.Patient;
import com.ehrbridge.hospital.repository.PatientRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PatientRecordService {
    
    private final PatientRepository patientRepository;

    public PatientRegisterResponse RegisterPatient(PatientRegisterRequest request){
        var patient = Patient.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .emailAddress(request.getEmailAddress())
                .phoneString(request.getPhoneString())
                .address(request.getAddress())
                .gender(request.getGender())
                .ehrbID(request.getEhrbID())
                .build();

        try {
            patientRepository.save(patient);
        } catch (Exception e) {
            // TODO: handle exception
        }
        
        return PatientRegisterResponse.builder().msg("Patient Registration Successful!").build();
    }

    public Optional<Patient> FetchPatient(FetchPatientRequest request){
        Optional<Patient> patient = patientRepository.findById(request.getEhrbID());
        return patient;
    }
}
