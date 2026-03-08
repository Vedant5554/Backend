package com.example.uams.module.report.dto;

import com.example.uams.module.student.entity.StudentCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Report (j): Students who have not supplied next-of-kin details.
 */
@Getter
@AllArgsConstructor
public class NoNextOfKinReport {
    private Long            bannerId;
    private String          fullName;
    private String          email;
    private StudentCategory category;
}