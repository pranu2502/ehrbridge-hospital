package com.ehrbridge.hospital.dto.hospital;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Discovery {
    private String visitID;
    private String ehrbID;
    private String hospitalID; 
    private String hospitalName;
    private Date timestamp;
    private String department;
}

