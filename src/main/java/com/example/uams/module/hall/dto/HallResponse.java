package com.example.uams.module.hall.dto;

import com.example.uams.module.hall.entity.ResidenceHall;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HallResponse {

    private Long    hallId;
    private String  hallName;
    private String  address;
    private String  telephoneNumber;
    private Integer totalRooms;
    private String  managerName;
    private String  managerPhone;

    public static HallResponse from(ResidenceHall hall) {
        HallResponse r = new HallResponse();
        r.hallId          = hall.getHallId();
        r.hallName        = hall.getHallName();
        r.address         = hall.getAddress();
        r.telephoneNumber = hall.getTelephoneNumber();
        r.totalRooms      = hall.getTotalRooms();
        r.managerName     = hall.getManagerName();
        r.managerPhone    = hall.getManagerPhone();
        return r;
    }
}