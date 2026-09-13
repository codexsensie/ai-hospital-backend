package com.aihospital.ai_hospital_backend.repository;

import com.aihospital.ai_hospital_backend.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppointmentRepository
        extends JpaRepository<Appointment, Integer> {

    List<Appointment> findByPatientId(Integer patientId);

    Optional<Appointment> findByAppointmentIdAndPatientId(
            Integer appointmentId,
            Integer patientId);
}