package com.example.uams.module.apartment.repository;

import com.example.uams.module.apartment.entity.StudentApartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApartmentRepository extends JpaRepository<StudentApartment, Long> {

    // Search by name or address
    @Query("SELECT a FROM StudentApartment a WHERE " +
            "LOWER(a.apartmentName) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(a.address)       LIKE LOWER(CONCAT('%', :q, '%'))")
    List<StudentApartment> search(@Param("q") String query);

    // Report: apartments with available bedrooms
    @Query("SELECT DISTINCT a FROM StudentApartment a " +
            "JOIN Bedroom b ON b.apartment = a " +
            "WHERE b.isOccupied = false")
    List<StudentApartment> findApartmentsWithAvailableBedrooms();

    // Report: apartment for a specific student
    @Query("SELECT DISTINCT a FROM StudentApartment a " +
            "JOIN Bedroom b ON b.apartment = a " +
            "WHERE b.student.studentId = :studentId AND b.isOccupied = true")
    List<StudentApartment> findApartmentsByStudentId(
            @Param("studentId") Long studentId);
}