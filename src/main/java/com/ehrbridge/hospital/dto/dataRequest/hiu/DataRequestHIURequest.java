package com.ehrbridge.hospital.dto.dataRequest.hiu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataRequestHIURequest {
    private String txnID;
    private String ehrbID;
    private String hipID;
    private String doctorID;
    private String request_msg;
    private String dateFrom;
    private String dateTo;
}
