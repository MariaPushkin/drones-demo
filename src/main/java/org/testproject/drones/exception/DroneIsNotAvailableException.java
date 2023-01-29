package org.testproject.drones.exception;

public class DroneIsNotAvailableException extends RuntimeException {
    private final String droneId;

    public DroneIsNotAvailableException(String droneId) {
        this.droneId = droneId;
    }

    @Override
    public String getMessage() {
        return "Drone [" + droneId + "] can not be loaded";
    }
}
