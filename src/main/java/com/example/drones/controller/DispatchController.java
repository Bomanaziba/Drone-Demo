package com.example.drones.controller;

import com.example.drones.request.LoadDroneRequest;
import com.example.drones.service.DispatchService;
import com.example.drones.utils.CreateResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dispatch")
public class DispatchController {

    @Autowired
    DispatchService dispatchService;

    @RequestMapping(value = "load", method = RequestMethod.POST)
    public ResponseEntity<?> load(@Valid  @RequestBody LoadDroneRequest request) {
        var res=  dispatchService.loadMedication(request);
        return new ResponseEntity<CreateResponse<?>>(res,res.getResponseCode());
    }

    @RequestMapping(value = "start", method = RequestMethod.POST)
    public ResponseEntity<?> start(@RequestParam Integer dispatchId) {
        var res=  dispatchService.startDispatch(dispatchId);
        return new ResponseEntity<CreateResponse<?>>(res,res.getResponseCode());
    }

}
