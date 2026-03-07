package com.example.uams.module.nextofkin.repository;

import com.example.uams.module.nextofkin.entity.NextOfKin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NextOfKinRepository extends JpaRepository<NextOfKin, Long> {

    // All next of kin records for a specific student
    List<NextOfKin> findByStudent_StudentId(Long studentId);

    // Check if a student has any next of kin registered
    boolean existsByStudent_StudentId(Long studentId);

    // Delete all next of kin for a student (used when student is removed)
    void deleteByStudent_StudentId(Long studentId);
}