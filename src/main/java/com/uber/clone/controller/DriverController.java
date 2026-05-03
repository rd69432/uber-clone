package com.uber.clone.controller;

import com.uber.clone.dto.DriverProfileRequest;
import com.uber.clone.dto.DriverProfileResponse;
import com.uber.clone.service.DriverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
@Tag(name = "Drivers", description = "APIs for driver management")
public class DriverController {

    private final DriverService driverService;

    @PostMapping("/profile")
    @Operation(summary = "Create driver profile", description = "Create a new driver profile")
    public ResponseEntity<DriverProfileResponse> createDriverProfile(
            @RequestParam Long userId,
            @Valid @RequestBody DriverProfileRequest request) {
        return new ResponseEntity<>(driverService.createDriverProfile(userId, request), HttpStatus.CREATED);
    }

    @GetMapping("/profile/{userId}")
    @Operation(summary = "Get driver profile", description = "Get driver profile by user ID")
    public ResponseEntity<DriverProfileResponse> getDriverProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(driverService.getDriverProfile(userId));
    }

    @PutMapping("/status/{userId}")
    @Operation(summary = "Update driver status", description = "Update driver availability status")
    public ResponseEntity<DriverProfileResponse> updateDriverStatus(
            @PathVariable Long userId,
            @RequestParam String status) {
        return ResponseEntity.ok(driverService.updateDriverStatus(userId, status));
    }

    @GetMapping("/available")
    @Operation(summary = "Get available drivers", description = "Get list of available and verified drivers")
    public ResponseEntity<List<DriverProfileResponse>> getAvailableDrivers() {
        return ResponseEntity.ok(driverService.getAvailableDrivers());
    }

    @PutMapping("/verify/{driverId}")
    @Operation(summary = "Verify driver", description = "Verify a driver profile")
    public ResponseEntity<DriverProfileResponse> verifyDriver(@PathVariable Long driverId) {
        return ResponseEntity.ok(driverService.verifyDriver(driverId));
    }
}