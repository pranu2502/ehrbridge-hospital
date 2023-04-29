package com.ehrbridge.hospital.repository;

import com.ehrbridge.hospital.entity.ConsentTransaction;
import com.ehrbridge.hospital.entity.DataRequestHIP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DataRequestsHIPRepository extends JpaRepository<DataRequestHIP, String> {
    Optional<DataRequestHIP> findByTxnID(String txnID);
}
