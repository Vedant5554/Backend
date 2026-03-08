package com.example.uams.module.apartment.repository;

import com.example.uams.module.apartment.entity.Bedroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BedroomRepository extends JpaRepository<Bedroom, Long> {

    // All bedrooms in an apartment
    List<Bedroom> findByApartment_ApartmentId(Long apartmentId);

    // Available bedrooms in an apartment
    List<Bedroom> findByApartment_ApartmentIdAndIsOccupiedFalse(Long apartmentId);

    // Bedrooms assigned to a student
    List<Bedroom> findByStudent_StudentId(Long studentId);

    // Check bedroom number uniqueness within apartment
    boolean existsByApartment_ApartmentIdAndBedroomNumber(
            Long apartmentId, String bedroomNumber);

    // Check if student already has a bedroom in this apartment
    boolean existsByApartment_ApartmentIdAndStudent_StudentId(
            Long apartmentId, Long studentId);
}