package org.testproject.drones.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.testproject.drones.model.dto.request.NewMedication;
import org.testproject.drones.model.dto.response.LoadedMedication;
import org.testproject.drones.model.entity.MedicationEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MedicationMapper {
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "drone", ignore = true)
    MedicationEntity toMedicationEntity(NewMedication newMedication);
    List<MedicationEntity> toMedicationEntityList(List<NewMedication> newMedications);
    List<LoadedMedication> toLoadedMedicationList(List<MedicationEntity> medications);
}
