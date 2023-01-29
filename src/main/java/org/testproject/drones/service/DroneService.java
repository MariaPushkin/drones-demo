package org.testproject.drones.service;

import org.testproject.drones.model.dto.request.NewDrone;
import org.testproject.drones.model.dto.request.NewMedication;
import org.testproject.drones.model.dto.response.LoadedDrone;
import org.testproject.drones.model.dto.response.LoadedMedication;
import org.testproject.drones.model.dto.response.RegisteredDrone;

import java.util.List;

public interface DroneService {
    RegisteredDrone register(NewDrone newDrone);
    LoadedDrone load(String id, List<NewMedication> newMedication);
    List<LoadedMedication> getMedications(String id);
    Integer getBatteryLevel(String id);
    List<RegisteredDrone> getAvailableDrones();
}
