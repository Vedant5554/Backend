package com.example.uams.module.hall.repository;

import com.example.uams.module.hall.entity.ResidenceHall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResidenceHallRepository extends JpaRepository<ResidenceHall, Long> {

    // Search by name or address
    @Query("SELECT h FROM ResidenceHall h WHERE " +
            "LOWER(h.hallName) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(h.address)  LIKE LOWER(CONCAT('%', :q, '%'))")
    List<ResidenceHall> search(@Param("q") String query);

    // Report: halls with available rooms
    @Query("SELECT DISTINCT h FROM ResidenceHall h " +
            "JOIN Room r ON r.hall = h " +
            "WHERE r.isAvailable = true")
    List<ResidenceHall> findHallsWithAvailableRooms();

    // Report: hall details for a specific student placement
    @Query("SELECT h FROM ResidenceHall h " +
            "JOIN Room r ON r.hall = h " +
            "JOIN HallPlacement p ON p.room = r " +
            "WHERE p.student.studentId = :studentId AND p.isActive = true")
    List<ResidenceHall> findHallsByStudentId(@Param("studentId") Long studentId);
}