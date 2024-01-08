package com.example.drones;

import com.example.drones.dto.MedicationDto;
import com.example.drones.entity.Drone;
import com.example.drones.entity.Trip;
import com.example.drones.repository.DroneRepository;
import com.example.drones.repository.TripRepository;
import com.example.drones.request.LoadDroneRequest;
import com.example.drones.response.LoadDroneResponse;
import com.example.drones.service.impl.DispatchService;
import com.example.drones.utils.Constant;
import com.example.drones.utils.CreateResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@SpringBootTest
public class DispatchServiceTests {

    @Autowired
    private DispatchService dispatchService;

    @MockBean
    private DroneRepository droneRepository;

    @MockBean
    private TripRepository tripRepository;

    private LoadDroneRequest loadDroneRequest = new LoadDroneRequest();
    private MedicationDto medication = new MedicationDto();
    private Drone drone = new Drone();
    private Trip trip = new Trip();

    @Test
    void loadMedication_Should_Return_400_When_WeightExceedLimit() {

        //arrange
        loadDroneRequest.setSerialNumber("Test 01");
        medication.setDeliveryTime(50);
        medication.setCode("T1");
        medication.setWeight(400);
        medication.setName("TM");
        loadDroneRequest.setMedication(medication);
        drone.setSerialNumber("Test 01");
        drone.setWeightLimit(200);
        drone.setBatteryCapacity(100);
        CreateResponse<LoadDroneResponse> res = new CreateResponse<LoadDroneResponse>();
        res.setResponseCode(HttpStatus.BAD_REQUEST);
        res.setResponseDescription("Drone weight limit exceeded");

        //act
        when(droneRepository.findBySerialNumber("Test 01")).thenReturn(drone);
        var dispatch = dispatchService.loadMedication(loadDroneRequest);

        //assert
        assertEquals(res.getResponseDescription(), dispatch.getResponseDescription());
        assertEquals(res.getResponseCode(), dispatch.getResponseCode());
    }

    @Test
    void loadMedication_Should_Return_400_When_Battery_Below_25_Percent() {

        //arrange
        loadDroneRequest.setSerialNumber("Test 01");
        medication.setDeliveryTime(50);
        medication.setCode("T1");
        medication.setWeight(100);
        medication.setName("TM");
        loadDroneRequest.setMedication(medication);
        drone.setSerialNumber("Test 01");
        drone.setWeightLimit(200);
        drone.setBatteryCapacity(25);
        CreateResponse<LoadDroneResponse> res = new CreateResponse<LoadDroneResponse>();
        res.setResponseCode(HttpStatus.BAD_REQUEST);
        res.setResponseDescription("Drone battery is low. It's less than " + Constant.miniBattery + "%, please charge");

        //act
        when(droneRepository.findBySerialNumber("Test 01")).thenReturn(new Drone());
        var dispatch = dispatchService.loadMedication(loadDroneRequest);

        //assert
        assertEquals(res.getResponseDescription(), dispatch.getResponseDescription());
        assertEquals(res.getResponseCode(), dispatch.getResponseCode());
    }

    @Test
    void loadMedication_Should_Call_Save_From_TripRepository() {

        //arrange
        loadDroneRequest.setSerialNumber("Test 01");
        medication.setDeliveryTime(50);
        medication.setCode("T1");
        medication.setWeight(100);
        medication.setName("TM");
        loadDroneRequest.setMedication(medication);
        drone.setSerialNumber("Test 01");
        drone.setWeightLimit(200);
        drone.setBatteryCapacity(25);
        Trip trip = new Trip();
        trip.setHasEnd(false);
        trip.setState("DELIVERED");

        //act
        when(tripRepository.save(trip)).thenReturn(trip);
        dispatchService.loadMedication(loadDroneRequest);

        //assert
        assertEquals(trip,tripRepository.save(trip));
    }

    @Test
    void startDispatch_Should_Call_FindById_From_TripRepository() {

        //act
        var droneRep = dispatchService.startDispatch(1);

        //assert
        verify(tripRepository).findById(1);
    }

    @Test
    void startDispatch_Should_Call_FindById_From_DroneRepository() {

        //arrange
        drone.setId(1);
        drone.setModel("T1");
        drone.setBatteryCapacity(90);
        drone.setWeightLimit(250);
        drone.setSerialNumber("Test 01");
        trip.setDrone(drone);

        //act
        when(tripRepository.findById(1)).thenReturn(Optional.of(trip));
        when(tripRepository.save(trip)).thenReturn(trip);
        when(droneRepository.findById(1)).thenReturn(Optional.of(drone));
        when(droneRepository.save(drone)).thenReturn(drone);

        var droneRep = dispatchService.startDispatch(1);

        //assert
        verify(droneRepository).findById(1);
    }

}
