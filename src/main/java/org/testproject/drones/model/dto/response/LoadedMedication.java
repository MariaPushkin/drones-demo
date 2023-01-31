package org.testproject.drones.model.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class LoadedMedication {
    private String name;
    private Short weight;
    private String code;
    private byte[] image;
}
