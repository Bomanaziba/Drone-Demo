package com.example.drones.service;

import com.example.drones.entity.Drone;
import com.example.drones.entity.Medication;
import com.example.drones.request.LoadDroneRequest;
import com.example.drones.response.LoadDroneResponse;
import com.example.drones.utils.CreateResponse;
import jakarta.annotation.Nullable;

import java.util.List;

public interface DispatchService {
    CreateResponse<LoadDroneResponse> loadMedication(LoadDroneRequest request);
    CreateResponse<String> startDispatch(Integer dispatchId);
}
