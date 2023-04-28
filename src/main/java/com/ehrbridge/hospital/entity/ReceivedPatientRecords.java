package com.ehrbridge.hospital.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;  

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "received-patient-records")
public class ReceivedPatientRecords {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String recordID;
    private String requestID;
    private String patientID;
    private String ehrbID;
    private String txnID;
    private String doctorID;
    private String height;
    private String weight;
    private String bp;
    private String heartRate;
    private String hiType;
    private String department;
    private String problems;
    private String diagnosis;
    private String prescription;
    private Date timeStamp;
}
