package com.ehrbridge.hospital.controller;

import com.ehrbridge.hospital.dto.dataRequest.hip.DataRequestHIPRequest;
import com.ehrbridge.hospital.dto.dataRequest.hip.DataRequestHIPResponse;
import com.ehrbridge.hospital.dto.dataRequest.hip.FetchDataRequestByIDResponse;
import com.ehrbridge.hospital.dto.dataRequest.hip.FetchDataRequests;
import com.ehrbridge.hospital.dto.dataRequest.hiu.DataRequestHIURequest;
import com.ehrbridge.hospital.dto.dataRequest.hiu.DataRequestHIUResponse;
import com.ehrbridge.hospital.dto.dataRequest.hiu.FetchDataRequestsHIUResponse;
import com.ehrbridge.hospital.service.DataRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/data")
@RequiredArgsConstructor
public class DataRequestController {
    @Autowired
    private final DataRequestService dataRequestService;

    @PostMapping("/request-data-hiu")
    public ResponseEntity<DataRequestHIUResponse> requestDataHIU(@RequestBody DataRequestHIURequest request)
    {
        return dataRequestService.requestDataHIU(request);
    }

    @PostMapping("/request-data-hip")
    public ResponseEntity<DataRequestHIPResponse> requestDataHIP(@RequestBody DataRequestHIPRequest request)
    {
        return dataRequestService.requestDataHIP(request);
    }

    @GetMapping("/fetch-data-requests-hip")
    public ResponseEntity<FetchDataRequests> fetchDataRequestsHIP()
    {
        return dataRequestService.fetchDataRequestsHIP();
    }

    @GetMapping("/fetch-data-request-by-id-hip")
    public ResponseEntity<FetchDataRequestByIDResponse> fetchDataRequestByID(@RequestParam String datarequestID){
        return dataRequestService.fetchDataRequestByID(datarequestID);
    } 

    @GetMapping("/fetch-data-requests-hiu")
    public ResponseEntity<FetchDataRequestsHIUResponse> fetchDataRequestsHIU(){
        return dataRequestService.fetchDataRequestsHIU();
    }

}
