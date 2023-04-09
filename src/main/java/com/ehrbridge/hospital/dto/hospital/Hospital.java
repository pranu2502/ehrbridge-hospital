package com.ehrbridge.hospital.dto.hospital;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Hospital  {
    private Long id;
    private String hospitalName;
    private String emailAddress;
    private String phone;
    private String address;
    private String hospitalLicense;
    private String hospitalId;
    private String hook_url;
}

