package com.example.uams.module.hall.repository;

import com.example.uams.module.hall.entity.HallPlacement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HallPlacementRepository extends JpaRepository<HallPlacement, Long> {

    // Active placements for a student
    List<HallPlacement> findByStudent_StudentIdAndIsActiveTrue(Long studentId);

    // All placements for a student (history)
    List<HallPlacement> findByStudent_StudentId(Long studentId);

    // All placements for a room
    List<HallPlacement> findByRoom_RoomId(Long roomId);

    // Active placement for a specific room
    Optional<HallPlacement> findByRoom_RoomIdAndIsActiveTrue(Long roomId);

    // Check if student already has an active placement
    boolean existsByStudent_StudentIdAndIsActiveTrue(Long studentId);

    // Report: all active placements with student + room details
    @Query("SELECT p FROM HallPlacement p " +
            "JOIN FETCH p.student " +
            "JOIN FETCH p.room r " +
            "JOIN FETCH r.hall " +
            "WHERE p.isActive = true")
    List<HallPlacement> findAllActivePlacements();

    // Report: placements for a specific hall
    @Query("SELECT p FROM HallPlacement p " +
            "JOIN FETCH p.room r " +
            "WHERE r.hall.hallId = :hallId AND p.isActive = true")
    List<HallPlacement> findActivePlacementsByHall(@Param("hallId") Long hallId);
}