package com.uber.clone.repository;

import com.uber.clone.entity.Ride;
import com.uber.clone.enums.RideStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {

    List<Ride> findByRiderId(Long riderId);

    List<Ride> findByDriverId(Long driverId);

    List<Ride> findByRiderIdAndStatus(Long riderId, RideStatus status);

    List<Ride> findByDriverIdAndStatus(Long driverId, RideStatus status);

    @Query("SELECT r FROM Ride r WHERE r.driver.id = :driverId AND r.status IN ('PENDING', 'ACCEPTED', 'IN_PROGRESS')")
    Optional<Ride> findActiveRideByDriver(Long driverId);

    @Query("SELECT r FROM Ride r WHERE r.rider.id = :riderId AND r.status IN ('PENDING', 'ACCEPTED', 'IN_PROGRESS')")
    Optional<Ride> findActiveRideByRider(Long riderId);

    List<Ride> findByStatus(RideStatus status);
}