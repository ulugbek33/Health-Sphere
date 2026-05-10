package uz.pdp.healthsphere.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import uz.pdp.healthsphere.dto.SpecializationDTO;
import uz.pdp.healthsphere.dto.UserDTO;
import uz.pdp.healthsphere.dto.security.RegisterDTO;
import uz.pdp.healthsphere.entity.Specialization;
import uz.pdp.healthsphere.entity.User;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface SpecializationMapper {

    SpecializationDTO toDTO(Specialization specialization);

    Specialization toEntity(SpecializationDTO specializationDTO);

    List<SpecializationDTO> toDTO(List<Specialization> specializations);
}