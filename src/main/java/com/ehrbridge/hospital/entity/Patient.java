package com.ehrbridge.hospital.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import org.springframework.boot.autoconfigure.AutoConfiguration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String ehrbID;
    private String firstName;
    private String lastName;
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")
    @Column(unique=true)
    private String emailAddress;
    private String phoneString;
    private String gender;
    private String address;

}
