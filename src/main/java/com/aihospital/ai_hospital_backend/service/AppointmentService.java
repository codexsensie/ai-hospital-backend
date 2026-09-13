package com.aihospital.ai_hospital_backend.service;
import com.aihospital.ai_hospital_backend.repository.DoctorSlotRepository;
import com.aihospital.ai_hospital_backend.entity.Appointment;
import com.aihospital.ai_hospital_backend.repository.AppointmentRepository;
import com.aihospital.ai_hospital_backend.entity.DoctorSlot;
import com.aihospital.ai_hospital_backend.repository.PatientRepository;
import com.aihospital.ai_hospital_backend.repository.DoctorRepository;
import com.aihospital.ai_hospital_backend.dto.AppointmentResponse;
import com.aihospital.ai_hospital_backend.dto.BookAppointmentRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.context.SecurityContextHolder;
import com.aihospital.ai_hospital_backend.entity.Doctor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorSlotRepository doctorSlotRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            DoctorSlotRepository doctorSlotRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository) {

        this.appointmentRepository = appointmentRepository;
        this.doctorSlotRepository = doctorSlotRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    public List<AppointmentResponse> getAllAppointments() {

        List<Appointment> appointments =
                appointmentRepository.findAll();

        return appointments.stream()
                .map(this::toAppointmentResponse)
                .toList();
    }

    public List<AppointmentResponse> getMyAppointments() {

        Integer patientId =
                (Integer) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        List<Appointment> appointments =
                appointmentRepository.findByPatientId(patientId);

        return appointments.stream()
                .map(this::toAppointmentResponse)
                .toList();
    }

    @Transactional
    public AppointmentResponse bookAppointment(
            BookAppointmentRequest request) {

        Integer patientId =
                (Integer) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Patient not found"
                        ));

        doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Doctor not found"
                        ));

        DoctorSlot slot = doctorSlotRepository
                .findById(request.getSlotId())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Slot not found"
                        ));

        if (Boolean.TRUE.equals(slot.getIsBooked())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Slot is already booked"
            );
        }

        if (!slot.getDoctor().getDoctorId()
                .equals(request.getDoctorId())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Slot does not belong to this doctor"
            );
        }

        slot.setIsBooked(true);
        doctorSlotRepository.save(slot);

        Appointment appointment = new Appointment();

        appointment.setPatientId(patientId);
        appointment.setDoctorId(request.getDoctorId());
        appointment.setSlotId(request.getSlotId());
        appointment.setBookingTime(LocalDateTime.now());

        Appointment savedAppointment =
                appointmentRepository.save(appointment);

        return toAppointmentResponse(savedAppointment);
    }

    @Transactional
    public void cancelAppointment(Integer appointmentId) {

        Integer patientId =
                (Integer) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        Appointment appointment =
                appointmentRepository
                        .findByAppointmentIdAndPatientId(
                                appointmentId,
                                patientId
                        )
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Appointment not found"
                                ));

        DoctorSlot slot =
                doctorSlotRepository
                        .findById(appointment.getSlotId())
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Slot not found"
                                ));

        // Free the slot
        slot.setIsBooked(false);
        doctorSlotRepository.save(slot);

        // Delete the appointment
        appointmentRepository.delete(appointment);
    }

    private AppointmentResponse toAppointmentResponse(
            Appointment appointment) {

        AppointmentResponse response =
                new AppointmentResponse();

        response.setAppointmentId(
                appointment.getAppointmentId());

        response.setPatientId(
                appointment.getPatientId());

        response.setDoctorId(
                appointment.getDoctorId());

        response.setSlotId(
                appointment.getSlotId());

        response.setBookingTime(
                appointment.getBookingTime());

        // Get doctor
        Doctor doctor = doctorRepository
                .findById(appointment.getDoctorId())
                .orElse(null);

        if (doctor != null) {

            response.setDoctorName(
                    doctor.getName());

            if (doctor.getHospital() != null) {
                response.setHospitalName(
                        doctor.getHospital().getName());
            }
        }

        // Get slot
        DoctorSlot slot = doctorSlotRepository
                .findBySlotId(appointment.getSlotId())
                .orElse(null);

        if (slot != null) {

            response.setSlotTime(
                    slot.getSlotTime());
        }

        return response;
    }
}