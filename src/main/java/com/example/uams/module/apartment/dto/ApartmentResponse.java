package com.example.uams.module.apartment.dto;

import com.example.uams.module.apartment.entity.StudentApartment;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApartmentResponse {

    private Long    apartmentId;
    private String  flatNumber;
    private String  apartmentName;
    private String  address;
    private Integer totalBedrooms;
    private String  managerName;
    private String  managerPhone;

    public static ApartmentResponse from(StudentApartment a) {
        ApartmentResponse r = new ApartmentResponse();
        r.apartmentId   = a.getApartmentId();
        r.flatNumber    = a.getFlatNumber();
        r.apartmentName = a.getApartmentName();
        r.address       = a.getAddress();
        r.totalBedrooms = a.getTotalBedrooms();
        r.managerName   = a.getManagerName();
        r.managerPhone  = a.getManagerPhone();
        return r;
    }
}