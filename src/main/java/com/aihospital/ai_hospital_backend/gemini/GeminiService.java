package com.aihospital.ai_hospital_backend.gemini;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.stereotype.Service;

@Service
public class GeminiService {

    private final Client client;
    private final ObjectMapper objectMapper;

    public GeminiService() {
        this.client = new Client();
        this.objectMapper = new ObjectMapper();
    }

    public GeminiSearchQuery parseHospitalQuery(String query) {

        String prompt = """
                You are an AI assistant for a hospital search application.

                Your job is to convert a user's natural-language medical
                search query into JSON search parameters.

                Supported medical services are:
                - X-ray
                - MRI Scan
                - Blood Test
                - CT Scan

                Return ONLY valid JSON.
                Do not add markdown.
                Do not add explanations.

                JSON format:

                {
                  "service": "X-ray | MRI Scan | Blood Test | CT Scan | null",
                  "maxPrice": number or null,
                  "maxDistanceKm": number or null,
                  "sortBy": "price_asc | distance_asc | null",
                  "nearMe": true or false,
                  "openNow": true or false
                }

                Rules:

                1. Convert synonyms to the supported service names.
                   - xray, x-ray, x ray, radiograph -> X-ray
                   - mri, mri scan, magnetic resonance -> MRI Scan
                   - blood test, bloodwork, blood work -> Blood Test
                   - ct, ct scan, computed tomography -> CT Scan

                2. Extract maximum price.
                   Examples:
                   "under 1000" -> maxPrice = 1000
                   "below ₹2000" -> maxPrice = 2000
                   "less than 500" -> maxPrice = 500

                3. Extract maximum distance.
                   Examples:
                   "within 10 km" -> maxDistanceKm = 10
                   "under 5 km" -> maxDistanceKm = 5

                4. If the user says:
                   - cheapest
                   - cheapest option
                   - lowest price
                   - affordable
                   - budget
                   then sortBy = "price_asc".

                5. If the user says:
                   - nearest
                   - closest
                   then sortBy = "distance_asc".

                6. If the query contains:
                   "near me", "nearby", "close to me", "around me"
                   then nearMe = true.
                   Otherwise nearMe = false.

                7. If the query contains:
                   "open now", "currently open", "open right now"
                   then openNow = true.
                   Otherwise openNow = false.

                8. If no maximum price is mentioned, maxPrice must be null.

                9. If no maximum distance is mentioned, maxDistanceKm must be null.

                10. If no sorting preference is mentioned, sortBy must be null.

                11. If no supported medical service can be identified,
                    service must be null.

                User query:
                """ + query;

        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-3.8-flash",
                        prompt,
                        null
                );

        try {
            return objectMapper.readValue(
                    response.text(),
                    GeminiSearchQuery.class
            );
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to parse Gemini response: "
                            + response.text(),
                    e
            );
        }
    }
}