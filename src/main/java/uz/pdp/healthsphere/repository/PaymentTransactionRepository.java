package uz.pdp.healthsphere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.pdp.healthsphere.dto.request.DailyRevenueDTO;
import uz.pdp.healthsphere.dto.request.PaymentMethodBreakdownDTO;
import uz.pdp.healthsphere.entity.PaymentTransaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {

    @Query(value = "SELECT SUM(pt.amount) FROM PaymentTransaction pt " +
            "WHERE pt.transactionDate BETWEEN :start AND :end")
    BigDecimal getTotalRevenue(@Param("start") LocalDateTime start,
                               @Param("end") LocalDateTime end);

    @Query(value = "select new uz.pdp.healthsphere.dto.request.PaymentMethodBreakdownDTO(" +
            " pt.paymentMethod,sum(pt.amount),count(pt))" +
            " from PaymentTransaction pt" +
            " where pt.transactionDate between :start and :end" +
            " group by pt.paymentMethod ")
    List<PaymentMethodBreakdownDTO> getRevenueByPaymentMethod(@Param("start") LocalDateTime start,
                                                              @Param("end") LocalDateTime end);


    @Query(value = "select new uz.pdp.healthsphere.dto.request.DailyRevenueDTO(" +
            " pt.transactionDate,sum(pt.amount),count (pt)) " +
            " from PaymentTransaction pt" +
            " where pt.transactionDate between :start and :end" +
            " group by pt.transactionDate")
    List<DailyRevenueDTO> getDailyRevenue(@Param("start") LocalDateTime start,
                                          @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(pt.amount), 0) FROM PaymentTransaction pt WHERE pt.invoice.id = :invoiceId")
    BigDecimal getTotalPaidByInvoice(@Param("invoiceId") Long invoiceId);
}