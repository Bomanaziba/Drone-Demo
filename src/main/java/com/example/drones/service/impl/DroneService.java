package com.example.drones.service.impl;

import com.example.drones.entity.ChargingLog;
import com.example.drones.entity.Drone;
import com.example.drones.entity.Medication;
import com.example.drones.repository.ChargingLogRepository;
import com.example.drones.repository.DroneRepository;
import com.example.drones.repository.MedicationRepository;
import com.example.drones.repository.TripRepository;
import com.example.drones.request.AddDroneRequest;
import com.example.drones.utils.Constant;
import com.example.drones.utils.CreateResponse;
import com.example.drones.utils.DroneHelper;
import com.example.drones.utils.State;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Service
public class DroneService implements com.example.drones.service.DroneService {

    @Autowired
    private DroneRepository droneRepository;
    @Autowired
    MedicationRepository medicationRepository;
    @Autowired
    TripRepository tripRepository;
    @Autowired
    ChargingLogRepository chargingLogRepository;

    private Logger logger = LoggerFactory.getLogger(getClass());


    @Transactional
    public CreateResponse<Drone> createDrone(AddDroneRequest req) {

        CreateResponse<Drone> res = new CreateResponse<Drone>();

        try {

            if(!req.getSerialNumber().isBlank() || !req.getSerialNumber().isEmpty()) {

                var drone = droneRepository.findBySerialNumber(req.getSerialNumber());

                if(drone != null) {

                    res.setResponseCode(HttpStatus.BAD_REQUEST);
                    res.setResponseDescription("Drone with " + req.getSerialNumber() + " already exist");

                    return  res;
                }
            }

            var drone = new Drone();

            drone.setSerialNumber(req.getSerialNumber());
            drone.setWeightLimit(req.getWeightLimit());
            drone.setModel(req.getModel());
            drone.setBatteryCapacity(Constant.maxBattery);
            drone.setState(State.IDLE.name());

            var rep = droneRepository.save(drone);

            res.setData(rep);
            res.setResponseCode(HttpStatus.OK);
            res.setResponseDescription("Successful");

        } catch (Exception e) {
            res.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR);
            res.setResponseDescription("An error occur");
        }

