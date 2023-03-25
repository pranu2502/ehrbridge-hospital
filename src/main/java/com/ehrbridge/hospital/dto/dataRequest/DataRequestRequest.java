package com.ehrbridge.hospital.dto.dataRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataRequestRequest {
    private String txnID;
    private String ehrbID;
    private String hipID;
    private String request_msg;
}
