package uz.pdp.healthsphere.service;

import uz.pdp.healthsphere.dto.MedicalRecordDTO;
import uz.pdp.healthsphere.dto.PrescriptionDTO;

public interface PharmacyService {

    MedicalRecordDTO readPrescription(Long patientId);

    void dispense(Long medicalRecordId);

}
