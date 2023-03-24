package com.ehrbridge.hospital.repository;

import com.ehrbridge.hospital.entity.ConsentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsentTransactionRepository extends JpaRepository<ConsentTransaction, String> {
}
