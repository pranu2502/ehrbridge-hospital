package com.ehrbridge.hospital.dto.dataRequest.hip;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.ehrbridge.hospital.entity.DataRequestHIP;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FetchDataRequests {
    private List<DataRequestHIP> dataRequests;
}
