package com.aihospital.ai_hospital_backend.service;

import com.aihospital.ai_hospital_backend.dto.DoctorResponse;
import com.aihospital.ai_hospital_backend.entity.Doctor;
import com.aihospital.ai_hospital_backend.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<DoctorResponse> getAllDoctors() {

        List<Doctor> doctors =
                doctorRepository.findAll();

        System.out.println(
                "Number of doctors found: "
                        + doctors.size()
        );

        return doctors.stream()
                .map(doctor -> {

                    DoctorResponse response = new DoctorResponse();

                    response.setDoctorId(doctor.getDoctorId());
                    response.setName(doctor.getName());
                    response.setExperience(doctor.getExperience());
                    response.setConsultationFee(
                            doctor.getConsultationFee()
                    );

                    response.setHospitalId(
                            doctor.getHospital().getHospitalId()
                    );

                    response.setHospitalName(
                            doctor.getHospital().getName()
                    );

                    response.setDepartmentId(
                            doctor.getDepartment().getDepartmentId()
                    );

                    response.setDepartmentName(
                            doctor.getDepartment().getName()
                    );

                    return response;
                })
                .toList();
    }
}