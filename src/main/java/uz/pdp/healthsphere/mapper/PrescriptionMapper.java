package uz.pdp.healthsphere.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import uz.pdp.healthsphere.dto.AppointmentDTO;
import uz.pdp.healthsphere.dto.PrescriptionDTO;
import uz.pdp.healthsphere.entity.Appointment;
import uz.pdp.healthsphere.entity.Prescription;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface PrescriptionMapper {

//    @Mapping(target = "medicalRecordId", source = "medicalRecord.id")
    @Mapping(target = "medicineId", source = "medicine.id")
    @Mapping(target = "medicineName", source = "medicine.name")
    @Mapping(target = "medicinePrice", source = "medicine.price")
    PrescriptionDTO toDTO(Prescription prescription);

    @Mapping(target = "medicalRecord.id", source = "medicalRecordId")
    @Mapping(target = "medicine.id", source = "medicineId")
    Prescription toEntity(PrescriptionDTO prescriptionDTO);

}