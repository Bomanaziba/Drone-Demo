package com.example.drones.repository;

import com.example.drones.entity.Medication;
import com.example.drones.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Integer> {
    List<Medication> findByTripId(int tripId);
}
