package com.example.uams.module.report.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * Report (l): Minimum, maximum, and average monthly rent for rooms in halls.
 */
@Getter
@AllArgsConstructor
public class RentStatisticsReport {
    private BigDecimal minimumRent;
    private BigDecimal maximumRent;
    private BigDecimal averageRent;
    private int        totalRooms;
}