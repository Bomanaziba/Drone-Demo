package com.example.drones.backgroundjob;

import com.example.drones.service.DroneService;
import com.example.drones.utils.Constant;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.spring.annotations.Recurring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChargingSystemBackgroundJob {

    @Autowired
    DroneService droneService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Job(name = "Charging System")
    @Recurring(id = "charging-job", cron = "*/" + Constant.chargingCronJobIntervalInMinutes + " * * * *", zoneId = "Africa/Lagos")
    public void executeChargingJob() {

        logger.info("charging-job started");

        try {

            droneService.processCharging();

        } catch (Exception ex) {
            logger.error("Error while executing sample job", ex);
        } finally {
            logger.info("charging-job has finished...");
        }
    }
}
