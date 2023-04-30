package com.ehrbridge.hospital.repository;


import com.ehrbridge.hospital.entity.ReceivedPatientRecords;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;




public interface ReceivedDataRecordsRepository extends JpaRepository<ReceivedPatientRecords, String> {
    List<ReceivedPatientRecords> findAllByPatientID(String patientID);

    List<ReceivedPatientRecords> findAllByEhrbID(String ehrbID);
}
