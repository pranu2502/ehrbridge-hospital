package com.ehrbridge.hospital.dto.dataRequest.hiu;

import java.util.List;

import com.ehrbridge.hospital.entity.DataRequestsHIU;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FetchDataRequestsHIUResponse {
    public List<DataRequestsHIU> data_requests; 
}
