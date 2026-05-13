package uz.pdp.healthsphere.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import uz.pdp.healthsphere.dto.MedicalRecordDTO;
import uz.pdp.healthsphere.dto.MedicineDTO;
import uz.pdp.healthsphere.entity.MedicalRecord;
import uz.pdp.healthsphere.entity.Medicine;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface MedicineMapper {

    @Mapping(target = "prescriptions", ignore = true)
    MedicineDTO toDTO(Medicine medicine);

    Medicine toEntity(MedicineDTO medicineDTO);

    List<MedicineDTO> toDTO(List<Medicine> medicines);
}