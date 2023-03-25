package com.ehrbridge.hospital.controller;

import com.ehrbridge.hospital.dto.dataRequest.DataRequestRequest;
import com.ehrbridge.hospital.dto.dataRequest.DataRequestResponse;
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
    public ResponseEntity<DataRequestResponse> requestData(@RequestBody DataRequestRequest request)
    {
        return ResponseEntity.ok(dataRequestService.requestDataHIU(request));
    }
}
