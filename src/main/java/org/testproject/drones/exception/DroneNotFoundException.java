package org.testproject.drones.exception;

public class DroneNotFoundException extends RuntimeException {
    private final String droneId;
    public DroneNotFoundException(String droneId) {
        this.droneId = droneId;
    }
    @Override
    public String getMessage() {
        return "Drone [" + droneId + "] can not be found";
    }
}
