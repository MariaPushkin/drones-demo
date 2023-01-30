package org.testproject.drones.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.testproject.drones.model.enums.DroneModel;
import org.testproject.drones.model.enums.DroneState;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "drones")
public class DroneEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "uuid2")
    private String id;

    @Column(name = "serial_number", nullable = false, length = 100, unique = true)
    @NotBlank
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "model", nullable = false)
    private DroneModel model;

    @Column(name = "weight_limit", nullable = false)
    private Short weightLimit;

    @Column(name = "battery_capacity", nullable = false)
    private Short batteryCapacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private DroneState state;

    @OneToMany(mappedBy = "drone", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<MedicationEntity> medications;

    public boolean isAvailableForLoading() {
        return (this.state.equals(DroneState.IDLE) || this.state.equals(DroneState.LOADING));
    }

    public int getRemainingWeightCapacity() {
        return this.weightLimit - this.medications.stream()
                .mapToInt(MedicationEntity::getWeight)
                .sum();
    }
}
