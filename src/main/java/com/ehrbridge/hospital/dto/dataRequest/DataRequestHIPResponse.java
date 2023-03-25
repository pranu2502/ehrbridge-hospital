package com.ehrbridge.hospital.dto.dataRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataRequestHIPResponse {
    private String message;
}
