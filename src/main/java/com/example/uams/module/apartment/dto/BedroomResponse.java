package com.example.uams.module.apartment.dto;

import com.example.uams.module.apartment.entity.Bedroom;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class BedroomResponse {

    private Long       bedroomId;
    private Long       apartmentId;
    private String     apartmentName;
    private String     bedroomNumber;
    private BigDecimal monthlyFee;
    private Boolean    isOccupied;
    private Long       studentId;
    private String     studentName;
    private LocalDate  startDate;
    private LocalDate  endDate;

    public static BedroomResponse from(Bedroom b) {
        BedroomResponse r = new BedroomResponse();
        r.bedroomId     = b.getBedroomId();
        r.apartmentId   = b.getApartment().getApartmentId();
        r.apartmentName = b.getApartment().getApartmentName();
        r.bedroomNumber = b.getBedroomNumber();
        r.monthlyFee    = b.getMonthlyFee();
        r.isOccupied    = b.getIsOccupied();
        r.startDate     = b.getStartDate();
        r.endDate       = b.getEndDate();

        if (b.getStudent() != null) {
            r.studentId   = b.getStudent().getStudentId();
            r.studentName = b.getStudent().getFirstName()
                    + " " + b.getStudent().getLastName();
        }
        return r;
    }
}