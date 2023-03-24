package com.ehrbridge.hospital.auth.repository;

import com.ehrbridge.hospital.auth.entity.ConsentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsentTransactionRepository extends JpaRepository<ConsentTransaction, String> {
}
