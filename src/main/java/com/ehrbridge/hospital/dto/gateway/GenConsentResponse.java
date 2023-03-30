package com.ehrbridge.hospital.dto.gateway;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class GenConsentResponse {
    private String status;
    private String txnID;
}
