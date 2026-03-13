package com.example.uams.module.student.dto;

import com.example.uams.module.student.entity.Student;
import com.example.uams.module.student.entity.StudentCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Response body returned for all student endpoints.
 * Never exposes the password field.
 */
@Getter
@NoArgsConstructor
public class StudentResponse {

    private Long            studentId;
    private String          bannerNumber;
    private String          firstName;
    private String          lastName;
    private String          email;
    private LocalDate       dateOfBirth;
    private StudentCategory category;
    private Boolean         waitingList;
    private Boolean         isActive;

    // Address
    private String street;
    private String city;
    private String postcode;

    // Contact & personal
    private String mobilePhone;
    private String gender;
    private String nationality;
    private String specialNeeds;
    private String additionalComments;

    // Academic
    private String major;
    private String minor;

    // Adviser summary (null if not assigned)
    private Long   adviserId;
    private String adviserName;

    // ── Static factory ────────────────────────────────────────────────────────

    public static StudentResponse from(Student student) {
        StudentResponse r = new StudentResponse();
        r.studentId          = student.getStudentId();
        r.bannerNumber       = student.getBannerNumber();
        r.firstName          = student.getFirstName();
        r.lastName           = student.getLastName();
        r.email              = student.getEmail();
        r.dateOfBirth        = student.getDateOfBirth();
        r.category           = student.getCategory();
        r.waitingList        = student.getWaitingList();
        r.isActive           = student.getIsActive();
        r.street             = student.getStreet();
        r.city               = student.getCity();
        r.postcode           = student.getPostcode();
        r.mobilePhone        = student.getMobilePhone();
        r.gender             = student.getGender();
        r.nationality        = student.getNationality();
        r.specialNeeds       = student.getSpecialNeeds();
        r.additionalComments = student.getAdditionalComments();
        r.major              = student.getMajor();
        r.minor              = student.getMinor();

        if (student.getAdviser() != null) {
            r.adviserId   = student.getAdviser().getAdviserId();
            r.adviserName = student.getAdviser().getFirstName()
                    + " " + student.getAdviser().getLastName();
        }
        return r;
    }
}