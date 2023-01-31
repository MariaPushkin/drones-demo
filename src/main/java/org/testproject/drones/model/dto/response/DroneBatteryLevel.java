package org.testproject.drones.model.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class DroneBatteryLevel {
    private String id;
    private Short batteryCapacity;
}
