package com.aihospital.ai_hospital_backend.repository;

import com.aihospital.ai_hospital_backend.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @Query("""
           SELECT AVG(r.rating)
           FROM Review r
           WHERE r.hospital.hospitalId = :hospitalId
           """)
    Double findAverageRatingByHospitalId(
            @Param("hospitalId") Integer hospitalId);
}