package com.aihospital.ai_hospital_backend.service;

import com.aihospital.ai_hospital_backend.dto.AIQueryResponse;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AIQueryParserService {

    public AIQueryResponse parseQuery(String query) {

        AIQueryResponse response = new AIQueryResponse();

        String text = query
                .toLowerCase(Locale.ROOT)
                .trim();

        // 1. Detect medical service

        if (text.contains("mri") ||
                text.contains("magnetic resonance")) {

            response.setService("MRI Scan");

        } else if (text.contains("x-ray") ||
                text.contains("xray") ||
                text.contains("x ray") ||
                text.contains("radiograph")) {

            response.setService("X-ray");

        } else if (text.contains("blood test") ||
                text.contains("bloodtest") ||
                text.contains("blood-test") ||
                text.contains("blood examination") ||
                text.contains("blood work") ||
                text.contains("bloodwork")) {

            response.setService("Blood Test");

        } else if (text.contains("ct scan") ||
                text.contains("ctscan") ||
                text.contains("ct scan test") ||
                text.matches(".*\\bct\\b.*")) {

            response.setService("CT Scan");
        }

        // 2. Detect maximum price
        Pattern pricePattern =
                Pattern.compile(
                        "(?:under|below|less than|upto|up to|for|costing|price)\\s*" +
                                "(?:₹|rs\\.?|rupees)?\\s*" +
                                "(\\d+(?:,\\d+)*)",
                        Pattern.CASE_INSENSITIVE
                );

        Matcher priceMatcher =
                pricePattern.matcher(text);

        if (priceMatcher.find()) {

            Integer maxPrice =
                    Integer.parseInt(
                            priceMatcher.group(1).replace(",", "")
                    );

            response.setMaxPrice(maxPrice);
        }

        // 3. Detect maximum distance
        Pattern distancePattern =
                Pattern.compile(
                        "(?:within|under|less than|up to)\\s*" +
                                "(\\d+(?:\\.\\d+)?)\\s*" +
                                "(?:km|kilometer|kilometers|kms)\\b",
                        Pattern.CASE_INSENSITIVE
                );

        Matcher distanceMatcher =
                distancePattern.matcher(text);

        if (distanceMatcher.find()) {

            Double maxDistance =
                    Double.parseDouble(
                            distanceMatcher.group(1)
                    );

            response.setMaxDistanceKm(maxDistance);
        }

        // 4. Detect sorting preference

        if (text.contains("cheapest") ||
                text.contains("lowest price") ||
                text.contains("lowest cost") ||
                text.contains("most affordable") ||
                text.contains("affordable") ||
                text.contains("budget") ||
                text.contains("cheaper")) {

            response.setSortBy("price_asc");

        } else if (text.contains("nearest") ||
                text.contains("closest") ||
                text.contains("closest to me") ||
                text.contains("nearest to me")) {

            response.setSortBy("distance_asc");
        }

        // 5. Detect location preference

        if (text.contains("near me") ||
                text.contains("nearby") ||
                text.contains("close to me") ||
                text.contains("around me") ||
                text.contains("nearby me")) {

            response.setNearMe(true);

        } else {

            response.setNearMe(false);
        }

        // 6. Detect open-now preference

        if (text.contains("open now") ||
                text.contains("currently open") ||
                text.contains("open at the moment") ||
                text.contains("open right now")) {

            response.setOpenNow(true);

        } else {

            response.setOpenNow(false);
        }

        return response;
    }

}