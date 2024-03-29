package com.ehrbridge.hospital.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;  

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "data_requests_hiu")
@Data
public class DataRequestsHIU {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String data_request_id;
    private String txnID;
    private String request_message;
    private String ehrbID;
    private String hipID;
    private Date dateFrom;
    private Date dateTo;
    private String hiType;
    private String[] departments;
    @Column(columnDefinition = "TEXT")
    private String rsa_pubkey;
}
