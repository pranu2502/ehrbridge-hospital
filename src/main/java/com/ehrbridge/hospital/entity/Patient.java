package com.ehrbridge.hospital.entity;

import org.springframework.boot.autoconfigure.AutoConfiguration;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "patients")
public class Patient {

    @Id
    private String ehrbID;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneString;
    private String gender;
    private String address;
}
