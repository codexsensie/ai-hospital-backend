package com.aihospital.ai_hospital_backend.controller;

import com.aihospital.ai_hospital_backend.dto.AppointmentResponse;
import com.aihospital.ai_hospital_backend.dto.BookAppointmentRequest;
import com.aihospital.ai_hospital_backend.entity.Appointment;
import com.aihospital.ai_hospital_backend.service.AppointmentService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }


    @GetMapping
    public List<AppointmentResponse> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    @GetMapping("/me")
    public List<AppointmentResponse> getMyAppointments() {
        return appointmentService.getMyAppointments();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public AppointmentResponse bookAppointment(
            @Valid @RequestBody BookAppointmentRequest request) {

        return appointmentService.bookAppointment(request);
    }

    @DeleteMapping("/{appointmentId}")
    public void cancelAppointment(
            @PathVariable Integer appointmentId) {

        appointmentService.cancelAppointment(appointmentId);
    }
}