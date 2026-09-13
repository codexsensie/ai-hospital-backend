package com.aihospital.ai_hospital_backend.controller;

import com.aihospital.ai_hospital_backend.dto.DoctorResponse;
import com.aihospital.ai_hospital_backend.dto.DoctorSlotResponse;
import com.aihospital.ai_hospital_backend.service.DoctorService;
import com.aihospital.ai_hospital_backend.service.DoctorSlotService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;
    private final DoctorSlotService doctorSlotService;

    public DoctorController(
            DoctorService doctorService,
            DoctorSlotService doctorSlotService) {

        this.doctorService = doctorService;
        this.doctorSlotService = doctorSlotService;
    }



    @GetMapping
    public List<DoctorResponse> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @GetMapping("/{doctorId}/slots")
    public List<DoctorSlotResponse> getSlotsByDoctorId(
            @PathVariable Integer doctorId) {

        System.out.println(
                "Doctor slots endpoint called for doctor: "
                        + doctorId
        );

        return doctorSlotService.getSlotsByDoctorId(doctorId);
    }
}