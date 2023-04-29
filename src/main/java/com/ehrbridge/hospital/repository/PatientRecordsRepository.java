package com.ehrbridge.hospital.repository;

import com.ehrbridge.hospital.entity.Patient;
import com.ehrbridge.hospital.entity.PatientRecords;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.*;
import java.util.List;
import java.util.Optional;

public interface PatientRecordsRepository extends JpaRepository<PatientRecords, String> {

    Optional<List<PatientRecords>> findPatientRecordsByPatientID(String patientId);
    
}
