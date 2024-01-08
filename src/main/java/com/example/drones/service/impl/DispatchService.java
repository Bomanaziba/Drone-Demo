package com.example.drones.service.impl;

import com.example.drones.dto.MedicationDto;
import com.example.drones.entity.Drone;
import com.example.drones.entity.Medication;
import com.example.drones.entity.Trip;
import com.example.drones.repository.DroneRepository;
import com.example.drones.repository.MedicationRepository;
import com.example.drones.repository.TripRepository;
import com.example.drones.request.LoadDroneRequest;
import com.example.drones.response.LoadDroneResponse;
import com.example.drones.utils.Constant;
import com.example.drones.utils.CreateResponse;
import com.example.drones.utils.DroneHelper;
import com.example.drones.utils.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Component
public class DispatchService implements com.example.drones.service.DispatchService {

    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private MedicationRepository medicationRepository;
    @Autowired
    private DroneRepository droneRepository;

    public CreateResponse<LoadDroneResponse> loadMedication(LoadDroneRequest request) {

        CreateResponse<LoadDroneResponse> res = new CreateResponse<LoadDroneResponse>();

        try {

            if(request.getSerialNumber() == null || request.getSerialNumber().isEmpty() || request.getSerialNumber().isBlank()) {
                res.setResponseCode(HttpStatus.BAD_REQUEST);
                res.setResponseDescription("Serial number not passed");
                return  res;
            }

            Drone repoD = droneRepository.findBySerialNumber(request.getSerialNumber());

            if(repoD == null) {
                res.setResponseCode(HttpStatus.BAD_REQUEST);
                res.setResponseDescription("Drone not found");
                return  res;
            }

            if(repoD.getBatteryCapacity() <= Constant.miniBattery) {
                res.setResponseCode(HttpStatus.BAD_REQUEST);
                res.setResponseDescription("Drone battery is low. It's less than " + Constant.miniBattery + "%, please charge");
                return  res;
            }

            if(request.getMedication() == null) {
                res.setResponseCode(HttpStatus.BAD_REQUEST);
                res.setResponseDescription("No medication details passed");
                return  res;
            }

            double totalWeight = request.getMedication().getWeight();
            double totalTrip = request.getMedication().getDeliveryTime();

            if(totalWeight > repoD.getWeightLimit()) {
                res.setResponseCode(HttpStatus.BAD_REQUEST);
                res.setResponseDescription("Drone weight limit exceeded");
                return  res;
            }

            Trip repoT = tripRepository.findByDroneId(repoD.getId());

            List<Medication> repoMs = (repoT != null) ?
                    medicationRepository.findByTripId(repoT.getId()):
                    new ArrayList<Medication>();

            if (!repoMs.isEmpty()) {
                totalWeight += repoMs.stream().filter(p -> p.getWeight() > 0).mapToDouble(p -> p.getWeight()).sum();
                totalTrip += repoMs.stream().filter(p -> p.getDeliveryTime() > 0).mapToDouble(p -> p.getDeliveryTime()).sum();
            }

            if(totalWeight > repoD.getWeightLimit()) {
                res.setResponseCode(HttpStatus.BAD_REQUEST);
                res.setResponseDescription("Drone weight limit exceeded, can't load any more medication");
                return  res;
            }

            double estimatedBatteryNeeded = DroneHelper.batteryConsumption(repoD.getWeightLimit(), totalWeight, totalTrip);

            if((repoD.getBatteryCapacity() - estimatedBatteryNeeded) <= Constant.miniBattery) {
                res.setResponseCode(HttpStatus.BAD_REQUEST);
                res.setResponseDescription("Drone battery capacity is not enough for trip");
                return  res;
            }

            repoT = processTrip(repoT, repoD, totalTrip, request.isStartTrip());

            var med = new Medication();
            med.setCode(request.getMedication().getCode());
            med.setImage(request.getMedication().getImage());
            med.setName(request.getMedication().getName());
            med.setDeliveryTime(request.getMedication().getDeliveryTime());
            med.setWeight(request.getMedication().getWeight());
            med.setTrip(repoT);

            var repoM = medicationRepository.save(med);

            repoMs.add(repoM);

            repoD.setState(State.LOADING.name());
            droneRepository.save(repoD);

            var respM = new ArrayList<MedicationDto>();

            for(int i = 0; i < repoMs.size(); i++) {
                var medi = new MedicationDto();
                medi.setName(repoMs.get(i).getName());
                medi.setCode(repoMs.get(i).getCode());
                medi.setWeight(repoMs.get(i).getWeight());
                medi.setDeliveryTime(repoMs.get(i).getDeliveryTime());
                medi.setImage(repoMs.get(i).getImage());
                respM.add(medi);
            }
            var resp = new LoadDroneResponse();

            resp.setDispatchId(repoT.getId());
            resp.setMedicationList(respM);
            resp.setDrone(repoD);
            resp.setTotalTimeofFlight(totalTrip);
            resp.setTotalWeightOfCargo(totalWeight);

            res.setData(resp);
            res.setResponseCode(HttpStatus.OK);
            res.setResponseDescription("Successfully");

        } catch (Exception e) {
            res.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR);
            res.setResponseDescription("An error occur");
        }

        return  res;
    }

    private Trip processTrip(Trip trip, Drone drone, Double totalTrip, boolean startTrip) {

        if (trip == null) {
            trip = new Trip();
            trip.setDrone(drone);
            trip.setCreatedOn(LocalDateTime.now());
        }

        trip.setTripDuration(totalTrip);
        trip.setHasStart((startTrip == true) ? true: false);
        trip.setLoading(true);
        trip.setState(State.LOADING.name());
        trip.setStartTime((startTrip == true) ? LocalDateTime.now(): null);

        return tripRepository.save(trip);
    }

    public CreateResponse<String> startDispatch(Integer dispatchId) {

        CreateResponse<String> res = new CreateResponse<String>();

        try {

            var repoT = tripRepository.findById(dispatchId);

            if(repoT.isEmpty()) {
                res.setResponseCode(HttpStatus.BAD_REQUEST);
                res.setResponseDescription("Dispatch with Id: " + dispatchId + " does not exit.");
                return  res;
            }

            Trip trip = repoT.get();

            trip.setHasStart(true);
            trip.setState(State.LOADED.name());
            trip.setLoading(false);
            trip.setStartTime(LocalDateTime.now());
            var repoSt = tripRepository.save(trip);

            Drone repoD = droneRepository.findById(repoSt.getDrone().getId()).get();
            repoD.setState(State.LOADED.name());
            droneRepository.save(repoD);

            res.setData("Trip started");
            res.setResponseCode(HttpStatus.OK);
            res.setResponseDescription("Successful");

        } catch (Exception e) {
            res.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR);
            res.setResponseDescription("An error occur");
        }

        return  res;
    }
}
