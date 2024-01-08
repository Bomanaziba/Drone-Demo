package com.example.drones.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public class MedicationDto {

    @NotNull(message = "The name is required.")
    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "Name is only allows a-z, A-Z, 0-9, _, and -")
    @Size(min = 2, message = "Minimum length of name must be 2")
    private String name;

    @NotNull(message = "The weight is required.")
    @Min(value = 1, message = "Weight must be greater than 0")
    private double weight;

    @NotNull(message = "The code is required.")
    @Pattern(regexp = "^[A-Z0-9_]*$", message = "Code is only allows A-Z, 0-9, and _")
    @Size(min = 2, message = "Minimum length of code must be 2")
    private String code;

    private String image;

    @NotNull(message = "The delivery time is required.")
    @Min(value = 1, message = "Delivery time must be greater than 0")
    private double deliveryTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(double deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
}
