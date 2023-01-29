package org.testproject.drones.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.testproject.drones.exception.DroneIsNotAvailableException;
import org.testproject.drones.exception.DroneNotFoundException;
import org.testproject.drones.exception.NotEnoughSpaceException;
import org.testproject.drones.mapper.DroneMapper;
import org.testproject.drones.mapper.MedicationMapper;
import org.testproject.drones.model.dto.request.NewDrone;
import org.testproject.drones.model.dto.request.NewMedication;
import org.testproject.drones.model.dto.response.LoadedDrone;
import org.testproject.drones.model.dto.response.LoadedMedication;
import org.testproject.drones.model.dto.response.RegisteredDrone;
import org.testproject.drones.model.entity.DroneEntity;
import org.testproject.drones.model.entity.MedicationEntity;
import org.testproject.drones.model.enums.DroneState;
import org.testproject.drones.repository.DroneRepository;
import org.testproject.drones.service.DroneService;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DroneServiceImpl implements DroneService {
    private final DroneRepository droneRepository;
    private final DroneMapper droneMapper;
    private final MedicationMapper medicationMapper;

    @Override
    public RegisteredDrone register(NewDrone newDrone) {
        return droneMapper.toRegisteredDrone(droneRepository.save(
                        droneMapper.toDroneEntity(newDrone)));
    }

    @Override
    public LoadedDrone load(String id, List<NewMedication> medications) {
        Optional<DroneEntity> optDrone = droneRepository.findById(id);
        DroneEntity drone = optDrone.orElseThrow(() -> new DroneNotFoundException(id));
        if (!drone.isAvailableForLoading()) {
            throw new DroneIsNotAvailableException(id);
        }
        if (drone.getRemainingWeightCapacity() < this.getMedicationsWeight(medications)) {
            throw new NotEnoughSpaceException(id);
        }
        List<MedicationEntity> currentMedications = drone.getMedications();
        List<MedicationEntity> newMedications = medicationMapper.toMedicationEntityList(medications);
        newMedications.forEach(medicationEntity -> medicationEntity.setDrone(drone));
        currentMedications.addAll(newMedications);
        drone.setMedications(currentMedications);
        if (drone.getRemainingWeightCapacity() == 0) {
            drone.setState(DroneState.LOADED);
        }
        return droneMapper.toLoadedDrone(droneRepository.save(drone));
    }

    @Override
    public List<LoadedMedication> getMedications(String id) {
        Optional<DroneEntity> optDrone = droneRepository.findById(id);
        DroneEntity drone = optDrone.orElseThrow(() -> new DroneNotFoundException(id));
        return medicationMapper.toLoadedMedicationList(drone.getMedications());
    }

    @Override
    public Integer getBatteryLevel(String id) {
        Optional<DroneEntity> optDrone = droneRepository.findById(id);
        DroneEntity drone = optDrone.orElseThrow(() -> new DroneNotFoundException(id));
        return Integer.valueOf(drone.getBatteryCapacity());
    }

    @Override
    public List<RegisteredDrone> getAvailableDrones() {
        return null;
    }

    private int getMedicationsWeight(List<NewMedication> medications) {
        return medications.stream()
                .mapToInt(NewMedication::getWeight)
                .sum();
    }



}
