package com.ehrbridge.hospital.dto.dataRequest.hiu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EncryptedPatientDataObject {
    private byte[] encrypted_data_object;
    private byte[] aes_secret;
}
