package com.aihospital.ai_hospital_backend.repository;

import com.aihospital.ai_hospital_backend.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
}