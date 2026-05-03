package uz.pdp.healthsphere.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.healthsphere.dto.MedicalRecordDTO;
import uz.pdp.healthsphere.dto.PrescriptionDTO;
import uz.pdp.healthsphere.service.PharmacyService;

@RestController
@RequestMapping("/api/pharmacy")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PHARMACIST')")
public class PharmacyController {

    private final PharmacyService pharmacyService;

    @GetMapping("/prescriptions/{patientId}")
    public MedicalRecordDTO getPrescription(@PathVariable Long patientId) {

        return pharmacyService.readPrescription(patientId);
    }

    @PostMapping("/dispense/{medicalRecordId}")
    public void dispense(@PathVariable Long medicalRecordId) {
        pharmacyService.dispense(medicalRecordId);
    }

}
