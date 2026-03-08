package com.example.uams.module.hall.dto;

import com.example.uams.module.hall.entity.HallPlacement;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Response body for all placement endpoints.
 */
@Getter
@NoArgsConstructor
public class PlacementResponse {

    private Long      placementId;
    private Long      studentId;
    private String    studentName;
    private Long      roomId;
    private String    roomNumber;
    private Long      hallId;
    private String    hallName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean   isActive;

    public static PlacementResponse from(HallPlacement p) {
        PlacementResponse r = new PlacementResponse();
        r.placementId = p.getPlacementId();
        r.studentId   = p.getStudent().getStudentId();
        r.studentName = p.getStudent().getFirstName()
                + " " + p.getStudent().getLastName();
        r.roomId      = p.getRoom().getRoomId();
        r.roomNumber  = p.getRoom().getRoomNumber();
        r.hallId      = p.getRoom().getHall().getHallId();
        r.hallName    = p.getRoom().getHall().getHallName();
        r.startDate   = p.getStartDate();
        r.endDate     = p.getEndDate();
        r.isActive    = p.getIsActive();
        return r;
    }
}