package com.ehrbridge.hospital.service;


import com.ehrbridge.hospital.dto.auth.doctor.LoginRequest;
import com.ehrbridge.hospital.dto.auth.doctor.LoginResponse;
import com.ehrbridge.hospital.dto.auth.doctor.RegisterRequest;
import com.ehrbridge.hospital.dto.auth.doctor.RegisterResponse;
import com.ehrbridge.hospital.entity.Doctor;
import com.ehrbridge.hospital.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoctorAuthService {
    private final JwtService jwtService;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

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
        return RegisterResponse.builder().message("Doctor Registered Successfully").doctorID(doctor.getId()).build();
    }

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        var user = doctorRepository.findByEmailAddress(request.getEmail()).orElseThrow();
        var token = jwtService.generateToken(user);


        return LoginResponse.builder().token(token).message("Login Successful").build();
    }
}
