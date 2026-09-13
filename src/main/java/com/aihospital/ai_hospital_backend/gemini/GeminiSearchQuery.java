package com.aihospital.ai_hospital_backend.gemini;

public class GeminiSearchQuery {

    private String service;

    private Integer maxPrice;

    private Double maxDistanceKm;

    private String sortBy;

    private Boolean nearMe;

    private Boolean openNow;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Integer getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Integer maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Double getMaxDistanceKm() {
        return maxDistanceKm;
    }

    public void setMaxDistanceKm(Double maxDistanceKm) {
        this.maxDistanceKm = maxDistanceKm;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Boolean getNearMe() {
        return nearMe;
    }

    public void setNearMe(Boolean nearMe) {
        this.nearMe = nearMe;
    }

    public Boolean getOpenNow() {
        return openNow;
    }

    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }
}