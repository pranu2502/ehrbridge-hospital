package com.ehrbridge.hospital.controller;


import com.ehrbridge.hospital.dto.dataRequest.hiu.ReceiveDataCallbackURLRequest;
import com.ehrbridge.hospital.dto.dataRequest.hiu.ReceiveDataCallbackURLResponse;
import com.ehrbridge.hospital.service.DataReceiveService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/data")
@RequiredArgsConstructor
public class DataReceiveController {

    @Autowired
    private final DataReceiveService dataReceiveService;

    @PostMapping("/receive-data-hiu")
    public ResponseEntity<ReceiveDataCallbackURLResponse> requestDataHIU(@RequestBody ReceiveDataCallbackURLRequest request)
    {
        return dataReceiveService.receiveDataHIU(request);
    }
    
}
