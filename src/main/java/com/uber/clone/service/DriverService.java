package com.uber.clone.service;

import com.uber.clone.dto.DriverProfileRequest;
import com.uber.clone.dto.DriverProfileResponse;

import java.util.List;

public interface DriverService {

    DriverProfileResponse createDriverProfile(Long userId, DriverProfileRequest request);

    DriverProfileResponse getDriverProfile(Long userId);

    DriverProfileResponse updateDriverStatus(Long userId, String status);

    List<DriverProfileResponse> getAvailableDrivers();

    DriverProfileResponse verifyDriver(Long driverId);
}