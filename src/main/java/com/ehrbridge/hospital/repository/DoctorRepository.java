package com.ehrbridge.hospital.repository;


import com.ehrbridge.hospital.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, String> {

    Optional<Doctor> findByEmailAddress(String emailAddress);

    Optional<Doctor> findByDoctorEhrbID(String ehrbID);

    Optional<Doctor> findById(String id);

}
