package com.example.drones.controller;

import com.example.drones.entity.Drone;
import com.example.drones.request.AddDroneRequest;
import com.example.drones.service.DroneService;
import com.example.drones.utils.CreateResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/drone")
@Validated
public class DroneController {

    @Autowired
    private DroneService droneService;

    @RequestMapping(value = "info", method = RequestMethod.GET)
    public String info() {
        return "The application is up...";
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        var res = droneService.getDrones();
        return new ResponseEntity<CreateResponse<?>>(res, res.getResponseCode());
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> get(@RequestParam int id) {
        var res = droneService.getDrone(id);
        return new ResponseEntity<CreateResponse<?>>(res, res.getResponseCode());
    }

    @RequestMapping(value = "get", method = RequestMethod.GET)
    public ResponseEntity<?> get(@RequestParam String serialNumber) {
        var res = droneService.getDroneBySerialNumber(serialNumber);
        return new ResponseEntity<CreateResponse<?>>(res, res.getResponseCode());
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> add(@Valid @RequestBody AddDroneRequest req) {
        var res = droneService.createDrone(req);
        return new ResponseEntity<CreateResponse<?>>(res, res.getResponseCode());
    }

    @RequestMapping(method = RequestMethod.PATCH)
    public ResponseEntity<?> update(@Valid @RequestBody AddDroneRequest req) {
        var res = droneService.editDrone(req);
        return new ResponseEntity<CreateResponse<?>>(res, res.getResponseCode());
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@Valid @RequestBody Drone drone) {
        var res = droneService.deleteDrone(drone);
        return new ResponseEntity<CreateResponse<?>>(res, res.getResponseCode());
    }

    @RequestMapping(value = "medication", method = RequestMethod.GET)
    public ResponseEntity<?> getMedication(@RequestParam String serialNumber) {
        var res = droneService.checkLoaded(serialNumber);
        return new ResponseEntity<CreateResponse<?>>(res, res.getResponseCode());
    }

    @RequestMapping(value = "available", method = RequestMethod.GET)
    public ResponseEntity<?> getAvailable() {
        var res = droneService.availableDrones();
        return new ResponseEntity<CreateResponse<?>>(res, res.getResponseCode());
    }

    @RequestMapping(value = "battery", method = RequestMethod.GET)
    public ResponseEntity<?> getBattery(@RequestParam String serialNumber) {
        var res = droneService.batteryLevel(serialNumber);
        return new ResponseEntity<CreateResponse<?>>(res, res.getResponseCode());
    }

    @RequestMapping(value = "initiateCharging", method = RequestMethod.GET)
    public ResponseEntity<?> initiateCharging(@RequestParam String serialNumber) {
        var res = droneService.initiateCharging(serialNumber);
        return new ResponseEntity<CreateResponse<?>>(res, res.getResponseCode());
    }
}
