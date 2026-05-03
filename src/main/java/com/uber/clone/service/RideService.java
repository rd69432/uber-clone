package com.uber.clone.service;

import com.uber.clone.dto.RideRequest;
import com.uber.clone.dto.RideResponse;
import com.uber.clone.enums.RideStatus;

import java.util.List;

public interface RideService {

    RideResponse createRide(RideRequest request);

    RideResponse acceptRide(Long rideId, Long driverId);

    RideResponse updateRideStatus(Long rideId, RideStatus status);

    RideResponse cancelRide(Long rideId, String reason);

    RideResponse getRideById(Long rideId);

    List<RideResponse> getRiderRides(Long riderId);

    List<RideResponse> getDriverRides(Long driverId);

    RideResponse getActiveRide(Long userId);
}