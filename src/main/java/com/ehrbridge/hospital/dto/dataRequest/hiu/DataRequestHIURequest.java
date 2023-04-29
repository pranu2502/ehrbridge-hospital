package com.ehrbridge.hospital.dto.dataRequest.hiu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;  

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataRequestHIURequest {
    private String txnID;
    private String ehrbID;
    private String hipID;
    private String doctorID;
    private String request_msg;
    private Date dateFrom;
    private Date dateTo;
    private String hiType;
    private String departments;
    private String rsa_pubkey;
}
