package com.ehrbridge.hospital.dto.dataRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataRequestHIPRequest {
    private String encrypted_consent_object;
    private String txnID;
    private String requestID;
    private String ehrbID;
    private String hiuID;
    private String request_msg;
    private String callbackURL;

}
