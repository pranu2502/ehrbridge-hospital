package com.ehrbridge.hospital.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Table(name="consent_objects_hip")
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ConsentObjectHIP {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String consent_object_id;

    private String encrypted_consent_object;
    private String public_key;
    private String txnID;
}
