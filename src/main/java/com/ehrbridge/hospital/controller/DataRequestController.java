package com.ehrbridge.hospital.controller;

import com.ehrbridge.hospital.dto.dataRequest.DataRequestHIPRequest;
import com.ehrbridge.hospital.dto.dataRequest.DataRequestHIPResponse;
import com.ehrbridge.hospital.dto.dataRequest.DataRequestHIURequest;
import com.ehrbridge.hospital.dto.dataRequest.DataRequestHIUResponse;
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
        return ResponseEntity.ok(dataRequestService.requestDataHIU(request));
    }

    @PostMapping("/request-data-hip")
    public ResponseEntity<DataRequestHIPResponse> requestDataHIP(@RequestBody DataRequestHIPRequest request)
    {
        return ResponseEntity.ok(dataRequestService.requestDataHIP(request));
    }
}
