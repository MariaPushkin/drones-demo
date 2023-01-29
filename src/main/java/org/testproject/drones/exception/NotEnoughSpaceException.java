package org.testproject.drones.exception;

public class NotEnoughSpaceException extends RuntimeException {
    private final String droneId;

    public NotEnoughSpaceException(String droneId) {
        this.droneId = droneId;
    }

    @Override
    public String getMessage() {
        return "Drone [" + droneId + "] do not have enough space";
    }
}
