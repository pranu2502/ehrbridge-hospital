package com.ehrbridge.hospital.repository;

import com.ehrbridge.hospital.entity.ConsentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

public interface ConsentTransactionRepository extends JpaRepository<ConsentTransaction, String> {
    Optional<ConsentTransaction> findByTxnID(String txnID);


    @Query(value = "select * from consent_transactions where consent_object_id in ?1", nativeQuery = true)
    List<ConsentTransaction> findAllByConsentObjectID(List<String> consentIDs);

}
