package com.example.uams.module.adviser.dto;

import com.example.uams.module.adviser.entity.Adviser;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Response body returned for all adviser endpoints.
 */
@Getter
@NoArgsConstructor
public class AdviserResponse {

    private Long   adviserId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String department;

    // ── Static factory ────────────────────────────────────────────────────────

    public static AdviserResponse from(Adviser adviser) {
        AdviserResponse r = new AdviserResponse();
        r.adviserId  = adviser.getAdviserId();
        r.firstName  = adviser.getFirstName();
        r.lastName   = adviser.getLastName();
        r.email      = adviser.getEmail();
        r.phone      = adviser.getPhone();
        r.department = adviser.getDepartment();
        return r;
    }
}