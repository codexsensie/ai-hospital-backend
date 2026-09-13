package com.aihospital.ai_hospital_backend.dto;

import jakarta.validation.constraints.NotNull;

public class BookAppointmentRequest {

    @NotNull(message = "Doctor ID is required")
    private Integer doctorId;

    @NotNull(message = "Slot ID is required")
    private Integer slotId;

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public Integer getSlotId() {
        return slotId;
    }

    public void setSlotId(Integer slotId) {
        this.slotId = slotId;
    }
}