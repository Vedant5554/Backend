package com.example.uams.module.hall.service;

import com.example.uams.exception.BadRequestException;
import com.example.uams.exception.ResourceNotFoundException;
import com.example.uams.module.hall.dto.*;
import com.example.uams.module.hall.entity.HallPlacement;
import com.example.uams.module.hall.entity.ResidenceHall;
import com.example.uams.module.hall.entity.Room;
import com.example.uams.module.hall.repository.HallPlacementRepository;
import com.example.uams.module.hall.repository.ResidenceHallRepository;
import com.example.uams.module.hall.repository.RoomRepository;
import com.example.uams.module.student.entity.Student;
import com.example.uams.module.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HallService {

    private final ResidenceHallRepository hallRepository;
    private final RoomRepository          roomRepository;
    private final HallPlacementRepository placementRepository;
    private final StudentRepository       studentRepository;

    // ── Hall CRUD ─────────────────────────────────────────────────────────────

    @Transactional
    public HallResponse createHall(HallRequest request) {
        ResidenceHall hall = ResidenceHall.builder()
                .hallName(request.getHallName())
                .address(request.getAddress())
                .totalRooms(request.getTotalRooms())
                .managerName(request.getManagerName())
                .managerPhone(request.getManagerPhone())
                .build();
        return HallResponse.from(hallRepository.save(hall));
    }

    @Transactional(readOnly = true)
    public List<HallResponse> getAllHalls() {
        return hallRepository.findAll().stream()
                .map(HallResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public HallResponse getHallById(Long id) {
        return HallResponse.from(findHallOrThrow(id));
    }

    @Transactional
    public HallResponse updateHall(Long id, HallRequest request) {
        ResidenceHall hall = findHallOrThrow(id);
        hall.setHallName(request.getHallName());
        hall.setAddress(request.getAddress());
        hall.setTotalRooms(request.getTotalRooms());
        hall.setManagerName(request.getManagerName());
        hall.setManagerPhone(request.getManagerPhone());
        return HallResponse.from(hallRepository.save(hall));
    }

    @Transactional
    public void deleteHall(Long id) {
        hallRepository.delete(findHallOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<HallResponse> searchHalls(String query) {
        return hallRepository.search(query).stream()
                .map(HallResponse::from).toList();
    }

    // ── Placement ─────────────────────────────────────────────────────────────

    @Transactional
    public PlacementResponse createPlacement(PlacementRequest request) {
        if (placementRepository.existsByStudent_StudentIdAndIsActiveTrue(
                request.getStudentId())) {
            throw new BadRequestException(
                    "Student " + request.getStudentId()
                            + " already has an active hall placement");
        }

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student", request.getStudentId()));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Room", request.getRoomId()));

        if (!room.getIsAvailable()) {
            throw new BadRequestException(
                    "Room " + room.getRoomNumber() + " is not available");
        }

        HallPlacement placement = HallPlacement.builder()
                .student(student)
                .room(room)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .isActive(true)
                .build();

        room.setIsAvailable(false);
        roomRepository.save(room);

        return PlacementResponse.from(placementRepository.save(placement));
    }

    @Transactional(readOnly = true)
    public List<PlacementResponse> getAllActivePlacements() {
        return placementRepository.findAllActivePlacements().stream()
                .map(PlacementResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<PlacementResponse> getPlacementsByStudent(Long studentId) {
        return placementRepository.findByStudent_StudentId(studentId).stream()
                .map(PlacementResponse::from).toList();
    }

    @Transactional
    public PlacementResponse endPlacement(Long placementId) {
        HallPlacement placement = placementRepository.findById(placementId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Placement", placementId));

        placement.setIsActive(false);

        Room room = placement.getRoom();
        room.setIsAvailable(true);
        roomRepository.save(room);

        return PlacementResponse.from(placementRepository.save(placement));
    }

    // ── Internal helpers ──────────────────────────────────────────────────────

    private ResidenceHall findHallOrThrow(Long id) {
        return hallRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hall", id));
    }
}