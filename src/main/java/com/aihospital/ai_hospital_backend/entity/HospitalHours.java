package com.aihospital.ai_hospital_backend.entity;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(
        name = "hospital_hours",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"hospital_id", "day_of_week"}
                )
        }
)
public class HospitalHours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hospital_hours_id")
    private Integer hospitalHoursId;

    @ManyToOne
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @Column(name = "day_of_week")
    private Integer dayOfWeek;

    @Column(name = "opening_time")
    private LocalTime openingTime;

    @Column(name = "closing_time")
    private LocalTime closingTime;

    @Column(name = "is_closed")
    private Boolean isClosed;

    public Integer getHospitalHoursId() {
        return hospitalHoursId;
    }

    public void setHospitalHoursId(Integer hospitalHoursId) {
        this.hospitalHoursId = hospitalHoursId;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }

    public Boolean getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(Boolean closed) {
        isClosed = closed;
    }
}