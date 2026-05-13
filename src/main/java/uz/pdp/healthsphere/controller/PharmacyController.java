package uz.pdp.healthsphere.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.healthsphere.dto.MedicalRecordDTO;
import uz.pdp.healthsphere.dto.MedicineDTO;
import uz.pdp.healthsphere.dto.PatientDTO;
import uz.pdp.healthsphere.dto.PrescriptionDTO;
import uz.pdp.healthsphere.service.PharmacyService;

import java.util.List;

@RestController
@RequestMapping("/api/pharmacy")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PHARMACIST')")
@CrossOrigin(origins = {"http://localhost:5173"
        , "https://healthsphere-kappa.vercel.app"
})
public class PharmacyController {

    private final PharmacyService pharmacyService;

    @GetMapping("/prescriptions/{patientId}")
    public MedicalRecordDTO getPrescription(@PathVariable Long patientId) {

        return pharmacyService.readPrescription(patientId);
    }

    @GetMapping("/medicines")
    public List<MedicineDTO> getMedicines() {
        return pharmacyService.getMedicines();
    }

    @GetMapping("/patients")
    public List<PatientDTO> getPatients() {
        return pharmacyService.getPatients();
    }

    @PostMapping("/medicines")
    public ResponseEntity<MedicineDTO> addMedicine(@RequestBody @Valid MedicineDTO medicineDTO) {
        return ResponseEntity.ok(pharmacyService.addMedicine(medicineDTO));
    }

    @PostMapping("/dispense/{medicalRecordId}")
    public void dispense(@PathVariable Long medicalRecordId) {
        pharmacyService.dispense(medicalRecordId);
    }

}
