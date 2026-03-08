package com.example.uams.module.hall.dto;

import com.example.uams.module.hall.entity.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class RoomResponse {

    private Long       roomId;
    private Long       hallId;
    private String     hallName;
    private String     roomNumber;
    private String     roomType;
    private BigDecimal monthlyFee;
    private Boolean    isAvailable;

    public static RoomResponse from(Room room) {
        RoomResponse r = new RoomResponse();
        r.roomId      = room.getRoomId();
        r.hallId      = room.getHall().getHallId();
        r.hallName    = room.getHall().getHallName();
        r.roomNumber  = room.getRoomNumber();
        r.roomType    = room.getRoomType();
        r.monthlyFee  = room.getMonthlyFee();
        r.isAvailable = room.getIsAvailable();
        return r;
    }
}