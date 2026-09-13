package com.aihospital.ai_hospital_backend.repository;

import com.aihospital.ai_hospital_backend.entity.DoctorSlot;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface DoctorSlotRepository
        extends JpaRepository<DoctorSlot, Integer> {

    List<DoctorSlot> findByDoctorDoctorId(Integer doctorId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<DoctorSlot> findById(Integer slotId);

    Optional<DoctorSlot> findBySlotId(Integer slotId);
}