package org.testproject.drones.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DroneModel {
    LIGHTWEIGHT("Lightweight"),
    MIDDLEWEIGHT("Middleweight"),
    CRUISERWEIGHT("Cruiserweight"),
    HEAVYWEIGHT("Heavyweight");

    private final String name;

    DroneModel(String name) {
        this.name = name;
    }


    @JsonValue
    public String getName() {
        return name;
    }
}
