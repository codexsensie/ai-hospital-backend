package com.aihospital.ai_hospital_backend.gemini;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gemini")
public class GeminiController {

    private final GeminiService geminiService;

    public GeminiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @GetMapping("/parse")
    public GeminiSearchQuery parseQuery(
            @RequestParam String query) {

        return geminiService.parseHospitalQuery(query);
    }
}