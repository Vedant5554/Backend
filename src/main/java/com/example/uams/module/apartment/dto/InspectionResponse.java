package com.example.uams.module.apartment.dto;

import com.example.uams.module.apartment.entity.ApartmentInspection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class InspectionResponse {

    private Long      inspectionId;
    private Long      apartmentId;
    private String    apartmentName;
    private LocalDate inspectionDate;
    private String    inspectorName;
    private String    remarks;
    private String    status;

    public static InspectionResponse from(ApartmentInspection i) {
        InspectionResponse r = new InspectionResponse();
        r.inspectionId   = i.getInspectionId();
        r.apartmentId    = i.getApartment().getApartmentId();
        r.apartmentName  = i.getApartment().getApartmentName();
        r.inspectionDate = i.getInspectionDate();
        r.inspectorName  = i.getInspectorName();
        r.remarks        = i.getRemarks();
        r.status         = i.getStatus();
        return r;
    }
}