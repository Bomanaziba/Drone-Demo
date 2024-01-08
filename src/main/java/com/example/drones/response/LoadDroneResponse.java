package com.example.drones.response;

import com.example.drones.dto.MedicationDto;
import com.example.drones.entity.Drone;
import com.example.drones.entity.Medication;

import java.util.List;

public class LoadDroneResponse {
    private int dispatchId;
    private Drone drone;
    private List<MedicationDto> medicationList;
    private double totalWeightOfCargo;
    private double totalTimeofFlight;

    public int getDispatchId() {
        return dispatchId;
    }

    public void setDispatchId(int dispatchId) {
        this.dispatchId = dispatchId;
    }

    public Drone getDrone() {
        return drone;
    }

    public void setDrone(Drone drone) {
        this.drone = drone;
    }

    public List<MedicationDto> getMedicationList() {
        return medicationList;
    }

    public void setMedicationList(List<MedicationDto> medicationList) {
        this.medicationList = medicationList;
    }

    public double getTotalWeightOfCargo() {
        return totalWeightOfCargo;
    }

    public void setTotalWeightOfCargo(double totalWeightOfCargo) {
        this.totalWeightOfCargo = totalWeightOfCargo;
    }

    public double getTotalTimeofFlight() {
        return totalTimeofFlight;
    }

    public void setTotalTimeofFlight(double totalTimeofFlight) {
        this.totalTimeofFlight = totalTimeofFlight;
    }
}
