package org.testproject.drones.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.testproject.drones.model.enums.DroneModel;

@AllArgsConstructor
@Getter
@Setter
public class NewDrone {

    @NotBlank(message = "Serial number should not be null or empty")
    @Size(max = 100, message = "Length of serial number should be 100 characters max")
    private String serialNumber;

    @NotNull(message = "Model should not be null")
    private DroneModel model;

    @NotNull(message = "Weight limit should not be null")
    @Min(value = 0, message = "Weight limit should not be less than 0")
    @Max(value = 500, message = "Weight limit should not be more than 500")
    private Short weightLimit;

    @Min(value = 0, message = "Battery capacity should not be less than 0")
    @Max(value = 100, message = "Battery capacity should not be more than 100")
    private Short batteryCapacity;

}
