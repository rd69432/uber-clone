package com.uber.clone.dto;

import com.uber.clone.enums.RideStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RideResponse {

    private Long id;
    private Long riderId;
    private String riderName;
    private Long driverId;
    private String driverName;
    private RideStatus status;
    private String pickupLocation;
    private Double pickupLatitude;
    private Double pickupLongitude;
    private String dropoffLocation;
    private Double dropoffLatitude;
    private Double dropoffLongitude;
    private Double distance;
    private Double duration;
    private Double fare;
    private String paymentStatus;
    private String paymentMethod;
    private LocalDateTime requestedAt;
    private LocalDateTime acceptedAt;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private String cancellationReason;
}