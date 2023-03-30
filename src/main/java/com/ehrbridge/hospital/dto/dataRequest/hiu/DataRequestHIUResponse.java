package com.ehrbridge.hospital.dto.dataRequest.hiu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataRequestHIUResponse {
    private String message;
    private String data_request_id;
}
