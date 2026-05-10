package uz.pdp.healthsphere.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.healthsphere.dto.PaymentTransactionDTO;
import uz.pdp.healthsphere.service.PaymentService;

@RestController
@RequestMapping("/api/reception")
@RequiredArgsConstructor
@PreAuthorize("hasRole('RECEPTIONIST')")
@CrossOrigin(origins = {"http://localhost:5173"
        , "https://healthsphere-kappa.vercel.app"
})
public class ReceptionController {

    private final PaymentService paymentService;

    @PostMapping("/invoices/{invoiceId}/pay")
    public PaymentTransactionDTO payment(@PathVariable Long invoiceId,
                                         @Valid @RequestBody PaymentTransactionDTO paymentTransactionDTO) {

        return paymentService.pay(invoiceId,paymentTransactionDTO);
    }

}
