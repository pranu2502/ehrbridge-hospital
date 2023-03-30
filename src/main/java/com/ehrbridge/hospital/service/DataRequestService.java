package com.ehrbridge.hospital.service;

import com.ehrbridge.hospital.dto.dataRequest.DataRequestHIPRequest;
import com.ehrbridge.hospital.dto.dataRequest.DataRequestHIPResponse;
import com.ehrbridge.hospital.dto.dataRequest.DataRequestHIURequest;
import com.ehrbridge.hospital.dto.dataRequest.DataRequestHIUResponse;
import com.ehrbridge.hospital.entity.DataRequestHIP;
import com.ehrbridge.hospital.entity.DataRequestsHIU;
import com.ehrbridge.hospital.repository.DataRequestsHIPRepository;
import com.ehrbridge.hospital.repository.DataRequestsHIURepository;

import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

@Service
@RequiredArgsConstructor
public class DataRequestService {

    private final DataRequestsHIURepository dataRequestsHIURepository;
    private final DataRequestsHIPRepository dataRequestsHIPRepository;
    
    @Value("${ehrbridge.gateway.host}")
    private String GATEWAY_URL;

    // @Value("${ehrbridge.gateway.datarequest.endpoint}")
    // private final String GATEWAY_DATA_REQ_ENDPOINT;

    public DataRequestHIUResponse requestDataHIU(DataRequestHIURequest request)
    {
        var dataRequest = DataRequestsHIU.builder()
                .ehbrID(request.getEhrbID())
                .txnID(request.getTxnID())
                .hipID(request.getHipID())
                .request_message(request.getRequest_msg())
                .build();

        dataRequestsHIURepository.save(dataRequest);

        String data_request_id = dataRequest.getData_request_id();
        System.out.println(GATEWAY_URL);

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
        //TODO: Perform verification of encrypted consent object and send FHIR using the call back link
        // Algorithm algorithm = Algorithm.RSA256(Constants.RSA_PUB, Constants.RSA_PRIV);
        // JWTVerifier verifier = JWT.require(RSA256)
        return DataRequestHIPResponse.builder().message("Request sent to HIP").build();
    }
}
