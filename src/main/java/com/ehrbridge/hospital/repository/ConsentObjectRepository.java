package com.ehrbridge.hospital.repository;

import com.ehrbridge.hospital.entity.ConsentObjectHIU;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsentObjectRepository extends JpaRepository<ConsentObjectHIU, String> {

}
