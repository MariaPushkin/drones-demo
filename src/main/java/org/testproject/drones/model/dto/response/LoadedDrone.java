package org.testproject.drones.model.dto.response;

import lombok.*;
import org.testproject.drones.model.entity.MedicationEntity;
import org.testproject.drones.model.enums.DroneModel;
import org.testproject.drones.model.enums.DroneState;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class LoadedDrone {
    private String id;
    private String serialNumber;
    private DroneModel model;
    private Short weightLimit;
    private Short batteryCapacity;
    private DroneState state;
    private List<MedicationEntity> medications;
}
