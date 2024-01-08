package com.example.drones.repository;

import com.example.drones.entity.Drone;
import com.example.drones.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Integer> {
    Trip findByDroneId(int droneId);
    Trip findByDroneIdAndHasEnd(int droneId, boolean hasEnd);
    List<Trip> findByHasStartAndHasEnd(boolean hasStart, boolean hasEnd);
}
