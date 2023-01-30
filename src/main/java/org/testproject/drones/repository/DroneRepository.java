package org.testproject.drones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.testproject.drones.model.entity.DroneEntity;
import org.testproject.drones.model.enums.DroneState;

import java.util.List;

@Repository
public interface DroneRepository extends JpaRepository<DroneEntity, String> {
    List<DroneEntity> findAllByStateIn(List<DroneState> states);
}
