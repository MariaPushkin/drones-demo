package org.testproject.drones.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.testproject.drones.model.dto.request.NewDrone;
import org.testproject.drones.model.dto.request.NewMedication;
import org.testproject.drones.model.dto.response.LoadedDrone;
import org.testproject.drones.model.dto.response.LoadedMedication;
import org.testproject.drones.model.dto.response.RegisteredDrone;
import org.testproject.drones.service.DroneService;

import java.util.List;


@RestController
@RequestMapping("/drones")
@RequiredArgsConstructor
public class DroneController {

    private final DroneService droneService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<RegisteredDrone> registerDrone(@Valid @RequestBody NewDrone newDrone) {
        return new ResponseEntity<>(droneService.register(newDrone), HttpStatus.CREATED);
    }

    @PostMapping(
            path = "{id}/load",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<LoadedDrone> loadDrone(@PathVariable String id,
                                                 @Valid @RequestBody List<NewMedication> medications) {
        return new ResponseEntity<>(droneService.load(id, medications), HttpStatus.OK);
    }


    @GetMapping(
            path = "{id}/medications",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<LoadedMedication>> getMedications(@PathVariable String id) {
        return new ResponseEntity<>(droneService.getMedications(id), HttpStatus.OK);
    }

    @GetMapping(
            path = "{id}/battery",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Integer> getBatteryLevel(@PathVariable String id) {
        return new ResponseEntity<>(droneService.getBatteryLevel(id), HttpStatus.OK);
    }

    @GetMapping(
            path = "available",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<RegisteredDrone>> getAvailableDrones(){
        return new ResponseEntity<>(droneService.getAvailableDrones(), HttpStatus.OK);
    }
}
