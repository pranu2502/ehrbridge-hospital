package com.ehrbridge.hospital.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String ehbrID;
    private String hipID;
    private String dateFrom;
    private String dateTo;
}
