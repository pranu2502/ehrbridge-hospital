package com.ehrbridge.hospital.service;

import java.util.Date;
import java.util.Optional;

import com.ehrbridge.hospital.dto.Patient.*;
import com.ehrbridge.hospital.entity.Doctor;
import com.ehrbridge.hospital.entity.PatientRecords;
import com.ehrbridge.hospital.repository.DoctorRepository;
import com.ehrbridge.hospital.repository.PatientRecordsRepository;
import org.springframework.stereotype.Service;

import com.ehrbridge.hospital.entity.Patient;
import com.ehrbridge.hospital.repository.PatientRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PatientRecordService {
    
    private final PatientRepository patientRepository;
    private final PatientRecordsRepository patientRecordsRepository;
    private final DoctorRepository doctorRepository;

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
        
        return PatientRegisterResponse.builder().msg("Patient Registration Successful!").patientID(patient.getId()).build();
    }

    public Optional<Patient> FetchPatient(FetchPatientRequest request){
        Optional<Patient> patient = patientRepository.findById(request.getEhrbID());
        return patient;
    }

    public AddPatientRecordResponse addRecord(AddPatientRecordRequest request) {
        var patientID = request.getPatientID();
        Patient patient = patientRepository.findById(patientID).orElseThrow();
        Doctor doctor = doctorRepository.findById(request.getDoctorID()).orElseThrow();
        System.out.println("check");

        var patientRecord  = PatientRecords.builder()
                .bp(request.getMetaData().getBp())
                .height(request.getMetaData().getHeight())
                .weight(request.getMetaData().getWeight())
                .problems(request.getData().getProblems())
                .diagnosis(request.getData().getDiagnosis())
                .prescription(request.getData().getPrescription())
                .hiType(request.getHiType())
                .department(request.getDepartment())
                .timeStamp(String.valueOf(new Date()))
                .doctorID(request.getDoctorID())
                .patientID(request.getPatientID())
                .build();

        patientRecordsRepository.save(patientRecord);

        return AddPatientRecordResponse.builder().message("Record Added Successfully").build();

    }
}
