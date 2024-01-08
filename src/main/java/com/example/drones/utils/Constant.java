package com.example.drones.utils;

public class Constant {

    public static int miniBattery = 25;
    public static int maxBattery = 100;
    public final static int tripCronJobIntervalInMinutes = 1;
    public final static int chargingCronJobIntervalInMinutes = 1;
    public static double payloadFraction = 0.3; // Assume 30% of the total weight is the payload (adjust as needed)
    public static double efficiencyFactor = 0.9; // Efficiency factor for energy conversion
    public static int energyDensity = 250; // Energy density of the battery in Watt-hours per kilogram
    public static double averagePowerConsumption = 100; // Average power consumption of the drone in watts
    public static double voltageOfBattery = 11.1; //assuming a certain voltage, e.g., 11.1V for a 3S LiPo battery
    public static int averageDroneFlightInHour = 2;
    public static int currentDeliveredByChargingOutlet = 3; //assuming the current delivered by charging outlet is 2 ampere
}
