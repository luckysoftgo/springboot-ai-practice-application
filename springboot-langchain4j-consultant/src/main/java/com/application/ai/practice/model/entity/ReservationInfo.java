package com.application.ai.practice.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationInfo {
    private Long id;
    private String reservationName;
    private String reservationGender;
    private String reservationPhone;
    private LocalDateTime communicationTime;
    private String reservationProvince;
    private Integer estimatedScore;
    private String reservationDesc;
}
