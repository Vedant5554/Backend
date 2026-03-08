package com.example.uams.module.hall.repository;

import com.example.uams.module.hall.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    // All rooms in a hall
    List<Room> findByHall_HallId(Long hallId);

    // Available rooms in a hall
    List<Room> findByHall_HallIdAndIsAvailableTrue(Long hallId);

    // All available rooms across all halls
    List<Room> findByIsAvailableTrue();

    // Report: rooms by type
    List<Room> findByRoomType(String roomType);

    // Check room number uniqueness within a hall
    boolean existsByHall_HallIdAndRoomNumber(Long hallId, String roomNumber);

    // Count available rooms in a hall
    @Query("SELECT COUNT(r) FROM Room r WHERE r.hall.hallId = :hallId " +
            "AND r.isAvailable = true")
    long countAvailableRooms(@Param("hallId") Long hallId);
}