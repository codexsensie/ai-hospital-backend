package com.aihospital.ai_hospital_backend.service;

import com.aihospital.ai_hospital_backend.entity.HospitalHours;
import com.aihospital.ai_hospital_backend.repository.HospitalHoursRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.time.ZoneId;

@Service
public class HospitalHoursService {

    private final HospitalHoursRepository hospitalHoursRepository;

    public HospitalHoursService(
            HospitalHoursRepository hospitalHoursRepository) {

        this.hospitalHoursRepository = hospitalHoursRepository;
    }

    public boolean isHospitalOpenNow(Integer hospitalId) {

        // 1. Get current date and time
        LocalDateTime now =
                LocalDateTime.now(
                        ZoneId.of("Asia/Kolkata")
                );

        // 2. Get current day
        DayOfWeek dayOfWeek = now.getDayOfWeek();

        // 3. Convert Java day to our database format
        int dbDayOfWeek =
                dayOfWeek.getValue() % 7;

        // 4. Get today's hospital hours
        Optional<HospitalHours> hours =
                hospitalHoursRepository
                        .findByHospitalHospitalIdAndDayOfWeek(
                                hospitalId,
                                dbDayOfWeek
                        );

        // 5. No hours record means closed
        if (hours.isEmpty()) {
            return false;
        }

        HospitalHours hospitalHours =
                hours.get();

        // 6. Explicitly closed
        if (Boolean.TRUE.equals(
                hospitalHours.getIsClosed())) {

            return false;
        }

        // 7. Get opening and closing time
        LocalTime openingTime =
                hospitalHours.getOpeningTime();

        LocalTime closingTime =
                hospitalHours.getClosingTime();

        LocalTime currentTime =
                now.toLocalTime();

        // 8. Check whether current time is within hours
        return !currentTime.isBefore(openingTime)
                && !currentTime.isAfter(closingTime);
    }
}