package org.testproject.drones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.testproject.drones.model.entity.DroneEntity;

@Repository
public interface DroneRepository extends JpaRepository<DroneEntity, String> {
}
