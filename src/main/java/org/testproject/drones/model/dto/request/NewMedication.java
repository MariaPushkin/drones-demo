package org.testproject.drones.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class NewMedication {

    @NotBlank(message = "Name should not be null or empty")
    @Pattern(regexp = "^[0-9A-Za-z\\-_]+$",
            message = "Name can consist only from letters, numbers, `-`, `_`")
    private String name;

    @NotNull(message = "Weight should not be null")
    @Min(value = 0, message = "Weight should not be less than 0")
    @Max(value = 500, message = "Weight should not be more than 500")
    private Short weight;

    @NotBlank(message = "Name should not be null or empty")
    @Pattern(regexp = "^[0-9A-Z_]+$",
            message = "Name can consist only from upper case, numbers, `_`")
    private String code;

    private byte[] image;
}
