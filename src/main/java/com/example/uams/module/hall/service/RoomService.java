package com.example.uams.module.hall.service;

import com.example.uams.exception.BadRequestException;
import com.example.uams.exception.ResourceNotFoundException;
import com.example.uams.module.hall.dto.RoomRequest;
import com.example.uams.module.hall.dto.RoomResponse;
import com.example.uams.module.hall.entity.ResidenceHall;
import com.example.uams.module.hall.entity.Room;
import com.example.uams.module.hall.repository.ResidenceHallRepository;
import com.example.uams.module.hall.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository          roomRepository;
    private final ResidenceHallRepository hallRepository;

    // ── Create ────────────────────────────────────────────────────────────────

    @Transactional
    public RoomResponse create(Long hallId, RoomRequest request) {
        ResidenceHall hall = findHallOrThrow(hallId);

        if (roomRepository.existsByHall_HallIdAndRoomNumber(
                hallId, request.getRoomNumber())) {
            throw new BadRequestException(
                    "Room number " + request.getRoomNumber()
                            + " already exists in this hall");
        }

        Room room = Room.builder()
                .hall(hall)
                .roomNumber(request.getRoomNumber())
                .roomType(request.getRoomType())
                .monthlyFee(request.getMonthlyFee())
                .isAvailable(request.getIsAvailable() != null
                        && request.getIsAvailable())
                .placeNumber(request.getPlaceNumber())
                .build();

        return RoomResponse.from(roomRepository.save(room));
    }

    // ── Get all rooms in a hall ───────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<RoomResponse> getByHall(Long hallId) {
        findHallOrThrow(hallId);
        return roomRepository.findByHall_HallId(hallId).stream()
                .map(RoomResponse::from).toList();
    }

    // ── Get available rooms in a hall ─────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<RoomResponse> getAvailableByHall(Long hallId) {
        findHallOrThrow(hallId);
        return roomRepository.findByHall_HallIdAndIsAvailableTrue(hallId).stream()
                .map(RoomResponse::from).toList();
    }

    // ── Get one room ──────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public RoomResponse getById(Long roomId) {
        return RoomResponse.from(findRoomOrThrow(roomId));
    }

    // ── Update ────────────────────────────────────────────────────────────────

    @Transactional
    public RoomResponse update(Long hallId, Long roomId, RoomRequest request) {
        Room room = findRoomOrThrow(roomId);

        if (!room.getRoomNumber().equals(request.getRoomNumber())
                && roomRepository.existsByHall_HallIdAndRoomNumber(
                hallId, request.getRoomNumber())) {
            throw new BadRequestException(
                    "Room number " + request.getRoomNumber()
                            + " already exists in this hall");
        }

        room.setRoomNumber(request.getRoomNumber());
        room.setRoomType(request.getRoomType());
        room.setMonthlyFee(request.getMonthlyFee());
        room.setIsAvailable(request.getIsAvailable());
        room.setPlaceNumber(request.getPlaceNumber());

        return RoomResponse.from(roomRepository.save(room));
    }

    // ── Delete ────────────────────────────────────────────────────────────────

    @Transactional
    public void delete(Long roomId) {
        roomRepository.delete(findRoomOrThrow(roomId));
    }

    // ── Internal helpers ──────────────────────────────────────────────────────

    private Room findRoomOrThrow(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room", id));
    }

    private ResidenceHall findHallOrThrow(Long id) {
        return hallRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hall", id));
    }
}