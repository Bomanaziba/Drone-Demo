package com.example.drones.backgroundjob;

import com.example.drones.entity.Drone;
import com.example.drones.entity.Medication;
import com.example.drones.entity.Trip;
import com.example.drones.repository.DroneRepository;
import com.example.drones.repository.MedicationRepository;
import com.example.drones.repository.TripRepository;
import com.example.drones.utils.Constant;
import com.example.drones.utils.DroneHelper;
import com.example.drones.utils.State;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.spring.annotations.Recurring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Component
public class TripMonitorBackgroundJob {

    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private DroneRepository droneRepository;
    @Autowired
    protected MedicationRepository medicationRepository;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Job(name = "Trip System")
    @Recurring(id = "trip-job", cron = "*/" + Constant.tripCronJobIntervalInMinutes + " * * * *", zoneId = "Africa/Lagos")
    public void executeTripJob() {

        logger.info("trip-job started");

        try {

            List<Trip> repoT = tripRepository.findByHasStartAndHasEnd(true, false);

            for(int i = 0; i < repoT.size(); i++) {

                logger.info("Trip Id: " + repoT.get(i).getId());

                Trip reT = repoT.get(i);

                Drone reD = reT.getDrone();

                List<Medication> medications = medicationRepository.findByTripId(reT.getId());

                var weightOfMedications = medications.stream().filter(p -> p.getWeight() > 0).mapToDouble(p -> p.getWeight()).sum();

                //Get the battery consumption per interval in minutes, given the interval, weigh capacity and weigh of medication
                double batteryPerMin = DroneHelper.batteryConsumption(reD.getWeightLimit(), weightOfMedications, Constant.tripCronJobIntervalInMinutes);

                //Deduce form battery life
                reD.setBatteryCapacity(reD.getBatteryCapacity()-batteryPerMin);

                //Get the state of the drone per interval in minutes based on the total time of flight
                if(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) < reT.getStartTime().plusMinutes(Double.valueOf(reT.getTripDuration() * 0.30).longValue()).toEpochSecond(ZoneOffset.UTC)) {
                    reT.setState(State.DELIVERING.name());
                    reD.setState(State.DELIVERING.name());
                } else if(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) >= reT.getStartTime().plusMinutes(Double.valueOf(reT.getTripDuration() * 0.45).longValue()).toEpochSecond(ZoneOffset.UTC) &&
                        LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) <= reT.getStartTime().plusMinutes(Double.valueOf(reT.getTripDuration() * 0.85).longValue()).toEpochSecond(ZoneOffset.UTC)) {
                    reT.setState(State.RETURNING.name());
                    reD.setState(State.RETURNING.name());
                } else if(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) >= reT.getStartTime().plusMinutes(Double.valueOf(reT.getTripDuration() * 0.85).longValue()).toEpochSecond(ZoneOffset.UTC) &&
                        LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) <= reT.getStartTime().plusMinutes(Double.valueOf(reT.getTripDuration()).longValue()).toEpochSecond(ZoneOffset.UTC)) {
                    reT.setState(State.DELIVERED.name());
                    reD.setState(State.DELIVERED.name());
                } else if (LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) >= reT.getStartTime().plusMinutes(Double.valueOf(reT.getTripDuration()).longValue()).toEpochSecond(ZoneOffset.UTC)) {
                    reT.setHasEnd(true);
                    reD.setState(State.IDLE.name());
                }

                tripRepository.save(reT);
                droneRepository.save(reD);
            }
        } catch (Exception ex) {
            logger.error("Error while executing sample job", ex);
        } finally {
            logger.info("trip-job has finished...");
        }

    }
}
