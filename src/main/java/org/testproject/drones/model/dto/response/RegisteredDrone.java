package org.testproject.drones.model.dto.response;

import lombok.*;
import org.testproject.drones.model.enums.DroneModel;
import org.testproject.drones.model.enums.DroneState;

@AllArgsConstructor
@NoArgsConstructor
@Builder
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