        return  res;
    }

    @Transactional
    public CreateResponse<Drone> editDrone(AddDroneRequest req) {

        CreateResponse<Drone> res = new CreateResponse<Drone>();

        try {

            var repo = droneRepository.findById(req.getId());

            Drone drone =(repo == null || repo.isEmpty()) ?
                drone = droneRepository.findBySerialNumber(req.getSerialNumber()) : repo.get();


            if(drone == null) {
                res.setResponseCode(HttpStatus.BAD_REQUEST);
                res.setResponseDescription("Drone not found");
                return res;
            }

            drone.setSerialNumber(req.getSerialNumber());
            drone.setWeightLimit(req.getWeightLimit());
            drone.setModel(req.getModel());

            var rep = droneRepository.save(drone);

            res.setData(drone);
            res.setResponseCode(HttpStatus.OK);
            res.setResponseDescription("Successful");

        } catch (Exception e) {
            res.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR);
            res.setResponseDescription("An error occur");
        }

        return res;
    }

    public CreateResponse<List<Drone>> getDrones() {

        CreateResponse<List<Drone>> res = new CreateResponse<List<Drone>>();

        try {

            var rep = droneRepository.findAll();

            res.setData(rep);
            res.setResponseCode(HttpStatus.OK);
            res.setResponseDescription("Successful");

        } catch (Exception e) {
            res.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR);
            res.setResponseDescription("An error occur");
        }
        return  res;
    }

    public CreateResponse<Drone> getDrone(int id) {
        CreateResponse<Drone> res = new CreateResponse<Drone>();
        try {
            var rep= droneRepository.findById(id);

            if(rep.isEmpty()) {
                res.setResponseCode(HttpStatus.BAD_REQUEST);
                res.setResponseDescription("Drone not found");
                return res;
            }

            res.setData(rep.get());
            res.setResponseCode(HttpStatus.OK);
            res.setResponseDescription("Successful");
        } catch (Exception e) {
            res.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR);
            res.setResponseDescription("An error occur");
        }
        return res;
    }

    public CreateResponse<Drone> getDroneBySerialNumber(String serialNumber) {

        CreateResponse<Drone> res = new CreateResponse<Drone>();
        try {
            Drone rep = droneRepository.findBySerialNumber(serialNumber);
            res.setData(rep);
            res.setResponseCode(HttpStatus.OK);
            res.setResponseDescription("Successful");
        } catch (Exception e) {
            res.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR);
            res.setResponseDescription("An error occur");
        }
        return res;
    }

    @Transactional
    public CreateResponse<Drone>  deleteDrone(Drone drone) {
        CreateResponse<Drone> res = new CreateResponse<Drone>();
        try {
            droneRepository.delete(drone);
            res.setResponseCode(HttpStatus.OK);
            res.setResponseDescription("Successful");
        } catch (Exception e) {
            res.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR);
            res.setResponseDescription("An error occur");
        }
        return res;
    }

    public CreateResponse<List<Drone>> availableDrones() {

        CreateResponse<List<Drone>> res = new CreateResponse<List<Drone>>();

        try {

            var rep = droneRepository.findByState(State.IDLE.name());

            res.setData(rep);
            res.setResponseCode(HttpStatus.OK);
            res.setResponseDescription("Successful");

        } catch (Exception ex) {
            res.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR);
            res.setResponseDescription("An error occur");
        }

        return res;
    }

    public CreateResponse<Double> batteryLevel(String serialNumber) {

        CreateResponse<Double> res = new CreateResponse<Double>();

        try {
            var re = this.getDroneBySerialNumber(serialNumber);
            var cap = re.getData().getBatteryCapacity();

            res.setData(cap);
            res.setResponseCode(HttpStatus.OK);
            res.setResponseDescription("Successful");

        } catch (Exception e) {
            res.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR);
            res.setResponseDescription("An error occur");
        }
        return res;

    }

    public CreateResponse<List<Medication>> checkLoaded(String serialNumber) {

        CreateResponse<List<Medication>> res = new CreateResponse<List<Medication>>();

        try {
            var re = droneRepository.findBySerialNumber(serialNumber);

            if(re == null) {
                res.setResponseCode(HttpStatus.BAD_REQUEST);
                res.setResponseDescription("Drone not found");
                return  res;
            }

            var repoT = tripRepository.findByDroneIdAndHasEnd(re.getId(),false);

            if(repoT == null) {
                res.setResponseCode(HttpStatus.OK);
                res.setResponseDescription("Drone has no loaded medication");
                return  res;
            }

            var rep = medicationRepository.findByTripId(repoT.getId());

            res.setData(rep);
            res.setResponseCode(HttpStatus.OK);
            res.setResponseDescription("Successful");

        } catch (Exception ex) {
            res.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR);
            res.setResponseDescription("An error occur");
        }

        return res;
    }

    @Transactional
    public CreateResponse<String> initiateCharging(String serialNumber) {

        CreateResponse<String> res = new CreateResponse<String>();

        try {

            Drone drone = droneRepository.findBySerialNumber(serialNumber);

            ChargingLog chargingLog = new ChargingLog();

            chargingLog.setCreatedOn(LocalDateTime.now());
            chargingLog.setDrone(drone);

            chargingLogRepository.save(chargingLog);

            res.setData(serialNumber +" is plugged in");
            res.setResponseCode(HttpStatus.OK);
            res.setResponseDescription("Successful");

        } catch (Exception ex) {
            res.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR);
            res.setResponseDescription("An error occurred");
        }

        return  res;
    }


    @Transactional
    public CreateResponse<?> processCharging() {

        CreateResponse<Object> res = new CreateResponse<Object>();

        try {

            var repoCR = chargingLogRepository.findByIsFullyCharged(false);

            for (int i = 0; i < repoCR.size(); i++) {

                Drone drone = repoCR.get(i).getDrone();

                logger.info(drone.getSerialNumber() + " is charging.");

                var powerDelivered = DroneHelper.calculateChargingPower(drone.getWeightLimit(), Constant.chargingCronJobIntervalInMinutes);

                var currentBattery = drone.getBatteryCapacity();

                currentBattery +=  powerDelivered;

                drone.setBatteryCapacity((currentBattery >= Constant.maxBattery) ? Constant.maxBattery: currentBattery);

                droneRepository.save(drone);

                if(drone.getBatteryCapacity() >= Constant.maxBattery) {

                    logger.info(drone.getSerialNumber() + " is fully charged");

                    chargingLogRepository.delete(repoCR.get(i));
                }
            }

            res.setResponseCode(HttpStatus.OK);
            res.setResponseDescription("Successful");

        } catch (Exception ex) {
            res.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR);
            res.setResponseDescription("An error occurred");
        }

        return res;
    }


}
