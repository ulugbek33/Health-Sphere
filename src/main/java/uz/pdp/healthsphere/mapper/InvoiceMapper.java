package uz.pdp.healthsphere.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import uz.pdp.healthsphere.dto.AppointmentDTO;
import uz.pdp.healthsphere.dto.InvoiceDTO;
import uz.pdp.healthsphere.entity.Appointment;
import uz.pdp.healthsphere.entity.Invoice;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface InvoiceMapper {

    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "appointmentId", source = "appointment.id")
    InvoiceDTO toDTO(Invoice invoice);

    @Mapping(target = "patient.id", source = "patientId")
    @Mapping(target = "appointment.id", source = "appointmentId")
    Invoice toEntity(InvoiceDTO invoiceDTO);

    List<InvoiceDTO> toDTO(List<Invoice> invoices);
}