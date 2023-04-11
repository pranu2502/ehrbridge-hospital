package com.ehrbridge.hospital.dto.dataRequest.hip;

import com.ehrbridge.hospital.entity.DataRequestHIP;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FetchDataRequestByIDResponse {
    public DataRequestHIP data_request;
}
