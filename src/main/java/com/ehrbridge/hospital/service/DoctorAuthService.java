package com.ehrbridge.hospital.service;

import com.ehrbridge.hospital.dto.RegisterRequest;
import com.ehrbridge.hospital.dto.RegisterResponse;
import com.ehrbridge.hospital.entity.Doctor;
import com.ehrbridge.hospital.repository.DoctorRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoctorAuthService {
    private final JwtService jwtService;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    public RegisterResponse register(RegisterRequest request){
        var doctor = Doctor.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .emailAddress(request.getEmailAddress())
                .phoneString(request.getPhoneString())
                .address(request.getAddress())
                .gender(request.getGender())
                .doctorEhrbID(request.getEhrbID())
                .department(request.getDepartment())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        doctorRepository.save(doctor);

        var jwtToken = jwtService.generateToken(doctor);
        return RegisterResponse.builder().message("Doctor Registered Successfully").token(jwtToken).build();
    }
}
