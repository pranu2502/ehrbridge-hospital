package com.ehrbridge.hospital.dto.gateway;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataRequestGatewayRequest {
    private String signed_consent_object;
    private String txnID;
    private String requestID;
    private String ehrbID;
    private String doctorID;
    private String hiuID;
    private String hipID;
    private String request_msg;
    private String callbackURL;
    private String dateFrom;
    private String dateTo;

}
