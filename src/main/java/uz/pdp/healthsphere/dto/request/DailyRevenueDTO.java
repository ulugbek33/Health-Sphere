package uz.pdp.healthsphere.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.healthsphere.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DailyRevenueDTO {

    private LocalDateTime dateTime;

    private BigDecimal amount;

    private Long transactionCount;

}
