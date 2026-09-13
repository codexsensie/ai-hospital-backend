package com.aihospital.ai_hospital_backend.dto;

import java.util.List;

public class AISearchResponse {

    private String query;
    private String message;
    private List<ServiceSearchResponse> results;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ServiceSearchResponse> getResults() {
        return results;
    }

    public void setResults(List<ServiceSearchResponse> results) {
        this.results = results;
    }
}