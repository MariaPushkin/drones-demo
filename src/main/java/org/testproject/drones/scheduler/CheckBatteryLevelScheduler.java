package org.testproject.drones.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.testproject.drones.model.entity.DroneEntity;
import org.testproject.drones.model.enums.DroneState;
import org.testproject.drones.repository.DroneRepository;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class CheckBatteryLevelScheduler {
    Logger logger = LoggerFactory.getLogger("batteryFileLogger");
    private final DroneRepository droneRepository;

    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.MINUTES)
    public void checkBattery() {
        List<DroneEntity> drones = droneRepository.findAll();
        drones.forEach(drone -> {
            if (drone.getBatteryCapacity() < 25 && drone.getState().equals(DroneState.LOADING)) {
                drone.setState(DroneState.LOADED);
                droneRepository.save(drone);
            }

            logger.info(MarkerFactory.getMarker("BATTERY"),
                    "Current battery level for [" + drone.getId() + "] is " + drone.getBatteryCapacity());
        });
    }
}
