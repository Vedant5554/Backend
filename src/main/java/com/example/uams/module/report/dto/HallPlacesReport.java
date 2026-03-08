package com.example.uams.module.report.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Report (m): Total number of places in each residence hall.
 */
@Getter
@AllArgsConstructor
public class HallPlacesReport {
    private Long   hallId;
    private String hallName;
    private int    totalPlaces;
    private long   availablePlaces;
}