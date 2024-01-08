package com.example.drones;


import com.example.drones.entity.ChargingLog;
import com.example.drones.entity.Drone;
import com.example.drones.entity.Medication;
import com.example.drones.entity.Trip;
import com.example.drones.repository.ChargingLogRepository;
import com.example.drones.repository.DroneRepository;
import com.example.drones.repository.MedicationRepository;
import com.example.drones.repository.TripRepository;
import com.example.drones.request.AddDroneRequest;
import com.example.drones.service.impl.DroneService;
import com.example.drones.utils.CreateResponse;
import com.example.drones.utils.State;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DroneServiceTest {


    @Autowired
    private DroneService droneService;

    @MockBean
    private DroneRepository droneRepository;

    @MockBean
    private TripRepository tripRepository;

    @MockBean
    private MedicationRepository medicationRepository;

    @MockBean
    private ChargingLogRepository chargingLogRepository;

    private AddDroneRequest addDroneRequest = new AddDroneRequest();
    private CreateResponse<Drone> createResponse = new CreateResponse<Drone>();
    private Drone drone = new Drone();
    private Trip trip = new Trip();
    private ChargingLog chargingLog = new ChargingLog();

    @Test
    void createDrone_Should_Return_400_When_SerialNumber_Is_Empty_Or_Blank() {

        //arrange
        addDroneRequest.setModel("T1");
        addDroneRequest.setSerialNumber("Test 01");
        addDroneRequest.setWeightLimit(400);
        drone.setBatteryCapacity(400);
        drone.setSerialNumber("Test 01");
        drone.setModel("T1");
        createResponse.setResponseDescription("Drone with " + addDroneRequest.getSerialNumber() + " already exist");
        createResponse.setResponseCode(HttpStatus.BAD_REQUEST);

        //act
        when(droneRepository.findBySerialNumber(addDroneRequest.getSerialNumber())).thenReturn(drone);
        var droneRep = droneService.createDrone(addDroneRequest);

        //assert
        assertEquals(createResponse.getResponseCode(), droneRep.getResponseCode());
        assertEquals(createResponse.getResponseDescription(), droneRep.getResponseDescription());
    }

    @Test
    void createDrone_Should_Call_Save_From_DroneRepository() {

        //arrange
        addDroneRequest.setModel("T1");
        addDroneRequest.setSerialNumber("Test 01");
        addDroneRequest.setWeightLimit(400);
        drone.setBatteryCapacity(400);
        drone.setSerialNumber("Test 01");
        drone.setModel("T1");

        //act
        when(droneRepository.save(drone)).thenReturn(drone);
        var droneRep = droneService.createDrone(addDroneRequest);

        //assert
        assertEquals(drone, droneRepository.save(drone));
    }

    @Test
    void createDrone_Should_Return_500_If_Id_Or_SerialNumber_Is_Not_Passed() {

        //arrange
        createResponse.setResponseDescription("An error occur");
        createResponse.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR);

        //act
        var droneRep = droneService.createDrone(null);

        //assert
        assertEquals(createResponse.getResponseCode(), droneRep.getResponseCode());
        assertEquals(createResponse.getResponseDescription(), droneRep.getResponseDescription());
    }

    @Test
    void editDrone_Should_Return_400_When_Drone_Is_NotFound() {
        //arrange
        addDroneRequest.setId(1);
        addDroneRequest.setModel("T1");
        addDroneRequest.setSerialNumber("Test 01");
        addDroneRequest.setWeightLimit(400);
        createResponse.setResponseDescription("Drone not found");
        createResponse.setResponseCode(HttpStatus.BAD_REQUEST);

        //act
        when(droneRepository.findById(addDroneRequest.getId())).thenReturn(null);
        when(droneRepository.findBySerialNumber(addDroneRequest.getSerialNumber())).thenReturn(null);
        var droneRep = droneService.editDrone(addDroneRequest);

        //assert
        assertEquals(createResponse.getResponseCode(), droneRep.getResponseCode());
        assertEquals(createResponse.getResponseDescription(), droneRep.getResponseDescription());
    }

    @Test
    void editDrone_Should_Call_Save_From_DroneRepository() {
        //arrange
        addDroneRequest.setId(1);
        addDroneRequest.setModel("T1");
        addDroneRequest.setSerialNumber("Test 01");
        addDroneRequest.setWeightLimit(400);
        drone.setBatteryCapacity(400);
        drone.setSerialNumber("Test 01");
        drone.setModel("T1");

        //act
        when(droneRepository.save(drone)).thenReturn(drone);
        var droneRep = droneService.editDrone(addDroneRequest);

        //assert
        assertEquals(drone, droneRepository.save(drone));
    }

    @Test
    void editDrone_Should_Return_500_If_Id_Or_SerialNumber_Is_Not_Passed() {

        //arrange
        createResponse.setResponseDescription("An error occur");
        createResponse.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR);

        //act
        var droneRep = droneService.editDrone(null);

        //assert
        assertEquals(createResponse.getResponseCode(), droneRep.getResponseCode());
        assertEquals(createResponse.getResponseDescription(), droneRep.getResponseDescription());
    }

    @Test
    void getDrones_Should_Call_FindAll_From_DroneRepository() {
        //arrange
        List<Drone> droneList = new ArrayList<Drone>();

        //act
        when(droneRepository.findAll()).thenReturn(droneList);
        var droneRep = droneService.getDrones();

        //assert
        assertEquals(droneList, droneRepository.findAll());
    }

    @Test
    void getDrone_Should_Call_FindById_From_DroneRepository() {

        //act
        when(droneRepository.findById(0)).thenReturn(Optional.of(drone));
        var drones = droneService.getDrone(0);

        //assert
        assertEquals(drone, droneRepository.findById(0).get());
    }

    @Test
    void getDroneBySerialNumber_Should_Call_FindBySerialNumber_From_DroneRepository() {

        //act
        when(droneRepository.findBySerialNumber("Test 01")).thenReturn(drone);
        var droneRep = droneService.getDroneBySerialNumber("Test 01");

        //assert
        assertEquals(drone, droneRepository.findBySerialNumber("Test 01"));
    }

    @Test
    void deleteDrone_Should_Call_Delete_From_DroneRepository() {

        //act
        var droneRep = droneService.deleteDrone(drone);

        //assert
        verify(droneRepository).delete(drone);
    }

    @Test
    void availableDrones_Should_Call_FindByState_From_DroneRepository() {

        //arrange
        var arr = new ArrayList<Drone>();

        //act
        when(droneRepository.findByState(State.IDLE.name())).thenReturn(arr);
        var droneRep = droneService.availableDrones();

        //assert
        assertEquals(arr, droneRepository.findByState(State.IDLE.name()));
    }

    @Test
    void batteryLevel_Should_Call_GetDroneBySerialNumber_From_DroneRepository() {

        //act
        var droneRep = droneService.batteryLevel("Test 01");

        //assert
        verify(droneRepository).findBySerialNumber("Test 01");
    }

    @Test
    void checkLoad_Should_Call_GetDroneBySerialNumber_From_DroneRepository() {

        //act
        var droneRep = droneService.checkLoaded("Test 01");

        //assert
        verify(droneRepository).findBySerialNumber("Test 01");
    }

    @Test
    void checkLoad_Should_Return_400_When_Drone_Is_NotFound() {

        //arrange
        createResponse.setResponseCode(HttpStatus.BAD_REQUEST);
        createResponse.setResponseDescription("Drone not found");

        //act
        when(droneRepository.findBySerialNumber("Test 01")).thenReturn(null);
        var droneRep = droneService.checkLoaded("Test 01");

        //assert
        assertEquals(createResponse.getResponseCode(), droneRep.getResponseCode());
        assertEquals(createResponse.getResponseDescription(), droneRep.getResponseDescription());
    }

    @Test
    void checkLoad_Should_Call_FindByDroneIdAndHasEnd_From_TripRepository() {

        //arrange
        Trip trip = new Trip();
        trip.setState(State.DELIVERING.name());

        drone.setBatteryCapacity(400);
        drone.setSerialNumber("Test 01");
        drone.setModel("T1");


        //act
        when(droneRepository.findBySerialNumber("Test 01")).thenReturn(drone);
        when(tripRepository.findByDroneIdAndHasEnd(0,false)).thenReturn(trip);
        var droneRep = droneService.checkLoaded("Test 01");

        //assert
        verify(tripRepository).findByDroneIdAndHasEnd(0,false);
    }

    @Test
    void checkLoad_Should_Call_FindByTripId_From_MedicationRepository() {

        //arrange
        trip.setState(State.DELIVERING.name());

        drone.setBatteryCapacity(400);
        drone.setSerialNumber("Test 01");
        drone.setModel("T1");

        //act
        when(droneRepository.findBySerialNumber("Test 01")).thenReturn(drone);
        when(tripRepository.findByDroneIdAndHasEnd(0,false)).thenReturn(trip);
        when(medicationRepository.findByTripId(0)).thenReturn(new ArrayList<Medication>());
        var droneRep = droneService.checkLoaded("Test 01");

        //assert
        verify(medicationRepository).findByTripId(0);
    }

    @Test
    void initiateCharging_Should_Call_FindBySerialNumber_From_DroneRepository() {

        //act
        when(droneRepository.findBySerialNumber("Test 01")).thenReturn(drone);
        when(chargingLogRepository.save(new ChargingLog())).thenReturn(new ChargingLog());
        var droneRep = droneService.initiateCharging("Test 01");

        //assert
        verify(droneRepository).findBySerialNumber("Test 01");
    }

    @Test
    void initiateCharging_Should_Call_FindByTripId_From_ChargingLogRepository() {
        //arrange
        drone.setWeightLimit(80);
        drone.setBatteryCapacity(90);
        drone.setModel("T1");
        drone.setSerialNumber("Test 01");
        chargingLog.setDrone(drone);
        chargingLog.setFullyCharged(false);
        chargingLog.setId(0);
        chargingLog.setCreatedOn(LocalDateTime.now());

        //act
        when(chargingLogRepository.save(chargingLog)).thenReturn(chargingLog);
        var droneRep = droneService.initiateCharging("Test 01");

        //assert
        assertEquals(chargingLog,chargingLogRepository.save(chargingLog));
    }

    @Test
    void processCharging_Should_Call_FindByIsFullyCharged_From_ChargingLogRepository() {

        //act
        var droneRep = droneService.processCharging();

        //assert
        verify(chargingLogRepository).findByIsFullyCharged(false);
    }



}
