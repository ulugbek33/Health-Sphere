package uz.pdp.healthsphere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import uz.pdp.healthsphere.entity.Appointment;
import uz.pdp.healthsphere.entity.MedicalRecord;
import uz.pdp.healthsphere.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

    default MedicalRecord getByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MedicalRecord not found with id :" + id, HttpStatus.NOT_FOUND));
    }

    Optional<MedicalRecord> findByAppointment(Appointment appointment);

//    @Query(value = """
//            SELECT p.*, m.name, m.price
//            FROM prescription p
//                     JOIN medicine m ON m.id = p.medicine_id
//            WHERE p.medical_record_id = (SELECT mr.id
//                                         FROM medical_record mr
//                                                  JOIN appointment a ON a.id = mr.appointment_id
//                                         WHERE a.patient_id = :patientId
//                                         ORDER BY mr.created_at DESC
//                                         LIMIT 1)
//            """,nativeQuery = true)
//    List<MedicalRecord> filter();

    Optional<MedicalRecord> findTopByAppointmentPatientIdOrderByCreatedAtDesc(Long patientId);
}