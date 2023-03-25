package com.ehrbridge.hospital.repository;

import com.ehrbridge.hospital.entity.DataRequestHIP;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataRequestsHIPRepository extends JpaRepository<DataRequestHIP, String> {
}
