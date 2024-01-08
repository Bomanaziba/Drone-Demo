package com.example.drones.utils;

public class DroneHelper {


    //Estimated battery consumption given a drone weight capacity, payload, and flight time
    //input is in grams and minutes
    public static double batteryConsumption(double payloadInGrams, double maxPayloadInGrams, double flightDurationMinutes) {

        //assume that weight capacity and payload weight is in kilogram, flight duration is in hours
        //assume that the weight of the drone itself is not considered
        //lets convert them to grams and minutes;
        double payload  = payloadInGrams/1000;
        double maxPayload  = maxPayloadInGrams/1000;
        double flightDuration  = flightDurationMinutes/60;

        // Calculate the total weight (drone + payload)
        double totalWeight = estimateDroneWeight(maxPayload) + payload;

        // Calculate the total energy required for the flight
        double totalEnergyConsumed = (totalWeight * flightDuration) * Constant.efficiencyFactor;

        // Calculate the estimated battery capacity needed
        double batteryCapacity = totalEnergyConsumed / Constant.energyDensity;

        // Calculate total energy capacity of the battery
        double totalEnergyCapacity = ((estimateDroneWeight(maxPayload) + maxPayload) * Constant.averageDroneFlightInHour) * Constant.efficiencyFactor;

        // Calculate the estimated battery capacity needed
        double maxBatteryCapacity = totalEnergyCapacity / Constant.energyDensity;

        // Calculate percentage of battery consumed
        return (batteryCapacity / maxBatteryCapacity) * 100;
    }

    //Estimated power delivered per time given a drone battery capacity
    //Input is in grams and minutes
    public static double calculateChargingPower(double maxPayloadInGram, double chargingTimeInMinutes) {

        double maxPayload = maxPayloadInGram/1000;

        double chargingTime = chargingTimeInMinutes/60;

        // Calculate total energy required for charging
        double totalEnergy = estimatedBatteryEnergy(maxPayload);

        //assumed power deliver by out let give the voltage and current
        double powerDeliveredByOutlet = Constant.voltageOfBattery * Constant.currentDeliveredByChargingOutlet;

        // Calculate charging energy required per time
        double rawEnergyDelivery = (powerDeliveredByOutlet * chargingTime) * Constant.efficiencyFactor;

        //Power in percentage
        return (rawEnergyDelivery/totalEnergy) * 100;
    }

    //Estimated energy of the drone battery given it max payload
    //input is in kilograms
    static double estimatedBatteryEnergy(double maxPayload) {

        // Calculate the total energy required for the flight
        double totalEnergy = ((maxPayload + estimateDroneWeight(maxPayload)) * Constant.energyDensity) * Constant.efficiencyFactor;

        return totalEnergy;
    }

    //Estimated drone weight based on payload capacity
    //input is in kilograms
    static double estimateDroneWeight(double maxPayload) {
        // Calculate drone weight based on payload capacity
        return maxPayload / (1 - Constant.payloadFraction);
    }
}
