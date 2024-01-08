package com.example.drones.repository;

import com.example.drones.entity.ChargingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ChargingLogRepository extends JpaRepository<ChargingLog, Integer> {
    ChargingLog findByDroneId(Integer droneId);
    List<ChargingLog> findByIsFullyCharged(boolean isFullyCharged);
}
