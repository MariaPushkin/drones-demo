package org.testproject.drones.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.testproject.drones.model.enums.DroneModel;
import org.testproject.drones.model.enums.DroneState;

@AllArgsConstructor
@Setter
@Getter
public class RegisteredDrone {
    private String id;
    private String serialNumber;
    private DroneModel model;
    private Short weightLimit;
    private Short batteryCapacity;
    private DroneState state;
}
