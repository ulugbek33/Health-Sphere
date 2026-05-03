package uz.pdp.healthsphere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.healthsphere.entity.MedicalRecord;
import uz.pdp.healthsphere.entity.Prescription;

import java.util.List;
import java.util.Optional;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    List<Prescription> findByMedicalRecordId(Long medicalRecordId);
}