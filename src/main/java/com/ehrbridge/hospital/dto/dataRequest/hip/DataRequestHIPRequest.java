package com.ehrbridge.hospital.dto.dataRequest.hip;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;  

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataRequestHIPRequest {
    private String signed_consent_object;
    private String txnID;
    private String requestID;
    private String ehrbID;
    private String hiuID;
    private String request_msg;
    private String callbackURL;
    @JsonFormat(pattern="yyyy-MM-dd' 'HH:mm:ss.SSS")
    private Date dateFrom;
    @JsonFormat(pattern="yyyy-MM-dd' 'HH:mm:ss.SSS")
    private Date dateTo;
    private String hiType;
    private String departments;
}
