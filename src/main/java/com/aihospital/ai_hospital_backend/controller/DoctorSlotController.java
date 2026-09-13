package com.aihospital.ai_hospital_backend.controller;

import com.aihospital.ai_hospital_backend.entity.DoctorSlot;
import com.aihospital.ai_hospital_backend.service.DoctorSlotService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctor-slots")
public class DoctorSlotController {

    private final DoctorSlotService doctorSlotService;

    public DoctorSlotController(DoctorSlotService doctorSlotService) {
        this.doctorSlotService = doctorSlotService;
    }

    @GetMapping
    public List<DoctorSlot> getAllSlots() {
        return doctorSlotService.getAllSlots();
    }
}