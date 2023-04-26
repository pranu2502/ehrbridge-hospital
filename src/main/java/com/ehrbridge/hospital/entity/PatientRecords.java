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
@Table(name = "patient-records")
public class PatientRecords {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String recordID;
    private String patientID;
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
