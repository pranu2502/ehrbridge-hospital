package com.ehrbridge.hospital.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="data_requests_hip")
@Entity
public class DataRequestHIP {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(length = 100000000)
    private String signed_consent_object;
    private String txnID;
    private String requestID;
    private String ehbrID;
    private String hiuID;
    private String request_message;
    private String callback_url;

}
