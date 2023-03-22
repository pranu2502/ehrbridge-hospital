package com.ehrbridge.hospital.auth.repository;

import com.ehrbridge.hospital.auth.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, String> {

    Optional<Doctor> findByEmailAddress(String emailAddress);

}
