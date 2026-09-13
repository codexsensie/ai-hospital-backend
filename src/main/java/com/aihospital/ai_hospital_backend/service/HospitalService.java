package com.aihospital.ai_hospital_backend.service;

import com.aihospital.ai_hospital_backend.entity.Hospital;
import com.aihospital.ai_hospital_backend.repository.HospitalRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class HospitalService {
    private final HospitalRepository hospitalRepository;
    public HospitalService(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }
    public List<Hospital> getAllHospitals(){
        return hospitalRepository.findAll();
    }
}
