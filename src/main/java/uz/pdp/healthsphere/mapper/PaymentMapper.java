package uz.pdp.healthsphere.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import uz.pdp.healthsphere.dto.DoctorDTO;
import uz.pdp.healthsphere.dto.PaymentTransactionDTO;
import uz.pdp.healthsphere.entity.Doctor;
import uz.pdp.healthsphere.entity.PaymentTransaction;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMapper {

    @Mapping(target = "invoiceId", source = "invoice.id")
    PaymentTransactionDTO toDTO(PaymentTransaction paymentTransaction);

    @Mapping(target = "invoice.id", source = "invoiceId")
    PaymentTransaction toEntity(PaymentTransactionDTO paymentTransactionDTO);
}