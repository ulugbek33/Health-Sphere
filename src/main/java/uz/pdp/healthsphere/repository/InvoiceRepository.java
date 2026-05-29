package uz.pdp.healthsphere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import uz.pdp.healthsphere.entity.Appointment;
import uz.pdp.healthsphere.entity.Invoice;
import uz.pdp.healthsphere.entity.Patient;
import uz.pdp.healthsphere.enums.InvoiceStatus;
import uz.pdp.healthsphere.exceptions.EntityNotFoundException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findByAppointment(Appointment appointment);

    default Invoice getByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found with id: " + id, HttpStatus.NOT_FOUND));
    }

    Optional<Invoice> findByPatient(Patient patient);

    Long countByStatus(InvoiceStatus status);

    @Query(value = """
            select sum(i.total_amount - (select coalesce(sum(pt.amount), 0) from payment_transaction pt where pt.invoice_id = i.id))
            from Invoice i
            where i.status in ('UNPAID', 'PARTIALLY_PAID')
            """, nativeQuery = true)
    BigDecimal getTotalUnPaidAmount();


    List<Invoice> findAllByPatient(Patient patient);
}