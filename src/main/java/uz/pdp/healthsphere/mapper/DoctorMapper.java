package uz.pdp.healthsphere.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import uz.pdp.healthsphere.dto.DoctorDTO;
import uz.pdp.healthsphere.entity.Doctor;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface DoctorMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "specialtyId", source = "specialization.id")
    DoctorDTO toDTO(Doctor doctor);

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "specialization.id", source = "specialtyId")
    Doctor toEntity(DoctorDTO dto);
}