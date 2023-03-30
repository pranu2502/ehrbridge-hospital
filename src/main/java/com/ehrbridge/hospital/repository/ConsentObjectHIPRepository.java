package com.ehrbridge.hospital.repository;

import com.ehrbridge.hospital.entity.ConsentObjectHIP;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsentObjectHIPRepository extends JpaRepository<ConsentObjectHIP, String> {
    Optional<ConsentObjectHIP> findByTxnID(String txnID);
}
