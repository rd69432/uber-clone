package com.uber.clone.entity;

import jakarta.persistence.*;
import lombok.*;
import com.uber.clone.enums.DriverStatus;
import com.uber.clone.enums.VehicleType;

import java.time.LocalDateTime;

@Entity
@Table(name = "driver_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private DriverStatus status = DriverStatus.OFFLINE;

    @Column(nullable = false)
    private String licenseNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType vehicleType;

    private String vehicleModel;

    private String vehicleColor;

    private String vehicleLicensePlate;

    private Double rating;

    @Column(nullable = false)
    @Builder.Default
    private Integer totalRides = 0;

    @Column(nullable = false)
    @Builder.Default
    private boolean verified = false;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}