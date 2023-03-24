package com.ehrbridge.hospital.repository;

import com.ehrbridge.hospital.entity.ConsentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConsentTransactionRepository extends JpaRepository<ConsentTransaction, String> {
    Optional<ConsentTransaction> findByTxnID(String txnID);
}
