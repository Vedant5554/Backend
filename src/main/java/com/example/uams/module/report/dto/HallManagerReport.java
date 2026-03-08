package com.example.uams.module.report.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Report (a): Manager name and telephone for each hall of residence.
 */
@Getter
@AllArgsConstructor
public class HallManagerReport {
    private String hallName;
    private String managerName;
    private String phone;
}