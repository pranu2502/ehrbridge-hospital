package com.ehrbridge.hospital.service;

import java.util.Date;
import java.util.Optional;

import com.ehrbridge.hospital.dto.Patient.*;
import com.ehrbridge.hospital.dto.auth.patient.PatientRegisterRequest;
import com.ehrbridge.hospital.dto.auth.patient.PatientRegisterResponse;
import com.ehrbridge.hospital.entity.Doctor;
import com.ehrbridge.hospital.entity.PatientRecords;
import com.ehrbridge.hospital.repository.DoctorRepository;
import com.ehrbridge.hospital.repository.PatientRecordsRepository;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<PatientRegisterResponse> RegisterPatient(PatientRegisterRequest request){
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
            return new ResponseEntity<PatientRegisterResponse>(PatientRegisterResponse.builder().msg("Patient already exists!").build(), HttpStatusCode.valueOf(403));
        }

        return new ResponseEntity<PatientRegisterResponse>(PatientRegisterResponse.builder().msg("Patient registered successfully!").patientID(patient.getId()).build(),  HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<Optional<Patient>> FetchPatient(FetchPatientRequest request){
        Optional<Patient> patient = null;
        try {
            patient = patientRepository.findById(request.getEhrbID());   
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<Optional<Patient>>(patient, HttpStatusCode.valueOf(403));
        }
        return new ResponseEntity<Optional<Patient>>(patient, HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<AddPatientRecordResponse> addRecord(AddPatientRecordRequest request) {
        var patientID = request.getPatientID();
        try {
            Patient patient = patientRepository.findById(patientID).orElseThrow();
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<AddPatientRecordResponse>(AddPatientRecordResponse.builder().message("could not find patient, with the provided patientID!").build(), HttpStatusCode.valueOf(403));
        }
        
        try{
            Doctor doctor = doctorRepository.findById(request.getDoctorID()).orElseThrow();
        }catch (Exception e){
            return new ResponseEntity<AddPatientRecordResponse>(AddPatientRecordResponse.builder().message("could not find pdoctor, with the provided doctorID!").build(), HttpStatusCode.valueOf(403));
        }

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

        try {
            patientRecordsRepository.save(patientRecord);            
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<AddPatientRecordResponse>(AddPatientRecordResponse.builder().message("Could not save the patient record to the database").build(), HttpStatusCode.valueOf(500));
        }

        return new ResponseEntity<AddPatientRecordResponse>(AddPatientRecordResponse.builder().message("Record Added Successfully").build(), HttpStatusCode.valueOf(200));
    }
}
