package com.ehrbridge.hospital.service;

import com.ehrbridge.hospital.dto.dataRequest.hiu.ReceiveDataCallbackURLRequest;
import com.ehrbridge.hospital.dto.dataRequest.hiu.ReceiveDataCallbackURLResponse;

import com.ehrbridge.hospital.repository.ReceivedDataRecordsRepository;

import com.ehrbridge.hospital.entity.PatientRecords;
import com.ehrbridge.hospital.entity.ReceivedPatientRecords;
import lombok.RequiredArgsConstructor;


import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class DataReceiveService {

    private final ReceivedDataRecordsRepository ReceivedDataRecordsRepository;

    public ResponseEntity<ReceiveDataCallbackURLResponse> receiveDataHIU (ReceiveDataCallbackURLRequest request) {
        for (PatientRecords record : request.getPatientRecords()) {
            var receivedPatientRecord  = ReceivedPatientRecords.builder()
                    .bp(record.getBp())
                    .height(record.getHeight())
                    .weight(record.getWeight())
                    .heartRate(record.getHeartRate())
                    .problems(record.getProblems())
                    .diagnosis(record.getDiagnosis())
                    .prescription(record.getPrescription())
                    .hiType(record.getHiType())
                    .department(record.getDepartment())
                    .timeStamp(record.getTimeStamp())
                    .doctorID(record.getDoctorID())
                    .patientID(record.getPatientID())
                    .ehrbID(request.getEhrbID())
                    .txnID(request.getTxnID())
                    .build();
            ReceivedDataRecordsRepository.save(receivedPatientRecord);
        }
        return new ResponseEntity<ReceiveDataCallbackURLResponse>(ReceiveDataCallbackURLResponse.builder().message("Data transfer successful").build(), HttpStatusCode.valueOf(200));


    } 
    
}