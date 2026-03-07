package com.example.uams.module.nextofkin.dto;

import com.example.uams.module.nextofkin.entity.NextOfKin;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Response body for all next of kin endpoints.
 */
@Getter
@NoArgsConstructor
public class NextOfKinResponse {

    private Long   nokId;
    private Long   studentId;
    private String studentName;
    private String fullName;
    private String relationship;
    private String phone;
    private String email;

    // ── Static factory ────────────────────────────────────────────────────────

    public static NextOfKinResponse from(NextOfKin nok) {
        NextOfKinResponse r = new NextOfKinResponse();
        r.nokId        = nok.getNokId();
        r.studentId    = nok.getStudent().getStudentId();
        r.studentName  = nok.getStudent().getFirstName()
                + " " + nok.getStudent().getLastName();
        r.fullName     = nok.getFullName();
        r.relationship = nok.getRelationship();
        r.phone        = nok.getPhone();
        r.email        = nok.getEmail();
        return r;
    }
}