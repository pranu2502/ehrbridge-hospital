package com.ehrbridge.hospital.repository;

import com.ehrbridge.hospital.entity.PatientRecords;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.*;

public interface PatientRecordsRepository extends JpaRepository<PatientRecords, String> {
}
