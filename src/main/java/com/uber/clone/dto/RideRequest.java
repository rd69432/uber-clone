package com.uber.clone.dto;

import com.uber.clone.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RideRequest {

    @NotBlank(message = "Pickup location is required")
    private String pickupLocation;

    @NotNull(message = "Pickup latitude is required")
    private Double pickupLatitude;

    @NotNull(message = "Pickup longitude is required")
    private Double pickupLongitude;

    @NotBlank(message = "Dropoff location is required")
    private String dropoffLocation;

    @NotNull(message = "Dropoff latitude is required")
    private Double dropoffLatitude;

    @NotNull(message = "Dropoff longitude is required")
    private Double dropoffLongitude;

    @NotNull(message = "Distance is required")
    @Positive(message = "Distance must be positive")
    private Double distance;

    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be positive")
    private Double duration;

    @NotNull(message = "Fare is required")
    @Positive(message = "Fare must be positive")
    private Double fare;

    private PaymentMethod paymentMethod;
}