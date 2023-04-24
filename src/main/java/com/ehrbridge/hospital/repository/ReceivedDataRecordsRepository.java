package com.ehrbridge.hospital.repository;


import com.ehrbridge.hospital.entity.ReceivedPatientRecords;
import org.springframework.data.jpa.repository.JpaRepository;




public interface ReceivedDataRecordsRepository extends JpaRepository<ReceivedPatientRecords, String> {
    
}
