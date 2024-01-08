package com.example.drones.entity;

import jakarta.persistence.*;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Entity
public class ChargingLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "droneId")
    private Drone drone;
    private LocalDateTime createdOn;
    private boolean isFullyCharged;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Drone getDrone() {
        return drone;
    }

    public void setDrone(Drone drone) {
        this.drone = drone;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public boolean isFullyCharged() {
        return isFullyCharged;
    }

    public void setFullyCharged(boolean fullyCharged) {
        isFullyCharged = fullyCharged;
    }
}
