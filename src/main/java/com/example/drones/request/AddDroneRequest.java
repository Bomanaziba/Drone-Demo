package com.example.drones.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AddDroneRequest {
    private int id;

    @NotNull(message = "The serial number is required.")
    @Size(min = 1, max = 100, message = "The serial number must be from 1 to 100 characters.")
    private String serialNumber;

    @NotNull(message = "The model is required.")
    private String model;

    @NotNull(message = "The weight limit is required.")
    @Max(value = 500, message = "The weight limit must be from 0g to 500g.")
    private Integer weightLimit;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getWeightLimit() {
        return weightLimit;
    }

    public void setWeightLimit(Integer weightLimit) {
        this.weightLimit = weightLimit;
    }
}
