package com.aihospital.ai_hospital_backend.repository;

import com.aihospital.ai_hospital_backend.entity.HospitalHours;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HospitalHoursRepository
        extends JpaRepository<HospitalHours, Integer> {

    Optional<HospitalHours> findByHospitalHospitalIdAndDayOfWeek(
            Integer hospitalId,
            Integer dayOfWeek
    );
}