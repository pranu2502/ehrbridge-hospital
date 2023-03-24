package com.ehrbridge.hospital.auth.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ConsentObjectRequest {
    private String ehrbID;
    private String hiuID;
    private String hipID;
    private String doctorId;
    private String[] hiType;
    private String[] departments;
    private JSONObject permisisons;
}
