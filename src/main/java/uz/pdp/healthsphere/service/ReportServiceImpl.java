package uz.pdp.healthsphere.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uz.pdp.healthsphere.dto.request.DailyRevenueDTO;
import uz.pdp.healthsphere.dto.request.DoctorPerformanceDTO;
import uz.pdp.healthsphere.dto.request.PaymentMethodBreakdownDTO;
import uz.pdp.healthsphere.dto.request.RevenueReportDTO;
import uz.pdp.healthsphere.enums.InvoiceStatus;
import uz.pdp.healthsphere.enums.StatusEnum;
import uz.pdp.healthsphere.exceptions.PasswordIncorrectException;
import uz.pdp.healthsphere.repository.DoctorRepository;
import uz.pdp.healthsphere.repository.InvoiceRepository;
import uz.pdp.healthsphere.repository.PaymentTransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final PaymentTransactionRepository paymentTransactionRepository;
    private final InvoiceRepository invoiceRepository;
    private final DoctorRepository doctorRepository;

    @Override
    public RevenueReportDTO getRevenueReport(LocalDate startDate, LocalDate endDate) {

        if (startDate.isAfter(endDate)) {
            throw new PasswordIncorrectException("Start date cannot be after end date", HttpStatus.BAD_REQUEST);
        }

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        BigDecimal totalRevenue = paymentTransactionRepository.getTotalRevenue(startDateTime, endDateTime);

        Long paidInvoicesCount = invoiceRepository.countByStatus(InvoiceStatus.PAID);

        BigDecimal unPaidAmount = invoiceRepository.getTotalUnPaidAmount();

        List<PaymentMethodBreakdownDTO> paymentMethod = paymentTransactionRepository.getRevenueByPaymentMethod(startDateTime, endDateTime);

        List<DailyRevenueDTO> dailyRevenue = paymentTransactionRepository.getDailyRevenue(startDateTime, endDateTime);

        return RevenueReportDTO.builder()
                .startDate(startDate)
                .endDate(endDate)
                .totalRevenue(totalRevenue != null ? totalRevenue : BigDecimal.ZERO)
                .paidInvoicesCount(paidInvoicesCount)
                .unpaidAmount(unPaidAmount != null ? unPaidAmount : BigDecimal.ZERO)
                .byPaymentMethod(paymentMethod)
                .byDailyRevenue(dailyRevenue)
                .build();
    }

    @Override
    public List<DoctorPerformanceDTO> performance(StatusEnum status) {

        return doctorRepository.getDoctorPerformance(status);

    }

}
