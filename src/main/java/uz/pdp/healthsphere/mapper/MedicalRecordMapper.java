package uz.pdp.healthsphere.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import uz.pdp.healthsphere.dto.AppointmentDTO;
import uz.pdp.healthsphere.dto.MedicalRecordDTO;
import uz.pdp.healthsphere.entity.Appointment;
import uz.pdp.healthsphere.entity.MedicalRecord;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {PrescriptionMapper.class})
public interface MedicalRecordMapper {

    @Mapping(target = "appointmentId", source = "appointment.id")
    @Mapping(target = "prescriptions", source = "prescriptions")
    MedicalRecordDTO toDTO(MedicalRecord medicalRecord);

    @Mapping(target = "appointment", ignore = true)
    @Mapping(target = "prescriptions", ignore = true)
    MedicalRecord toEntity(MedicalRecordDTO medicalRecordDTO);

}