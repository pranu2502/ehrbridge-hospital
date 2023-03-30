package com.ehrbridge.hospital.service;


import com.ehrbridge.hospital.dto.auth.doctor.LoginRequest;
import com.ehrbridge.hospital.dto.auth.doctor.LoginResponse;
import com.ehrbridge.hospital.dto.auth.doctor.RegisterRequest;
import com.ehrbridge.hospital.dto.auth.doctor.RegisterResponse;
import com.ehrbridge.hospital.entity.Doctor;
import com.ehrbridge.hospital.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<RegisterResponse> register(RegisterRequest request){
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

        Doctor doctorSaved;
        try {
            doctorSaved = doctorRepository.save(doctor);
        } catch (Exception e) {
            // TODO: handle exception
            return new ResponseEntity<RegisterResponse>(RegisterResponse.builder().message("Doctor Already Exists!").build(), HttpStatusCode.valueOf(400));
        }

        var jwtToken = jwtService.generateToken(doctor);

        return new ResponseEntity<RegisterResponse>(RegisterResponse.builder().message("Doctor Registered Successfully!").doctorID(doctorSaved.getDoctorEhrbID()).build(), HttpStatusCode.valueOf(200));

    }

    public ResponseEntity<LoginResponse> login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        Optional<Doctor> user;
        try{
            user = doctorRepository.findByEmailAddress(request.getEmail());
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<LoginResponse>(LoginResponse.builder().message("User does not exist!").build(), HttpStatusCode.valueOf(403));
        }
        var token = jwtService.generateToken(user.get());

        return new ResponseEntity<LoginResponse>(LoginResponse.builder().message("Login Successful!").token(token).build(), HttpStatusCode.valueOf(200));
    }
}
