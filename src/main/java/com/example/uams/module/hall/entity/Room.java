package com.example.uams.module.hall.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Maps to the 'rooms' table.
 *
 * Relationships:
 *  - ManyToOne  → ResidenceHall (a room belongs to one hall)
 *  - OneToMany  → HallPlacement (a room can have placement records over time)
 */
@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id", nullable = false)
    private ResidenceHall hall;

    @Column(name = "room_number", nullable = false, length = 20)
    private String roomNumber;

    @Column(name = "place_number", unique = true, length = 50)
    private String placeNumber;

    @Column(name = "room_type", length = 100)
    private String roomType;            // e.g. Single, Double, En-suite

    @Column(name = "monthly_fee", precision = 10, scale = 2)
    private BigDecimal monthlyFee;

    @Builder.Default
    @Column(name = "is_available")
    private Boolean isAvailable = true;
}