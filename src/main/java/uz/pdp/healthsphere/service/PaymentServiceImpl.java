package uz.pdp.healthsphere.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.healthsphere.dto.PaymentTransactionDTO;
import uz.pdp.healthsphere.entity.Invoice;
import uz.pdp.healthsphere.entity.Patient;
import uz.pdp.healthsphere.entity.PaymentTransaction;
import uz.pdp.healthsphere.entity.User;
import uz.pdp.healthsphere.enums.InvoiceStatus;
import uz.pdp.healthsphere.exceptions.EntityNotFoundException;
import uz.pdp.healthsphere.exceptions.OutOfStockException;
import uz.pdp.healthsphere.mapper.PaymentMapper;
import uz.pdp.healthsphere.repository.InvoiceRepository;
import uz.pdp.healthsphere.repository.PatientRepository;
import uz.pdp.healthsphere.repository.PaymentTransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final InvoiceRepository invoiceRepository;
    private final PaymentMapper paymentMapper;
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final PatientRepository patientRepository;

    @Override
    @Transactional
    public PaymentTransactionDTO pay(Long invoiceId, PaymentTransactionDTO paymentTransactionDTO) {

        Invoice invoice = invoiceRepository.getByIdOrThrow(invoiceId);

        if (invoice.getStatus() == InvoiceStatus.PAID)
            throw new OutOfStockException("Siz to'lov qilib bo'lgansiz : " + invoice.getStatus(), HttpStatus.BAD_REQUEST);

        BigDecimal alreadyPaid = paymentTransactionRepository.getTotalPaidByInvoice(invoiceId);

        BigDecimal newTotal = alreadyPaid.add(paymentTransactionDTO.getAmount());

        if (newTotal.compareTo(invoice.getTotalAmount()) > 0) {
            throw new OutOfStockException("To'lov summasi umumiy summadan oshib ketdi!", HttpStatus.BAD_REQUEST);
        }

        if (newTotal.compareTo(invoice.getTotalAmount()) == 0) {
            invoice.setStatus(InvoiceStatus.PAID);
        } else
            invoice.setStatus(InvoiceStatus.PARTIALLY_PAID);

        invoiceRepository.save(invoice);

        PaymentTransaction paymentTransaction = paymentMapper.toEntity(paymentTransactionDTO);
        paymentTransaction.setInvoice(invoice);
        paymentTransaction.setTransactionDate(LocalDateTime.now());
        paymentTransactionRepository.save(paymentTransaction);

        return paymentMapper.toDTO(paymentTransaction);
    }

}
