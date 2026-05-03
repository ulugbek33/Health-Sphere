package uz.pdp.healthsphere.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.healthsphere.enums.PaymentMethod;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PaymentMethodBreakdownDTO {

    private PaymentMethod method;

    private BigDecimal amount;

    private Long transactionCount;

}
