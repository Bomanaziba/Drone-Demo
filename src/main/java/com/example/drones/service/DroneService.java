package com.example.drones.service;

import com.example.drones.entity.Drone;
import com.example.drones.entity.Medication;
import com.example.drones.request.AddDroneRequest;
import com.example.drones.utils.CreateResponse;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service
public interface DroneService {
    CreateResponse<Drone> createDrone(AddDroneRequest drone);
    CreateResponse<Drone> editDrone(AddDroneRequest drone);
    CreateResponse<List<Drone>> getDrones();
    CreateResponse<Drone> getDrone(int id);
    CreateResponse<Drone> getDroneBySerialNumber(String serialNumber);
    CreateResponse<Drone>  deleteDrone(Drone drone);
    CreateResponse<List<Drone>> availableDrones();
    CreateResponse<Double> batteryLevel(String serialNumber);
    CreateResponse<List<Medication>> checkLoaded(String serialNumber);
    CreateResponse<?> processCharging();
    CreateResponse<String> initiateCharging(String serialNumber);
}
