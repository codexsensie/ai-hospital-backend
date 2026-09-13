package com.aihospital.ai_hospital_backend.service;

import com.aihospital.ai_hospital_backend.dto.ServiceSearchResponse;
import com.aihospital.ai_hospital_backend.entity.Service;
import com.aihospital.ai_hospital_backend.repository.ReviewRepository;
import com.aihospital.ai_hospital_backend.repository.ServiceRepository;
import com.aihospital.ai_hospital_backend.util.DistanceUtil;
import com.aihospital.ai_hospital_backend.service.HospitalHoursService;

import java.util.List;

@org.springframework.stereotype.Service
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final ReviewRepository reviewRepository;
    private final HospitalHoursService hospitalHoursService;

    public ServiceService(
            ServiceRepository serviceRepository,
            ReviewRepository reviewRepository,
            HospitalHoursService hospitalHoursService) {

        this.serviceRepository = serviceRepository;
        this.reviewRepository = reviewRepository;
        this.hospitalHoursService = hospitalHoursService;
    }

    public List<ServiceSearchResponse> searchServices(
            String serviceName,
            Double userLatitude,
            Double userLongitude,
            Double maxDistanceKm) {

        // Normalize the service name
        serviceName = normalizeServiceName(serviceName);

        // Find matching services
        List<Service> services =
                serviceRepository.findByServiceNameContainingIgnoreCase(serviceName);

        return services.stream()
                .map(service -> {

                    ServiceSearchResponse response =
                            new ServiceSearchResponse();

                    // Hospital information
                    response.setHospitalId(
                            service.getHospital().getHospitalId());

                    response.setHospitalName(
                            service.getHospital().getName());

                    response.setAddress(
                            service.getHospital().getAddress());

                    response.setCity(
                            service.getHospital().getCity());

                    response.setPhone(
                            service.getHospital().getPhone());

                    // Average hospital rating
                    response.setRating(
                            reviewRepository.findAverageRatingByHospitalId(
                                    service.getHospital().getHospitalId()));

                    // Hospital location
                    response.setLatitude(
                            service.getHospital().getLatitude());

                    response.setLongitude(
                            service.getHospital().getLongitude());

                    // Service information
                    response.setServiceName(
                            service.getServiceName());

                    response.setPrice(
                            service.getPrice());

                    response.setDurationMinutes(
                            service.getDurationMinutes());

                    // Calculate distance
                    double distance = DistanceUtil.calculateDistance(
                            userLatitude,
                            userLongitude,
                            service.getHospital().getLatitude(),
                            service.getHospital().getLongitude()
                    );

                    // Round distance to 2 decimal places
                    distance = Math.round(distance * 100.0) / 100.0;

                    response.setDistanceKm(distance);

                    response.setOpenNow(
                            hospitalHoursService.isHospitalOpenNow(
                                    service.getHospital().getHospitalId()
                            )
                    );

                    return response;
                })

                // Keep only hospitals within requested distance
                .filter(response ->
                        response.getDistanceKm() <= maxDistanceKm)

                // Nearest hospital first
                .sorted((a, b) ->
                        Double.compare(
                                a.getDistanceKm(),
                                b.getDistanceKm()))

                .toList();
    }

    public List<ServiceSearchResponse> searchServicesForAI(
            String serviceName,
            Double userLatitude,
            Double userLongitude,
            Double maxDistanceKm,
            Integer maxPrice,
            String sortBy,
            Boolean openNow) {

        List<ServiceSearchResponse> results =
                searchServices(
                        serviceName,
                        userLatitude,
                        userLongitude,
                        maxDistanceKm
                );

        // Filter by maximum price
        if (maxPrice != null) {
            results = results.stream()
                    .filter(result ->
                            result.getPrice() != null &&
                                    result.getPrice() <= maxPrice
                    )
                    .toList();
        }

        if (Boolean.TRUE.equals(openNow)) {

            results = results.stream()
                    .filter(result ->
                            hospitalHoursService.isHospitalOpenNow(
                                    result.getHospitalId()
                            )
                    )
                    .toList();
        }

        // Sort according to AI request
        if ("price_asc".equals(sortBy)) {

            results = results.stream()
                    .sorted((a, b) ->
                            Integer.compare(
                                    a.getPrice(),
                                    b.getPrice()
                            ))
                    .toList();

        } else if ("distance_asc".equals(sortBy)) {

            results = results.stream()
                    .sorted((a, b) ->
                            Double.compare(
                                    a.getDistanceKm(),
                                    b.getDistanceKm()
                            ))
                    .toList();
        }

        return results;
    }


    // Normalize user input such as:
    // xray, Xray, X-ray, X ray, x ray
    // into "X-ray"
    private String normalizeServiceName(String serviceName) {

        String normalized = serviceName
                .trim()
                .toLowerCase()
                .replace("-", "")
                .replace(" ", "");

        if (normalized.equals("xray")) {
            return "X-ray";
        }

        if (normalized.equals("mri") ||
                normalized.equals("mriscan")) {
            return "MRI Scan";
        }

        if (normalized.equals("bloodtest")) {
            return "Blood Test";
        }

        if (normalized.equals("ct") ||
                normalized.equals("ctscan")) {
            return "CT Scan";
        }

        return serviceName.trim();
    }
}