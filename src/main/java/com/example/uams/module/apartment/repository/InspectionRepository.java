package com.example.uams.module.apartment.repository;

import com.example.uams.module.apartment.entity.ApartmentInspection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InspectionRepository extends JpaRepository<ApartmentInspection, Long> {

    // All inspections for an apartment
    List<ApartmentInspection> findByApartment_ApartmentId(Long apartmentId);

    // Inspections by status
    List<ApartmentInspection> findByStatus(String status);

    // Most recent inspection for an apartment (sorted desc)
    List<ApartmentInspection> findByApartment_ApartmentIdOrderByInspectionDateDesc(
            Long apartmentId);
}