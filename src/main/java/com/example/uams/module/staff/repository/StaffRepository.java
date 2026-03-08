package com.example.uams.module.staff.repository;

import com.example.uams.module.staff.entity.ResidenceStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<ResidenceStaff, Long> {

    Optional<ResidenceStaff> findByEmail(String email);

    boolean existsByEmail(String email);

    // All staff in a specific hall
    List<ResidenceStaff> findByHall_HallId(Long hallId);

    // All active staff
    List<ResidenceStaff> findByIsActiveTrue();

    // Staff by role
    List<ResidenceStaff> findByRole(String role);

    // Staff by hall and role (e.g. find manager of a hall)
    List<ResidenceStaff> findByHall_HallIdAndRole(Long hallId, String role);

    // Search by name
    @Query("SELECT s FROM ResidenceStaff s WHERE " +
            "LOWER(s.firstName) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(s.lastName)  LIKE LOWER(CONCAT('%', :q, '%'))")
    List<ResidenceStaff> search(@Param("q") String query);

    // Report: all staff with their hall details
    @Query("SELECT s FROM ResidenceStaff s LEFT JOIN FETCH s.hall " +
            "WHERE s.isActive = true")
    List<ResidenceStaff> findAllActiveWithHall();
}