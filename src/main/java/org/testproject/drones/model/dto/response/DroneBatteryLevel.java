package org.testproject.drones.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class DroneBatteryLevel {
    private String id;
    private Short batteryCapacity;
}
