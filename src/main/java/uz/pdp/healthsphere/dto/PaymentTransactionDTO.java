package uz.pdp.healthsphere.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.healthsphere.enums.PaymentMethod;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for {@link uz.pdp.healthsphere.entity.PaymentTransaction}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentTransactionDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private BigDecimal amount;

    private PaymentMethod paymentMethod;

    private LocalDateTime transactionDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long invoiceId;
}