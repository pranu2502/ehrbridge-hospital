package com.ehrbridge.hospital.auth.repository;

import com.ehrbridge.hospital.auth.entity.ConsentObjectHIU;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsentObjectRepository extends JpaRepository<ConsentObjectHIU, String> {

}
