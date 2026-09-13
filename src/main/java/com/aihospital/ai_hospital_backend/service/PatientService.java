package com.aihospital.ai_hospital_backend.service;

import com.aihospital.ai_hospital_backend.dto.LoginRequest;
import com.aihospital.ai_hospital_backend.dto.LoginResponse;
import com.aihospital.ai_hospital_backend.dto.PatientResponse;
import com.aihospital.ai_hospital_backend.dto.RegisterRequest;
import com.aihospital.ai_hospital_backend.entity.Patient;
import com.aihospital.ai_hospital_backend.repository.PatientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public PatientService(
            PatientRepository patientRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public PatientResponse registerPatient(RegisterRequest request) {

        if (patientRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email is already registered"
            );
        }

        Patient patient = new Patient();

        patient.setName(request.getName());
        patient.setAge(request.getAge());
        patient.setGender(request.getGender());
        patient.setPhone(request.getPhone());
        patient.setEmail(request.getEmail());

        String hashedPassword =
                passwordEncoder.encode(request.getPassword());

        patient.setPasswordHash(hashedPassword);

        Patient savedPatient =
                patientRepository.save(patient);

        PatientResponse response =
                new PatientResponse();

        response.setPatientId(
                savedPatient.getPatientId());

        response.setName(
                savedPatient.getName());

        response.setAge(
                savedPatient.getAge());

        response.setGender(
                savedPatient.getGender());

        response.setPhone(
                savedPatient.getPhone());

        response.setEmail(
                savedPatient.getEmail());

        return response;
    }

    public LoginResponse loginPatient(LoginRequest request) {

        Patient patient = patientRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED,
                                "Invalid email or password"
                        ));

        boolean passwordMatches =
                passwordEncoder.matches(
                        request.getPassword(),
                        patient.getPasswordHash()
                );

        if (!passwordMatches) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password"
            );
        }

        String token =
                jwtService.generateToken(
                        patient.getPatientId(),
                        patient.getEmail()
                );

        LoginResponse response =
                new LoginResponse();

        response.setToken(token);
        response.setPatientId(
                patient.getPatientId());
        response.setName(
                patient.getName());
        response.setEmail(
                patient.getEmail());

        return response;
    }
}