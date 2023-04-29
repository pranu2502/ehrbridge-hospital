package com.ehrbridge.hospital.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ehrbridge.hospital.dto.hospital.FetchAllHospitalResponse;
import com.ehrbridge.hospital.dto.hospital.Hospital;
import com.ehrbridge.hospital.dto.hospital.PatientServerHospitalsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class HospitalService {
    @Value("${ehrbridge.gateway.host}")
    private String GATEWAY_HOST;

    @Value("${ehrbridge.gateway.hospital-fetch-all.endpoint}")
    private String GATEWAY_HOSPITAL_ALL_REQ_ENDPOINT;

    @Value("${ehrbridge.gateway.hospital-fetch-id.endpoint}")
    private String GATEWAY_HOSPITAL_ID_REQ_ENDPOINT;

    @Autowired
    private RestTemplate rest;

    @Autowired
    private HttpHeaders headers;

    public ResponseEntity<PatientServerHospitalsResponse> fetchHospitals(String ehrbID){
        String GATEWAY_REQ_ENDPOINT = GATEWAY_HOST + GATEWAY_HOSPITAL_ALL_REQ_ENDPOINT + "?ehrbID=" + ehrbID;
        try {
            // HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);
            ResponseEntity<PatientServerHospitalsResponse> responseEntity = rest.exchange(GATEWAY_REQ_ENDPOINT, HttpMethod.GET, new HttpEntity<>(headers), PatientServerHospitalsResponse.class);
            if(responseEntity.getStatusCode().value() == 200){
                return responseEntity;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return new ResponseEntity<PatientServerHospitalsResponse>(PatientServerHospitalsResponse.builder().build(), HttpStatusCode.valueOf(501));
    }

    public ResponseEntity<Hospital> fetchHospital(String hospitalID){
        String GATEWAY_REQ_ENDPOINT = GATEWAY_HOST + GATEWAY_HOSPITAL_ID_REQ_ENDPOINT;
        try {
            // HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);
            
            ResponseEntity<Hospital> responseEntity = rest.exchange(GATEWAY_REQ_ENDPOINT + "?hospitalID=" + hospitalID, HttpMethod.GET, new HttpEntity<>(headers), Hospital.class);
            if(responseEntity.getStatusCode().value() == 200){
                return responseEntity;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return new ResponseEntity<Hospital>(Hospital.builder().build(), HttpStatusCode.valueOf(501));
    }
}
