package com.aihospital.ai_hospital_backend.repository;

import com.aihospital.ai_hospital_backend.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Service, Integer> {

    List<Service> findByServiceNameContainingIgnoreCase(String serviceName);

}