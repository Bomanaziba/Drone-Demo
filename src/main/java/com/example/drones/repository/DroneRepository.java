package com.example.drones.repository;

import com.example.drones.entity.Drone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DroneRepository extends JpaRepository<Drone, Integer> {
    Drone findBySerialNumber(String serialNumber);
    List<Drone> findByState(String state);
}
