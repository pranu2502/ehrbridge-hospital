package com.ehrbridge.hospital.service;

import com.ehrbridge.hospital.dto.dataRequest.DataRequestHIPRequest;
import com.ehrbridge.hospital.dto.dataRequest.DataRequestHIPResponse;
import com.ehrbridge.hospital.dto.dataRequest.DataRequestHIURequest;
import com.ehrbridge.hospital.dto.dataRequest.DataRequestHIUResponse;
import com.ehrbridge.hospital.entity.DataRequestHIP;
import com.ehrbridge.hospital.entity.DataRequestsHIU;
import com.ehrbridge.hospital.repository.DataRequestsHIPRepository;
import com.ehrbridge.hospital.repository.DataRequestsHIURepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataRequestService {

    private final DataRequestsHIURepository dataRequestsHIURepository;
    private final DataRequestsHIPRepository dataRequestsHIPRepository;
    public DataRequestHIUResponse requestDataHIU(DataRequestHIURequest request)
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
        return DataRequestHIUResponse.builder().data_request_id(data_request_id).message("Data Request Generated").build();
    }

    public DataRequestHIPResponse requestDataHIP(DataRequestHIPRequest request) {
        var dataRequest = DataRequestHIP.builder()
                .encrypted_consent_object(request.getEncrypted_consent_object())
                .txnID(request.getTxnID())
                .requestID(request.getRequestID())
                .ehbrID(request.getEhrbID())
                .hiuID(request.getHiuID())
                .request_message(request.getRequest_msg())
                .callback_url(request.getCallbackURL())
                .build();

        dataRequestsHIPRepository.save(dataRequest);

        return DataRequestHIPResponse.builder().message("Request sent to HIP").build();
    }
}
