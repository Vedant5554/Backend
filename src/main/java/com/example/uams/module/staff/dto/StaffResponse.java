package com.example.uams.module.staff.dto;

import com.example.uams.module.staff.entity.ResidenceStaff;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class StaffResponse {

    private Long      staffId;
    private String    firstName;
    private String    lastName;
    private String    email;
    private String    phone;
    private String    role;
    private String    location;
    private LocalDate dateOfBirth;
    private Boolean   isActive;

    // Hall info (null if not assigned)
    private Long   hallId;
    private String hallName;

    public static StaffResponse from(ResidenceStaff s) {
        StaffResponse r = new StaffResponse();
        r.staffId      = s.getStaffId();
        r.firstName    = s.getFirstName();
        r.lastName     = s.getLastName();
        r.email        = s.getEmail();
        r.phone        = s.getPhone();
        r.role         = s.getRole();
        r.location     = s.getLocation();
        r.dateOfBirth  = s.getDateOfBirth();
        r.isActive     = s.getIsActive();

        if (s.getHall() != null) {
            r.hallId   = s.getHall().getHallId();
            r.hallName = s.getHall().getHallName();
        }
        return r;
    }
}