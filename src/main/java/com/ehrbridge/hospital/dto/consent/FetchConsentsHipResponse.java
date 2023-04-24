package com.ehrbridge.hospital.dto.consent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.*;


import java.util.List;

import com.ehrbridge.hospital.entity.ConsentObjectHIP;

import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FetchConsentsHipResponse {
    private List<ConsentObjectHIP> consent_objs;
}
