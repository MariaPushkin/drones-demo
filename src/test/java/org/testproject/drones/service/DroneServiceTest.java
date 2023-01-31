package org.testproject.drones.service;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.testproject.drones.exception.DroneIsNotAvailableException;
import org.testproject.drones.exception.DroneNotFoundException;
import org.testproject.drones.exception.NotEnoughSpaceException;
import org.testproject.drones.mapper.DroneMapper;
import org.testproject.drones.mapper.DroneMapperImpl;
import org.testproject.drones.mapper.MedicationMapper;
import org.testproject.drones.mapper.MedicationMapperImpl;
import org.testproject.drones.model.dto.request.NewDrone;
import org.testproject.drones.model.dto.request.NewMedication;
import org.testproject.drones.model.dto.response.DroneBatteryLevel;
import org.testproject.drones.model.dto.response.LoadedDrone;
import org.testproject.drones.model.dto.response.LoadedMedication;
import org.testproject.drones.model.dto.response.RegisteredDrone;
import org.testproject.drones.model.entity.DroneEntity;
import org.testproject.drones.model.entity.MedicationEntity;
import org.testproject.drones.model.enums.DroneModel;
import org.testproject.drones.model.enums.DroneState;
import org.testproject.drones.repository.DroneRepository;
import org.testproject.drones.service.DroneService;
import org.testproject.drones.service.impl.DroneServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DroneServiceTest {
    @InjectMocks
    DroneServiceImpl droneService;
    @Mock
    DroneMapper droneMapper;
    @Mock
    MedicationMapper medicationMapper;
    @Mock
    DroneRepository droneRepository;

    @Test
    void register() {
        DroneEntity droneEntity = new DroneEntity();
        when(droneRepository.save(any())).thenReturn(droneEntity);
        when(droneMapper.toDroneEntity(any())).thenReturn(droneEntity);
        String droneId = UUID.randomUUID().toString();
        when(droneMapper.toRegisteredDrone(any())).thenReturn(RegisteredDrone.builder()
                .id(droneId).build());

        RegisteredDrone createdDrone = droneService.register(new NewDrone());

        verify(droneRepository, times(1)).save((any()));
        verify(droneMapper, times(1)).toDroneEntity((any()));
        verify(droneMapper, times(1)).toRegisteredDrone((any()));
        assertEquals(droneId, createdDrone.getId());
    }

    @Test
    void load() {
        String droneId = UUID.randomUUID().toString();
        List<MedicationEntity> medicationEntities = new ArrayList<>();
        medicationEntities.add(MedicationEntity.builder()
                .weight((short)15)
                .build());
        DroneEntity droneEntity = DroneEntity.builder()
                .id(droneId)
                .serialNumber("adc_7655")
                .state(DroneState.IDLE)
                .weightLimit((short)200)
                .model(DroneModel.MIDDLEWEIGHT)
                .batteryCapacity((short)75)
                .medications(medicationEntities)
                .build();
        when(droneRepository.findById(droneId)).thenReturn(Optional.of(droneEntity));
        when(medicationMapper.toMedicationEntityList(any())).thenReturn(List.of(MedicationEntity
                .builder().weight((short)30).build()));
        when(droneRepository.save(any())).thenReturn(droneEntity);
        when(droneMapper.toLoadedDrone(any())).thenReturn(LoadedDrone.builder()
                .id(droneId).build());

        LoadedDrone drone = droneService.load(droneId, List.of(NewMedication.builder()
                .weight((short)30).build()));

        verify(droneRepository, times(1)).save((any()));
        verify(droneRepository, times(1)).findById(droneId);
        verify(droneMapper, times(1)).toLoadedDrone((any()));
        verify(medicationMapper, times(1)).toMedicationEntityList((any()));
        assertEquals(droneId, drone.getId());
    }

    @Test
    void loadButDroneNotFound() {
        String droneId = UUID.randomUUID().toString();
        when(droneRepository.findById(droneId)).thenReturn(Optional.empty());

        DroneNotFoundException thrown = assertThrows(DroneNotFoundException.class,
                () -> droneService.load(droneId, List.of(new NewMedication())),
                "DroneNotFoundException error was expected");

        assertEquals(new DroneNotFoundException(droneId).getMessage(), thrown.getMessage());
    }

    @Test
    void loadButDroneNotAvailable() {
        String droneId = UUID.randomUUID().toString();
        DroneEntity droneEntity = DroneEntity.builder()
                .id(droneId)
                .state(DroneState.LOADED)
                .build();

        when(droneRepository.findById(droneId)).thenReturn(Optional.of(droneEntity));

        DroneIsNotAvailableException thrown = assertThrows(DroneIsNotAvailableException.class,
                () -> droneService.load(droneId, List.of(new NewMedication())),
                "DroneIsNotAvailableException error was expected");

        assertEquals(new DroneIsNotAvailableException(droneId).getMessage(), thrown.getMessage());
    }

    @Test
    void loadButNotEnoughSpace() {
        String droneId = UUID.randomUUID().toString();
        List<MedicationEntity> medicationEntities = new ArrayList<>();
        medicationEntities.add(MedicationEntity.builder()
                .weight((short)100)
                .build());
        DroneEntity droneEntity = DroneEntity.builder()
                .id(droneId)
                .serialNumber("adc_7655")
                .state(DroneState.IDLE)
                .weightLimit((short)150)
                .model(DroneModel.MIDDLEWEIGHT)
                .batteryCapacity((short)75)
                .medications(medicationEntities)
                .build();
        when(droneRepository.findById(droneId)).thenReturn(Optional.of(droneEntity));

        NotEnoughSpaceException thrown = assertThrows(NotEnoughSpaceException.class,
                () -> droneService.load(droneId, List.of(NewMedication.builder()
                        .weight((short)75).build())),
                "NotEnoughSpaceException error was expected");

        assertEquals(new NotEnoughSpaceException(droneId).getMessage(), thrown.getMessage());
    }

    @Test
    void getMedications() {
        String droneId = UUID.randomUUID().toString();
        DroneEntity droneEntity = DroneEntity.builder()
                .id(droneId)
                .build();
        when(droneRepository.findById(droneId)).thenReturn(Optional.of(droneEntity));
        when(medicationMapper.toLoadedMedicationList(any())).thenReturn(List.of(new LoadedMedication()));

        droneService.getMedications(droneId);

        verify(medicationMapper, times(1)).toLoadedMedicationList((any()));
        verify(droneRepository, times(1)).findById(droneId);
    }

    @Test
    void getBatteryLevel() {
        String droneId = UUID.randomUUID().toString();
        DroneEntity droneEntity = DroneEntity.builder()
                .id(droneId)
                .batteryCapacity((short)45)
                .build();
        when(droneRepository.findById(droneId)).thenReturn(Optional.of(droneEntity));
        when(droneMapper.toDroneBatteryLevel(any())).thenReturn(DroneBatteryLevel.builder()
                .id(droneId).batteryCapacity(droneEntity.getBatteryCapacity()).build());

        DroneBatteryLevel result = droneService.getBatteryLevel(droneId);

        verify(droneMapper, times(1)).toDroneBatteryLevel((any()));
        verify(droneRepository, times(1)).findById(droneId);
        assertEquals(droneEntity.getBatteryCapacity(), result.getBatteryCapacity());
    }

    @Test
    void getAvailableDrones() {
        String droneId1 = UUID.randomUUID().toString();
        String droneId2 = UUID.randomUUID().toString();
        DroneEntity droneEntity1 = DroneEntity.builder()
                .id(droneId1)
                .build();
        DroneEntity droneEntity2 = DroneEntity.builder()
                .id(droneId2)
                .build();
        when(droneRepository.findAllByStateIn(anyList())).thenReturn(List.of(droneEntity1, droneEntity2));
        when(droneMapper.toRegisteredDroneList(anyList())).thenReturn(List.of(
                RegisteredDrone.builder().id(droneId1).build(),
                RegisteredDrone.builder().id(droneId2).build()
        ));

        List<RegisteredDrone> result = droneService.getAvailableDrones();

        verify(droneMapper, times(1)).toRegisteredDroneList((anyList()));
        verify(droneRepository, times(1)).findAllByStateIn(anyList());
        assertEquals(droneId1, result.get(0).getId());
        assertEquals(droneId2, result.get(1).getId());
    }
}