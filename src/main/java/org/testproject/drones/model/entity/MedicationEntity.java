package org.testproject.drones.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "medications")
public class MedicationEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "uuid2")
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "weight", nullable = false)
    private Short weight;

    @Column(name = "code", nullable = false, unique = true)
    @NotBlank
    private String code;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "image")
    private byte[] image;

    @ManyToOne
    @JoinColumn(name = "drone_id")
    @JsonBackReference
    private DroneEntity drone;
}
