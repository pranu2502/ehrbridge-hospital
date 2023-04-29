package com.ehrbridge.hospital.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.ehrbridge.hospital.dto.Patient.*;
import com.ehrbridge.hospital.dto.auth.patient.PatientRegisterRequest;
import com.ehrbridge.hospital.dto.auth.patient.PatientRegisterResponse;
import com.ehrbridge.hospital.entity.Doctor;
import com.ehrbridge.hospital.entity.PatientRecords;
import com.ehrbridge.hospital.dto.Patient.GetPatientRecordRequest;
import com.ehrbridge.hospital.dto.Patient.GetPatientRecordResponse;
import com.ehrbridge.hospital.repository.DoctorRepository;
import com.ehrbridge.hospital.repository.PatientRecordsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ehrbridge.hospital.entity.Patient;
import com.ehrbridge.hospital.repository.PatientRepository;
import java.util.List;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;


@Service
@RequiredArgsConstructor
public class PatientRecordService {
    
    private final PatientRepository patientRepository;
    private final PatientRecordsRepository patientRecordsRepository;
    private final DoctorRepository doctorRepository;

    @Autowired
    private RestTemplate rest;

    @Autowired
    private HttpHeaders headers;

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

    public ResponseEntity<Patient> FetchPatient(String patientID){
        try {
           Optional<Patient> patient = patientRepository.findById(patientID);
           //System.out.print(patient.isPresent());
            return new ResponseEntity<Patient>(patient.get(), HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            // TODO: handle exception
            
        }
        return new ResponseEntity<Patient>(HttpStatusCode.valueOf(403));
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
        final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

        
        var patientRecord  = PatientRecords.builder()
                .bp(request.getMetaData().getBp())
                .height(request.getMetaData().getHeight())
                .weight(request.getMetaData().getWeight())
                .heartRate(request.getMetaData().getHeartRate())
                .problems(request.getData().getProblems())
                .diagnosis(request.getData().getDiagnosis())
                .prescription(request.getData().getPrescription())
                .hiType(request.getHiType())
                .department(request.getDepartment())
                .doctorID(request.getDoctorID())
                .patientID(request.getPatientID())
                .timeStamp(new Date())
                .build();

        try {
            patientRecordsRepository.save(patientRecord);            
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<AddPatientRecordResponse>(AddPatientRecordResponse.builder().message("Could not save the patient record to the database").build(), HttpStatusCode.valueOf(500));
        }

        return new ResponseEntity<AddPatientRecordResponse>(AddPatientRecordResponse.builder().message("Record Added Successfully").build(), HttpStatusCode.valueOf(200));


    }


    public ResponseEntity<List<PatientRecords>> getPatientRecordsByID(GetPatientRecordRequest request) {

        List<PatientRecords> patientRecords = patientRecordsRepository.findPatientRecordsByPatientID(request.getPatientIDString()).orElseThrow();
//        List<PatientRecords> toBeRemoved = new ArrayList<PatientRecords>();
//        for (PatientRecords record : patientRecords) {
//            if (record.getPatientID().equals(request.getPatientIDString()) == false) {
//                toBeRemoved.add(record);
//            }
//        }
//        patientRecords.removeAll(toBeRemoved);
        return new ResponseEntity<List<PatientRecords>>(patientRecords, HttpStatusCode.valueOf(200));


    }


    public ResponseEntity<FetchAllPatientsResponse> fetchPatients(){
        List<Patient> patients;
        try {
            patients = patientRepository.findAll();
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<FetchAllPatientsResponse>(FetchAllPatientsResponse.builder().build(), HttpStatusCode.valueOf(500));
        }
        return new ResponseEntity<FetchAllPatientsResponse>(FetchAllPatientsResponse.builder().patients(patients).build(), HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<PatientRecords> FetchRecord(String recordID){
        try {
            Optional<PatientRecords> record = patientRecordsRepository.findById(recordID);
            //System.out.print(patient.isPresent());
            return new ResponseEntity<PatientRecords>(record.get(), HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            // TODO: handle exception

        }
        return new ResponseEntity<PatientRecords>(HttpStatusCode.valueOf(403));
    }
}
