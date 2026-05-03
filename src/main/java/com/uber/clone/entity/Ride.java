package com.uber.clone.entity;

import jakarta.persistence.*;
import lombok.*;
import com.uber.clone.enums.RideStatus;
import com.uber.clone.enums.PaymentMethod;
import com.uber.clone.enums.PaymentStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "rides")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rider_id", nullable = false)
    private User rider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private DriverProfile driver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private RideStatus status = RideStatus.PENDING;

    @Column(nullable = false)
    private String pickupLocation;

    @Column(nullable = false)
    private Double pickupLatitude;

    @Column(nullable = false)
    private Double pickupLongitude;

    @Column(nullable = false)
    private String dropoffLocation;

    @Column(nullable = false)
    private Double dropoffLatitude;

    @Column(nullable = false)
    private Double dropoffLongitude;

    @Column(nullable = false)
    private Double distance;

    @Column(nullable = false)
    private Double duration;

    @Column(nullable = false)
    private Double fare;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private LocalDateTime requestedAt;

    private LocalDateTime acceptedAt;

    private LocalDateTime startedAt;

    private LocalDateTime completedAt;

    private LocalDateTime cancelledAt;

    private String cancellationReason;

    private String otp;

    @PrePersist
    protected void onCreate() {
        if (requestedAt == null) {
            requestedAt = LocalDateTime.now();
        }
    }
}