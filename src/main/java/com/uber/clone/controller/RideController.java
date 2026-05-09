package com.uber.clone.controller;

import com.uber.clone.dto.RideRequest;
import com.uber.clone.dto.RideResponse;
import com.uber.clone.enums.RideStatus;
import com.uber.clone.service.RideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rides")
@RequiredArgsConstructor
@Tag(name = "Rides", description = "APIs for ride management")
public class RideController {

    private final RideService rideService;

    @PostMapping
    @Operation(summary = "Create a new ride", description = "Request a new ride")
    public ResponseEntity<RideResponse> createRide(@Valid @RequestBody RideRequest request) {
        return new ResponseEntity<>(rideService.createRide(request), HttpStatus.CREATED);
    }

    @PostMapping("/{rideId}/accept")
    @Operation(summary = "Accept a ride", description = "Driver accepts a pending ride")
    public ResponseEntity<RideResponse> acceptRide(
            @PathVariable Long rideId,
            @RequestParam Long driverId) {
        return ResponseEntity.ok(rideService.acceptRide(rideId, driverId));
    }

    @PutMapping("/{rideId}/status")
    @Operation(summary = "Update ride status", description = "Update the status of a ride")
    public ResponseEntity<RideResponse> updateRideStatus(
            @PathVariable Long rideId,
            @RequestParam RideStatus status) {
        return ResponseEntity.ok(rideService.updateRideStatus(rideId, status));
    }

    @DeleteMapping("/{rideId}")
    @Operation(summary = "Cancel a ride", description = "Cancel a ride with a reason")
    public ResponseEntity<RideResponse> cancelRide(
            @PathVariable Long rideId,
            @RequestParam String reason) {
        return ResponseEntity.ok(rideService.cancelRide(rideId, reason));
    }

    @GetMapping("/{rideId}")
    @Operation(summary = "Get ride by ID", description = "Get ride details by ride ID")
    public ResponseEntity<RideResponse> getRideById(@PathVariable Long rideId) {
        return ResponseEntity.ok(rideService.getRideById(rideId));
    }

    @GetMapping("/rider/{riderId}")
    @Operation(summary = "Get rider's rides", description = "Get all rides for a specific rider")
    public ResponseEntity<List<RideResponse>> getRiderRides(@PathVariable Long riderId) {
        return ResponseEntity.ok(rideService.getRiderRides(riderId));
    }

    @GetMapping("/driver/{driverId}")
    @Operation(summary = "Get driver's rides", description = "Get all rides for a specific driver")
    public ResponseEntity<List<RideResponse>> getDriverRides(@PathVariable Long driverId) {
        return ResponseEntity.ok(rideService.getDriverRides(driverId));
    }

    @GetMapping("/active/{userId}")
    @Operation(summary = "Get active ride", description = "Get the current active ride for a user")
    public ResponseEntity<RideResponse> getActiveRide(@PathVariable Long userId) {
        RideResponse activeRide = rideService.getActiveRide(userId);
        return activeRide != null ? ResponseEntity.ok(activeRide) : ResponseEntity.noContent().build();
    }
}