package com.uber.clone.service.impl;

import com.uber.clone.dto.DriverProfileRequest;
import com.uber.clone.dto.DriverProfileResponse;
import com.uber.clone.entity.DriverProfile;
import com.uber.clone.entity.User;
import com.uber.clone.enums.DriverStatus;
import com.uber.clone.enums.Role;
import com.uber.clone.exception.BadRequestException;
import com.uber.clone.exception.ResourceNotFoundException;
import com.uber.clone.repository.DriverProfileRepository;
import com.uber.clone.repository.UserRepository;
import com.uber.clone.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final DriverProfileRepository driverProfileRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public DriverProfileResponse createDriverProfile(Long userId, DriverProfileRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRole() != Role.DRIVER) {
            throw new BadRequestException("User is not a driver");
        }

        if (driverProfileRepository.existsByUserId(userId)) {
            throw new BadRequestException("Driver profile already exists");
        }

        if (driverProfileRepository.existsByLicenseNumber(request.getLicenseNumber())) {
            throw new BadRequestException("License number already exists");
        }

        DriverProfile driverProfile = DriverProfile.builder()
            .user(user)
            .licenseNumber(request.getLicenseNumber())
            .vehicleType(request.getVehicleType())
            .vehicleModel(request.getVehicleModel())
            .vehicleColor(request.getVehicleColor())
            .vehicleLicensePlate(request.getVehicleLicensePlate())
            .status(DriverStatus.OFFLINE)
            .rating(0.0)
            .totalRides(0)
            .verified(false)
            .build();

        driverProfile = driverProfileRepository.save(driverProfile);

        return mapToResponse(driverProfile);
    }

    @Override
    public DriverProfileResponse getDriverProfile(Long userId) {
        DriverProfile driverProfile = driverProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Driver profile not found"));
        return mapToResponse(driverProfile);
    }

    @Override
    @Transactional
    public DriverProfileResponse updateDriverStatus(Long userId, String status) {
        DriverProfile driverProfile = driverProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Driver profile not found"));

        DriverStatus driverStatus = DriverStatus.valueOf(status.toUpperCase());
        driverProfile.setStatus(driverStatus);
        driverProfile = driverProfileRepository.save(driverProfile);

        return mapToResponse(driverProfile);
    }

    @Override
    public List<DriverProfileResponse> getAvailableDrivers() {
        return driverProfileRepository.findAvailableVerifiedDrivers()
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DriverProfileResponse verifyDriver(Long driverId) {
        DriverProfile driverProfile = driverProfileRepository.findById(driverId)
            .orElseThrow(() -> new ResourceNotFoundException("Driver profile not found"));

        driverProfile.setVerified(true);
        driverProfile = driverProfileRepository.save(driverProfile);

        return mapToResponse(driverProfile);
    }

    private DriverProfileResponse mapToResponse(DriverProfile driverProfile) {
        User user = driverProfile.getUser();
        return DriverProfileResponse.builder()
            .id(driverProfile.getId())
            .userId(user.getId())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .status(driverProfile.getStatus())
            .licenseNumber(driverProfile.getLicenseNumber())
            .vehicleType(driverProfile.getVehicleType())
            .vehicleModel(driverProfile.getVehicleModel())
            .vehicleColor(driverProfile.getVehicleColor())
            .vehicleLicensePlate(driverProfile.getVehicleLicensePlate())
            .rating(driverProfile.getRating())
            .totalRides(driverProfile.getTotalRides())
            .verified(driverProfile.isVerified())
            .createdAt(driverProfile.getCreatedAt())
            .build();
    }
}