package uz.pdp.healthsphere.service;

import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;
import uz.pdp.healthsphere.dto.MedicalRecordDTO;
import uz.pdp.healthsphere.dto.MedicineDTO;
import uz.pdp.healthsphere.dto.PatientDTO;
import uz.pdp.healthsphere.dto.PrescriptionDTO;

import java.util.List;

public interface PharmacyService {

    MedicalRecordDTO readPrescription(Long patientId);

    void dispense(Long medicalRecordId);

    List<MedicineDTO> getMedicines();

    MedicineDTO addMedicine(MedicineDTO medicineDTO);

    List<PatientDTO> getPatients();

}
