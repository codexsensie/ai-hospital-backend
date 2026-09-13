package com.aihospital.ai_hospital_backend.controller;

import com.aihospital.ai_hospital_backend.dto.ServiceSearchResponse;
import com.aihospital.ai_hospital_backend.service.ServiceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/services")
public class ServiceController {

    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping
    public List<ServiceSearchResponse> searchServices(
            @RequestParam String name,
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam Double maxDistanceKm) {

        return serviceService.searchServices(
                name,
                latitude,
                longitude,
                maxDistanceKm
        );
    }
}