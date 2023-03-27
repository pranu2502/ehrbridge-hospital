package com.ehrbridge.hospital.auth.dto;

import org.json.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ConsentObject {
    private String ehrbID;
    private String hiuID;
    private String hipID;
    private String doctorId;
    private String[] hiType;
    private String[] departments;
    private JSONObject permission;
}
