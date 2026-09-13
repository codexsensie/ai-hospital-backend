package com.aihospital.ai_hospital_backend.repository;

import com.aihospital.ai_hospital_backend.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalRepository extends JpaRepository<Hospital , Integer> {

}
