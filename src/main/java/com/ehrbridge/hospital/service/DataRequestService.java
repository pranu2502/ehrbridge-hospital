package com.ehrbridge.hospital.service;

import com.ehrbridge.hospital.dto.dataRequest.DataRequestRequest;
import com.ehrbridge.hospital.dto.dataRequest.DataRequestResponse;
import com.ehrbridge.hospital.entity.DataRequestsHIU;
import com.ehrbridge.hospital.repository.DataRequestsHIURepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataRequestService {

    private final DataRequestsHIURepository dataRequestsHIURepository;
    public DataRequestResponse requestDataHIU(DataRequestRequest request)
    {
        var dataRequest = DataRequestsHIU.builder()
                .ehbrID(request.getEhrbID())
                .txnID(request.getTxnID())
                .hipID(request.getHipID())
                .request_message(request.getRequest_msg())
                .build();

        dataRequestsHIURepository.save(dataRequest);

        Long data_request_id = dataRequest.getData_request_id();
        //TODO: Send Request to ABDM
        return DataRequestResponse.builder().data_request_id(data_request_id).message("Data Request Generated").build();
    }
}
