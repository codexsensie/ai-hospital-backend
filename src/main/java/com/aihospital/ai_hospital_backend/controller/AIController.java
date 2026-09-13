package com.aihospital.ai_hospital_backend.controller;

import com.aihospital.ai_hospital_backend.dto.AISearchRequest;
import com.aihospital.ai_hospital_backend.dto.AISearchResponse;
import com.aihospital.ai_hospital_backend.dto.AIQueryResponse;
import com.aihospital.ai_hospital_backend.dto.ServiceSearchResponse;
import com.aihospital.ai_hospital_backend.gemini.GeminiSearchQuery;
import com.aihospital.ai_hospital_backend.gemini.GeminiService;
import com.aihospital.ai_hospital_backend.service.AIQueryParserService;
import com.aihospital.ai_hospital_backend.service.ServiceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/ai")
public class AIController {

    private final GeminiService geminiService;
    private final AIQueryParserService aiQueryParserService;
    private final ServiceService serviceService;

    public AIController(
            GeminiService geminiService,
            AIQueryParserService aiQueryParserService,
            ServiceService serviceService) {

        this.geminiService = geminiService;
        this.aiQueryParserService = aiQueryParserService;
        this.serviceService = serviceService;
    }

    @PostMapping("/search")
    public AISearchResponse search(
            @Valid @RequestBody AISearchRequest request) {

        String query = request.getQuery();

        Double latitude = request.getLatitude();
        Double longitude = request.getLongitude();

        GeminiSearchQuery parsedQuery;

        // Try Gemini first
        try {

            parsedQuery =
                    geminiService.parseHospitalQuery(query);

            System.out.println(
                    "Using Gemini AI search."
            );

        } catch (Exception e) {

            // Gemini unavailable → fallback
            System.out.println(
                    "Gemini unavailable. Using rule-based search."
            );

            AIQueryResponse ruleBasedQuery =
                    aiQueryParserService.parseQuery(query);

            parsedQuery =
                    convertToGeminiSearchQuery(
                            ruleBasedQuery
                    );
        }

        // Validate service
        if (parsedQuery.getService() == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Please specify a medical service"
            );
        }

        double maxDistance;

        if (parsedQuery.getMaxDistanceKm() != null) {

            maxDistance =
                    parsedQuery.getMaxDistanceKm();

        } else if (Boolean.TRUE.equals(
                parsedQuery.getNearMe())) {

            maxDistance = 30.0;

        } else {

            maxDistance = 20.0;
        }

        List<ServiceSearchResponse> results =
                serviceService.searchServicesForAI(
                        parsedQuery.getService(),
                        latitude,
                        longitude,
                        maxDistance,
                        parsedQuery.getMaxPrice(),
                        parsedQuery.getSortBy(),
                        parsedQuery.getOpenNow()
                );

        AISearchResponse response =
                new AISearchResponse();

        response.setQuery(query);
        response.setResults(results);

        if (results.isEmpty()) {

            response.setMessage(
                    "No matching hospitals found"
            );

        } else {

            response.setMessage(
                    "Found "
                            + results.size()
                            + " matching hospital"
                            + (results.size() > 1
                            ? "s"
                            : "")
            );
        }

        return response;
    }

    private GeminiSearchQuery convertToGeminiSearchQuery(
            AIQueryResponse ruleBasedQuery) {

        GeminiSearchQuery result =
                new GeminiSearchQuery();

        result.setService(
                ruleBasedQuery.getService()
        );

        result.setMaxPrice(
                ruleBasedQuery.getMaxPrice()
        );

        result.setMaxDistanceKm(
                ruleBasedQuery.getMaxDistanceKm()
        );

        result.setSortBy(
                ruleBasedQuery.getSortBy()
        );

        result.setNearMe(
                ruleBasedQuery.getNearMe()
        );

        result.setOpenNow(
                ruleBasedQuery.getOpenNow()
        );

        return result;
    }
}