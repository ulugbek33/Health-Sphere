package uz.pdp.healthsphere.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DoctorPerformanceDTO {

    private String doctor;
    private Long totalAppointments;
    private Long uniquePatients;
    private BigDecimal totalRevenue;

}
