package com.ehrbridge.hospital.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ehrbridge.hospital.entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, String>{
    @Override
    Optional<Patient> findById(String s);
}
