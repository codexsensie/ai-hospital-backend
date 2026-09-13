package com.aihospital.ai_hospital_backend.controller;

import com.aihospital.ai_hospital_backend.entity.Hospital;
import com.aihospital.ai_hospital_backend.repository.ReviewRepository;
import com.aihospital.ai_hospital_backend.service.HospitalHoursService;
import com.aihospital.ai_hospital_backend.service.HospitalService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hospitals")
public class HospitalController {

    private final HospitalService hospitalService;
    private final ReviewRepository reviewRepository;

    public HospitalController(
            HospitalService hospitalService,
            ReviewRepository reviewRepository) {

        this.hospitalService = hospitalService;
        this.reviewRepository = reviewRepository;
    }

    @GetMapping
    public List<Hospital> getAllHospitals() {
        return hospitalService.getAllHospitals();
    }

    @GetMapping("/{hospitalId}/rating")
    public Double getHospitalRating(
            @PathVariable Integer hospitalId) {

        return reviewRepository.findAverageRatingByHospitalId(hospitalId);
    }
}