package com.ehrbridge.hospital.dto.consent;

import java.util.List;

import com.ehrbridge.hospital.entity.ConsentObjectHIU;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FetchConsentObjsResponse {
    private List<ConsentObjectHIU> consent_objs;
}
