package com.aihospital.ai_hospital_backend.controller;

import com.aihospital.ai_hospital_backend.dto.LoginRequest;
import com.aihospital.ai_hospital_backend.dto.LoginResponse;
import com.aihospital.ai_hospital_backend.dto.PatientResponse;
import com.aihospital.ai_hospital_backend.dto.RegisterRequest;
import com.aihospital.ai_hospital_backend.service.PatientService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping("/register")
    public PatientResponse registerPatient(
            @Valid @RequestBody RegisterRequest request) {

        return patientService.registerPatient(request);
    }

    @PostMapping("/login")
    public LoginResponse loginPatient(
            @Valid @RequestBody LoginRequest request) {

        return patientService.loginPatient(request);
    }
}