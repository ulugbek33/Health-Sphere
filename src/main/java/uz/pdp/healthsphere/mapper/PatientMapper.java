package uz.pdp.healthsphere.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import uz.pdp.healthsphere.dto.DoctorDTO;
import uz.pdp.healthsphere.dto.PatientDTO;
import uz.pdp.healthsphere.entity.Doctor;
import uz.pdp.healthsphere.entity.Patient;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface PatientMapper {

    @Mapping(target = "userId", source = "user.id")
    PatientDTO toDTO(Patient patient);

    @Mapping(target = "user.id", source = "userId")
    Patient toEntity(PatientDTO patientDTO);

    List<PatientDTO> toDTO(List<Patient> patients);
}