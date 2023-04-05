package com.ehrbridge.hospital.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Table(name = "consent_transactions")
@NoArgsConstructor
@Entity
@Builder
public class ConsentTransaction
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String consent_request_id;
    private String txnID;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "consent_object_id", referencedColumnName = "consent_object_id")
    private ConsentObjectHIU consent_object_id;
    private String consent_status;
    @Column(length = 100000000)
    private String signed_consent_object;

}
