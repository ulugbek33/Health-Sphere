package uz.pdp.healthsphere.service;

import jakarta.validation.Valid;
import uz.pdp.healthsphere.dto.PaymentTransactionDTO;

public interface PaymentService {

    PaymentTransactionDTO pay(Long invoiceId, PaymentTransactionDTO paymentTransactionDTO);

}
