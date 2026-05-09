package com.uber.clone.service.impl;

import com.uber.clone.dto.RideRequest;
import com.uber.clone.dto.RideResponse;
import com.uber.clone.entity.DriverProfile;
import com.uber.clone.entity.Ride;
import com.uber.clone.entity.User;
import com.uber.clone.enums.DriverStatus;
import com.uber.clone.enums.PaymentStatus;
import com.uber.clone.enums.RideStatus;
import com.uber.clone.exception.BadRequestException;
import com.uber.clone.exception.ResourceNotFoundException;
import com.uber.clone.repository.DriverProfileRepository;
import com.uber.clone.repository.RideRepository;
import com.uber.clone.repository.UserRepository;
import com.uber.clone.service.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final UserRepository userRepository;
    private final DriverProfileRepository driverProfileRepository;

    @Override
    @Transactional
    public RideResponse createRide(RideRequest request) {
        // Get current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User rider = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Rider not found"));

        Ride ride = Ride.builder()
            .rider(rider)
            .pickupLocation(request.getPickupLocation())
            .pickupLatitude(request.getPickupLatitude())
            .pickupLongitude(request.getPickupLongitude())
            .dropoffLocation(request.getDropoffLocation())
            .dropoffLatitude(request.getDropoffLatitude())
            .dropoffLongitude(request.getDropoffLongitude())
            .distance(request.getDistance())
            .duration(request.getDuration())
            .fare(request.getFare())
            .status(RideStatus.PENDING)
            .paymentStatus(PaymentStatus.PENDING)
            .paymentMethod(request.getPaymentMethod())
            .otp(generateOTP())
            .build();

        ride = rideRepository.save(ride);
        return mapToResponse(ride);
    }

    @Override
    @Transactional
    public RideResponse acceptRide(Long rideId, Long driverId) {
        Ride ride = rideRepository.findById(rideId)
            .orElseThrow(() -> new ResourceNotFoundException("Ride not found"));

        if (ride.getStatus() != RideStatus.PENDING) {
            throw new BadRequestException("Ride is not pending");
        }

        DriverProfile driver = driverProfileRepository.findById(driverId)
            .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

        if (driver.getStatus() != DriverStatus.AVAILABLE) {
            throw new BadRequestException("Driver is not available");
        }

        ride.setDriver(driver);
        ride.setStatus(RideStatus.ACCEPTED);
        ride.setAcceptedAt(LocalDateTime.now());

        driver.setStatus(DriverStatus.BUSY);
        driverProfileRepository.save(driver);

        ride = rideRepository.save(ride);
        return mapToResponse(ride);
    }

    @Override
    @Transactional
    public RideResponse updateRideStatus(Long rideId, RideStatus status) {
        Ride ride = rideRepository.findById(rideId)
            .orElseThrow(() -> new ResourceNotFoundException("Ride not found"));

        ride.setStatus(status);

        if (status == RideStatus.ACCEPTED) {
            ride.setAcceptedAt(LocalDateTime.now());
        } else if (status == RideStatus.IN_PROGRESS) {
            ride.setStartedAt(LocalDateTime.now());
        } else if (status == RideStatus.COMPLETED) {
            ride.setCompletedAt(LocalDateTime.now());
            ride.setPaymentStatus(PaymentStatus.COMPLETED);
            
            if (ride.getDriver() != null) {
                DriverProfile driver = ride.getDriver();
                driver.setStatus(DriverStatus.AVAILABLE);
                driver.setTotalRides(driver.getTotalRides() + 1);
                driverProfileRepository.save(driver);
            }
        } else if (status == RideStatus.CANCELLED) {
            ride.setCancelledAt(LocalDateTime.now());
            ride.setPaymentStatus(PaymentStatus.FAILED);
            if (ride.getDriver() != null) {
                DriverProfile driver = ride.getDriver();
                driver.setStatus(DriverStatus.AVAILABLE);
                driverProfileRepository.save(driver);
            }
        }

        ride = rideRepository.save(ride);
        return mapToResponse(ride);
    }

    @Override
    @Transactional
    public RideResponse cancelRide(Long rideId, String reason) {
        Ride ride = rideRepository.findById(rideId)
            .orElseThrow(() -> new ResourceNotFoundException("Ride not found"));

        if (ride.getStatus() == RideStatus.COMPLETED || ride.getStatus() == RideStatus.CANCELLED) {
            throw new BadRequestException("Cannot cancel this ride");
        }

        ride.setStatus(RideStatus.CANCELLED);
        ride.setCancellationReason(reason);
        ride.setCancelledAt(LocalDateTime.now());

        // Free up driver if ride was accepted
        if (ride.getDriver() != null) {
            DriverProfile driver = ride.getDriver();
            driver.setStatus(DriverStatus.AVAILABLE);
            driverProfileRepository.save(driver);
        }

        ride = rideRepository.save(ride);
        return mapToResponse(ride);
    }

    @Override
    public RideResponse getRideById(Long rideId) {
        Ride ride = rideRepository.findById(rideId)
            .orElseThrow(() -> new ResourceNotFoundException("Ride not found"));
        return mapToResponse(ride);
    }

    @Override
    public List<RideResponse> getRiderRides(Long riderId) {
        return rideRepository.findByRiderId(riderId)
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<RideResponse> getDriverRides(Long driverId) {
        return rideRepository.findByDriverId(driverId)
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Override
    public RideResponse getActiveRide(Long userId) {
        return rideRepository.findActiveRideByRider(userId)
            .map(this::mapToResponse)
            .orElse(null);
    }

    private String generateOTP() {
        return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    private RideResponse mapToResponse(Ride ride) {
        return RideResponse.builder()
            .id(ride.getId())
            .riderId(ride.getRider().getId())
            .riderName(ride.getRider().getFullName())
            .driverId(ride.getDriver() != null ? ride.getDriver().getId() : null)
            .driverName(ride.getDriver() != null ? ride.getDriver().getUser().getFullName() : null)
            .status(ride.getStatus())
            .pickupLocation(ride.getPickupLocation())
            .pickupLatitude(ride.getPickupLatitude())
            .pickupLongitude(ride.getPickupLongitude())
            .dropoffLocation(ride.getDropoffLocation())
            .dropoffLatitude(ride.getDropoffLatitude())
            .dropoffLongitude(ride.getDropoffLongitude())
            .distance(ride.getDistance())
            .duration(ride.getDuration())
            .fare(ride.getFare())
            .paymentStatus(ride.getPaymentStatus().name())
            .paymentMethod(ride.getPaymentMethod() != null ? ride.getPaymentMethod().name() : null)
            .requestedAt(ride.getRequestedAt())
            .acceptedAt(ride.getAcceptedAt())
            .startedAt(ride.getStartedAt())
            .completedAt(ride.getCompletedAt())
            .cancellationReason(ride.getCancellationReason())
            .build();
    }
}