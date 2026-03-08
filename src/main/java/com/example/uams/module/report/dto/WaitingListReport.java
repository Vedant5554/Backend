package com.example.uams.module.report.dto;

import com.example.uams.module.student.entity.StudentCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Report (h): Students currently on the waiting list.
 */
@Getter
@AllArgsConstructor
public class WaitingListReport {
    private Long            bannerId;
    private String          fullName;
    private String          email;
    private StudentCategory category;
}