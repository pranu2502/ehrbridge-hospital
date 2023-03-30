package com.ehrbridge.hospital.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@Table(name = "consent_objects_hiu")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ConsentObjectHIU {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String consent_object_id;
    private String patient_ehbr_id;
    private String hiu_id;
    private String hip_id;
    private String doctor_ehbr_id;
    private String hi_type;
    private String departments;
    private Date date_from;
    private Date date_to;
    private Date validity;

    

}
