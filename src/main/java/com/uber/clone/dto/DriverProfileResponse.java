package com.uber.clone.dto;

import com.uber.clone.enums.DriverStatus;
import com.uber.clone.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverProfileResponse {

    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private DriverStatus status;
    private String licenseNumber;
    private VehicleType vehicleType;
    private String vehicleModel;
    private String vehicleColor;
    private String vehicleLicensePlate;
    private Double rating;
    private Integer totalRides;
    private boolean verified;
    private LocalDateTime createdAt;
}