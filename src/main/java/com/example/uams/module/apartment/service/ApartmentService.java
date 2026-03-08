package com.example.uams.module.apartment.service;

import com.example.uams.exception.BadRequestException;
import com.example.uams.exception.ResourceNotFoundException;
import com.example.uams.module.apartment.dto.*;
import com.example.uams.module.apartment.entity.ApartmentInspection;
import com.example.uams.module.apartment.entity.Bedroom;
import com.example.uams.module.apartment.entity.StudentApartment;
import com.example.uams.module.apartment.repository.ApartmentRepository;
import com.example.uams.module.apartment.repository.BedroomRepository;
import com.example.uams.module.apartment.repository.InspectionRepository;
import com.example.uams.module.student.entity.Student;
import com.example.uams.module.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApartmentService {

    private final ApartmentRepository  apartmentRepository;
    private final BedroomRepository    bedroomRepository;
    private final InspectionRepository inspectionRepository;
    private final StudentRepository    studentRepository;

    // ── Apartment CRUD ────────────────────────────────────────────────────────

    @Transactional
    public ApartmentResponse create(ApartmentRequest request) {
        StudentApartment apartment = StudentApartment.builder()
                .apartmentName(request.getApartmentName())
                .address(request.getAddress())
                .totalBedrooms(request.getTotalBedrooms())
                .managerName(request.getManagerName())
                .managerPhone(request.getManagerPhone())
                .build();
        return ApartmentResponse.from(apartmentRepository.save(apartment));
    }

    @Transactional(readOnly = true)
    public List<ApartmentResponse> getAll() {
        return apartmentRepository.findAll().stream()
                .map(ApartmentResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public ApartmentResponse getById(Long id) {
        return ApartmentResponse.from(findApartmentOrThrow(id));
    }

    @Transactional
    public ApartmentResponse update(Long id, ApartmentRequest request) {
        StudentApartment apartment = findApartmentOrThrow(id);
        apartment.setApartmentName(request.getApartmentName());
        apartment.setAddress(request.getAddress());
        apartment.setTotalBedrooms(request.getTotalBedrooms());
        apartment.setManagerName(request.getManagerName());
        apartment.setManagerPhone(request.getManagerPhone());
        return ApartmentResponse.from(apartmentRepository.save(apartment));
    }

    @Transactional
    public void delete(Long id) {
        apartmentRepository.delete(findApartmentOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<ApartmentResponse> search(String query) {
        return apartmentRepository.search(query).stream()
                .map(ApartmentResponse::from).toList();
    }

    // ── Bedroom CRUD ──────────────────────────────────────────────────────────

    @Transactional
    public BedroomResponse createBedroom(Long apartmentId, BedroomRequest request) {
        StudentApartment apartment = findApartmentOrThrow(apartmentId);

        if (bedroomRepository.existsByApartment_ApartmentIdAndBedroomNumber(
                apartmentId, request.getBedroomNumber())) {
            throw new BadRequestException(
                    "Bedroom number " + request.getBedroomNumber()
                            + " already exists in this apartment");
        }

        Bedroom bedroom = Bedroom.builder()
                .apartment(apartment)
                .bedroomNumber(request.getBedroomNumber())
                .monthlyFee(request.getMonthlyFee())
                .isOccupied(false)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        // Optionally assign to student on creation
        if (request.getStudentId() != null) {
            Student student = findStudentOrThrow(request.getStudentId());
            bedroom.setStudent(student);
            bedroom.setIsOccupied(true);
        }

        return BedroomResponse.from(bedroomRepository.save(bedroom));
    }

    @Transactional(readOnly = true)
    public List<BedroomResponse> getBedroomsByApartment(Long apartmentId) {
        findApartmentOrThrow(apartmentId);
        return bedroomRepository.findByApartment_ApartmentId(apartmentId).stream()
                .map(BedroomResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<BedroomResponse> getAvailableBedrooms(Long apartmentId) {
        findApartmentOrThrow(apartmentId);
        return bedroomRepository
                .findByApartment_ApartmentIdAndIsOccupiedFalse(apartmentId).stream()
                .map(BedroomResponse::from).toList();
    }

    @Transactional
    public BedroomResponse updateBedroom(Long apartmentId, Long bedroomId,
                                         BedroomRequest request) {
        Bedroom bedroom = findBedroomOrThrow(bedroomId);

        if (!bedroom.getBedroomNumber().equals(request.getBedroomNumber())
                && bedroomRepository.existsByApartment_ApartmentIdAndBedroomNumber(
                apartmentId, request.getBedroomNumber())) {
            throw new BadRequestException(
                    "Bedroom number " + request.getBedroomNumber()
                            + " already exists in this apartment");
        }

        bedroom.setBedroomNumber(request.getBedroomNumber());
        bedroom.setMonthlyFee(request.getMonthlyFee());
        bedroom.setStartDate(request.getStartDate());
        bedroom.setEndDate(request.getEndDate());

        if (request.getStudentId() != null) {
            Student student = findStudentOrThrow(request.getStudentId());
            bedroom.setStudent(student);
            bedroom.setIsOccupied(true);
        }

        return BedroomResponse.from(bedroomRepository.save(bedroom));
    }

    @Transactional
    public void deleteBedroom(Long bedroomId) {
        bedroomRepository.delete(findBedroomOrThrow(bedroomId));
    }

    // ── Inspection CRUD ───────────────────────────────────────────────────────

    @Transactional
    public InspectionResponse createInspection(Long apartmentId,
                                               InspectionRequest request) {
        StudentApartment apartment = findApartmentOrThrow(apartmentId);

        ApartmentInspection inspection = ApartmentInspection.builder()
                .apartment(apartment)
                .inspectionDate(request.getInspectionDate())
                .inspectorName(request.getInspectorName())
                .remarks(request.getRemarks())
                .status(request.getStatus())
                .build();

        return InspectionResponse.from(inspectionRepository.save(inspection));
    }

    @Transactional(readOnly = true)
    public List<InspectionResponse> getInspectionsByApartment(Long apartmentId) {
        findApartmentOrThrow(apartmentId);
        return inspectionRepository
                .findByApartment_ApartmentIdOrderByInspectionDateDesc(apartmentId)
                .stream().map(InspectionResponse::from).toList();
    }

    @Transactional
    public InspectionResponse updateInspection(Long inspectionId,
                                               InspectionRequest request) {
        ApartmentInspection inspection = inspectionRepository.findById(inspectionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inspection", inspectionId));

        inspection.setInspectionDate(request.getInspectionDate());
        inspection.setInspectorName(request.getInspectorName());
        inspection.setRemarks(request.getRemarks());
        inspection.setStatus(request.getStatus());

        return InspectionResponse.from(inspectionRepository.save(inspection));
    }

    @Transactional
    public void deleteInspection(Long inspectionId) {
        ApartmentInspection inspection = inspectionRepository.findById(inspectionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inspection", inspectionId));
        inspectionRepository.delete(inspection);
    }

    // ── Internal helpers ──────────────────────────────────────────────────────

    private StudentApartment findApartmentOrThrow(Long id) {
        return apartmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Apartment", id));
    }

    private Bedroom findBedroomOrThrow(Long id) {
        return bedroomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bedroom", id));
    }

    private Student findStudentOrThrow(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", id));
    }
}