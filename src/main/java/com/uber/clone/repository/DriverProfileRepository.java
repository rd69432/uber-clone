package com.uber.clone.repository;

import com.uber.clone.entity.DriverProfile;
import com.uber.clone.enums.DriverStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverProfileRepository extends JpaRepository<DriverProfile, Long> {

    Optional<DriverProfile> findByUserId(Long userId);

    List<DriverProfile> findByStatus(DriverStatus status);

    @Query("SELECT d FROM DriverProfile d WHERE d.status = 'AVAILABLE' AND d.verified = true")
    List<DriverProfile> findAvailableVerifiedDrivers();

    boolean existsByLicenseNumber(String licenseNumber);

    boolean existsByUserId(Long userId);
}