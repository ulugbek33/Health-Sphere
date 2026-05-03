package uz.pdp.healthsphere.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import uz.pdp.healthsphere.dto.DoctorDTO;
import uz.pdp.healthsphere.dto.DoctorScheduleDTO;
import uz.pdp.healthsphere.entity.Doctor;
import uz.pdp.healthsphere.entity.DoctorSchedule;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface DoctorScheduleMapper {

    @Mapping(target = "doctorId", source = "doctor.id")
    DoctorScheduleDTO toDTO(DoctorSchedule doctorSchedule);

    @Mapping(target = "doctor.id", source = "doctorId")
    DoctorSchedule toEntity(DoctorScheduleDTO doctorScheduleDTO);
}