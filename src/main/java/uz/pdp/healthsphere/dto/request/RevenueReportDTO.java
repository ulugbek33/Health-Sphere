package uz.pdp.healthsphere.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RevenueReportDTO {

    private BigDecimal totalRevenue;

    private Long paidInvoicesCount;

    private BigDecimal unpaidAmount;

    private LocalDate startDate;

    private LocalDate endDate;

    private List<PaymentMethodBreakdownDTO> byPaymentMethod;

    private List<DailyRevenueDTO> byDailyRevenue;
}
