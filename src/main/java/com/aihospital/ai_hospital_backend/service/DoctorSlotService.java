package com.aihospital.ai_hospital_backend.service;

import com.aihospital.ai_hospital_backend.dto.DoctorSlotResponse;
import com.aihospital.ai_hospital_backend.entity.DoctorSlot;
import com.aihospital.ai_hospital_backend.repository.DoctorSlotRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorSlotService {

    private final DoctorSlotRepository doctorSlotRepository;

    public DoctorSlotService(DoctorSlotRepository doctorSlotRepository) {
        this.doctorSlotRepository = doctorSlotRepository;
    }

    public List<DoctorSlot> getAllSlots() {
        return doctorSlotRepository.findAll();
    }

    public List<DoctorSlotResponse> getSlotsByDoctorId(Integer doctorId) {

        List<DoctorSlot> slots =
                doctorSlotRepository.findByDoctorDoctorId(doctorId);

        return slots.stream()
                .map(slot -> {

                    DoctorSlotResponse response =
                            new DoctorSlotResponse();

                    response.setSlotId(slot.getSlotId());

                    response.setDoctorId(
                            slot.getDoctor().getDoctorId());

                    response.setDoctorName(
                            slot.getDoctor().getName());

                    response.setSlotTime(
                            slot.getSlotTime());

                    response.setIsBooked(
                            slot.getIsBooked());

                    return response;
                })
                .toList();
    }
}