package com.ehrbridge.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ehrbridge.hospital.entity.Doctor;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, String> {

    Optional<Doctor> findByEmailAddress(String emailAddress);

}
