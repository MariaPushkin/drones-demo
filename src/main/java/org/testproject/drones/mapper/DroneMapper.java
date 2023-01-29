package org.testproject.drones.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.testproject.drones.model.dto.request.NewDrone;
import org.testproject.drones.model.dto.response.LoadedDrone;
import org.testproject.drones.model.dto.response.RegisteredDrone;
import org.testproject.drones.model.entity.DroneEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DroneMapper {

    @Mapping(source = "batteryCapacity", target = "batteryCapacity", defaultValue = "100")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "medications", ignore = true)
    @Mapping(target = "state", constant = "IDLE")
    DroneEntity toDroneEntity(NewDrone newDrone);
    RegisteredDrone toRegisteredDrone(DroneEntity drone);
    LoadedDrone toLoadedDrone(DroneEntity drone);
    List<RegisteredDrone> toRegisteredDroneList(List<DroneEntity> drones);
}
