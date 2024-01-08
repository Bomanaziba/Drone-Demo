package com.example.drones.request;

import com.example.drones.dto.MedicationDto;
import com.example.drones.entity.Medication;
import jakarta.validation.Valid;

public class LoadDroneRequest {
    private String serialNumber;

    @Valid
    private MedicationDto medication;

    private boolean startTrip;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public MedicationDto getMedication() {
        return medication;
    }

    public void setMedication(MedicationDto medication) {
        this.medication = medication;
    }

    public boolean isStartTrip() {
        return startTrip;
    }

    public void setStartTrip(boolean startTrip) {
        this.startTrip = startTrip;
    }
}
