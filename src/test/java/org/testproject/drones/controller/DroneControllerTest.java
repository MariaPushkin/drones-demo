package org.testproject.drones.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testproject.drones.mapper.MedicationMapper;
import org.testproject.drones.mapper.MedicationMapperImpl;
import org.testproject.drones.model.dto.request.NewDrone;
import org.testproject.drones.model.dto.request.NewMedication;
import org.testproject.drones.model.dto.response.DroneBatteryLevel;
import org.testproject.drones.model.dto.response.LoadedDrone;
import org.testproject.drones.model.dto.response.LoadedMedication;
import org.testproject.drones.model.dto.response.RegisteredDrone;
import org.testproject.drones.model.entity.MedicationEntity;
import org.testproject.drones.model.enums.DroneModel;
import org.testproject.drones.model.enums.DroneState;
import org.testproject.drones.service.DroneService;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(DroneController.class)
@Import(MedicationMapperImpl.class)
class DroneControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    MedicationMapper medicationMapper;
    @MockBean
    private DroneService droneService;

    @Test
    void registerDrone() throws Exception {
        final String serialNumber = "GFV_75";
        final short weightLimit = 400;
        final short batteryCapacity = 100;
        NewDrone newDrone = NewDrone.builder()
                .serialNumber(serialNumber)
                .weightLimit(weightLimit)
                .model(DroneModel.CRUISERWEIGHT)
                .build();
        RegisteredDrone registeredDrone = RegisteredDrone.builder()
                .id(UUID.randomUUID().toString())
                .serialNumber(serialNumber)
                .weightLimit(weightLimit)
                .model(DroneModel.CRUISERWEIGHT)
                .batteryCapacity(batteryCapacity)
                .state(DroneState.IDLE)
                .build();

        when(droneService.register(any())).thenReturn(registeredDrone);
        mvc.perform(post("/drones")
                .content(asJsonString(newDrone))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(registeredDrone.getId())))
                .andExpect(jsonPath("$.serialNumber", is(registeredDrone.getSerialNumber())))
                .andExpect(jsonPath("$.model", is(registeredDrone.getModel().getName())))
                .andExpect(jsonPath("$.weightLimit", is((int)registeredDrone.getWeightLimit())))
                .andExpect(jsonPath("$.batteryCapacity", is((int)registeredDrone.getBatteryCapacity())))
                .andExpect(jsonPath("$.state", is(registeredDrone.getState().toString())));
    }

    @Test
    void loadDrone() throws Exception {
        final String droneId = UUID.randomUUID().toString();
        NewMedication newMedication = NewMedication.builder()
                .name("pill")
                .code("abc1223")
                .weight((short)40)
                .build();
        MedicationEntity medication = medicationMapper.toMedicationEntity(newMedication);
        LoadedDrone loadedDrone = LoadedDrone.builder()
                .id(droneId)
                .serialNumber("GFV_75")
                .weightLimit((short)200)
                .model(DroneModel.MIDDLEWEIGHT)
                .batteryCapacity((short)100)
                .state(DroneState.LOADING)
                .medications(List.of(medication))
                .build();
        when(droneService.load(anyString(), anyList())).thenReturn(loadedDrone);
        mvc.perform(post("/drones/" + droneId + "/load")
                .content(asJsonString(List.of(newMedication)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(loadedDrone.getId())))
                .andExpect(jsonPath("$.serialNumber", is(loadedDrone.getSerialNumber())))
                .andExpect(jsonPath("$.model", is(loadedDrone.getModel().getName())))
                .andExpect(jsonPath("$.weightLimit", is((int)loadedDrone.getWeightLimit())))
                .andExpect(jsonPath("$.batteryCapacity", is((int)loadedDrone.getBatteryCapacity())))
                .andExpect(jsonPath("$.state", is(loadedDrone.getState().toString())))
                .andExpect(jsonPath("$.medications[0].name", is(newMedication.getName())))
                .andExpect(jsonPath("$.medications[0].weight", is((int)newMedication.getWeight())))
                .andExpect(jsonPath("$.medications[0].code", is(newMedication.getCode())));
    }

    @Test
    void getMedications() throws Exception {
        LoadedMedication medication1 = LoadedMedication.builder()
                .code("fgfh232")
                .name("aaa")
                .weight((short)10)
                .build();
        LoadedMedication medication2 = LoadedMedication.builder()
                .code("fgdh35")
                .name("bbb")
                .weight((short)20)
                .build();
        List<LoadedMedication> medications = List.of(medication1, medication2);
        when(droneService.getMedications(anyString())).thenReturn(medications);
        mvc.perform(get("/drones/" + UUID.randomUUID().toString() + "/medications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(medication1.getName())))
                .andExpect(jsonPath("$[0].weight", is((int)medication1.getWeight())))
                .andExpect(jsonPath("$[0].code", is(medication1.getCode())))
                .andExpect(jsonPath("$[1].name", is(medication2.getName())))
                .andExpect(jsonPath("$[1].weight", is((int)medication2.getWeight())))
                .andExpect(jsonPath("$[1].code", is(medication2.getCode())));
    }

    @Test
    void getBatteryLevel() throws Exception {
        final String droneId = UUID.randomUUID().toString();
        DroneBatteryLevel droneBatteryLevel = DroneBatteryLevel.builder()
                .batteryCapacity((short)50)
                .id(droneId)
                .build();
        when(droneService.getBatteryLevel(droneId)).thenReturn(droneBatteryLevel);
        mvc.perform(get("/drones/" + droneId + "/battery"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(droneBatteryLevel.getId())))
                .andExpect(jsonPath("$.batteryCapacity", is((int)droneBatteryLevel.getBatteryCapacity())));
    }

    @Test
    void getAvailableDrones() throws Exception {
        final String serialNumber1 = "sdd_123";
        final String serialNumber2 = "sdd_456";
        RegisteredDrone registeredDrone1 = RegisteredDrone.builder()
                .id(UUID.randomUUID().toString())
                .serialNumber(serialNumber1)
                .weightLimit((short)400)
                .model(DroneModel.CRUISERWEIGHT)
                .batteryCapacity((short)75)
                .state(DroneState.IDLE)
                .build();
        RegisteredDrone registeredDrone2 = RegisteredDrone.builder()
                .id(UUID.randomUUID().toString())
                .serialNumber(serialNumber2)
                .weightLimit((short)500)
                .model(DroneModel.HEAVYWEIGHT)
                .batteryCapacity((short)40)
                .state(DroneState.LOADING)
                .build();
        when(droneService.getAvailableDrones()).thenReturn(List.of(registeredDrone1, registeredDrone2));
        mvc.perform(get("/drones/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].serialNumber", is(registeredDrone1.getSerialNumber())))
                .andExpect(jsonPath("$[1].serialNumber", is(registeredDrone2.getSerialNumber())));
    }


    private static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}