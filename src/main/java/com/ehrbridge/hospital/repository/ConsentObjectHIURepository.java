package com.ehrbridge.hospital.repository;

import com.ehrbridge.hospital.entity.ConsentObjectHIU;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ConsentObjectHIURepository extends JpaRepository<ConsentObjectHIU, String> {
    @Query(value = "select * from consent_objects_hiu where doctor_ehrb_id = ?1", nativeQuery = true)
    List<ConsentObjectHIU> findByDoctorEhrbID(String doctorEhrbID);
}
