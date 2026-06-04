package uz.pdp.healthsphere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import uz.pdp.healthsphere.dto.request.DoctorPerformanceDTO;
import uz.pdp.healthsphere.entity.Doctor;
import uz.pdp.healthsphere.entity.User;
import uz.pdp.healthsphere.enums.StatusEnum;
import uz.pdp.healthsphere.exceptions.EntityNotFoundException;
import uz.pdp.healthsphere.projection.DoctorProjection;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    default Doctor getByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with id: " + id, HttpStatus.NOT_FOUND));
    }

    @Query(value = """
            SELECT d.id               as doctorId,
                d.full_name        AS fullName,
                d.experience_years AS experienceYears,
                d.consultation_fee AS consultationFee,
                d.room_number      AS roomNumber,
                s.name             AS specializationName
                FROM doctor d
                JOIN specialization s ON d.specialization_id = s.id
                WHERE (:specialization IS NULL OR s.name = :specialization)
                AND (:maxFee IS NULL OR d.consultation_fee <= :maxFee);
            """, nativeQuery = true)
    List<DoctorProjection> findByDoctorsByFilter(
            @Param("specialization") String specialization,
            @Param("maxFee") BigDecimal maxFee);

    Optional<Doctor> findByUser(User user);

    @Query("SELECT new uz.pdp.healthsphere.dto.request.DoctorPerformanceDTO(" +
            "d.fullName, " +
            "COUNT(a.id), " + // Jami qabullar soni
            "COUNT(DISTINCT a.patient.id), " + // Noyob bemorlar soni
            "COALESCE(SUM(i.totalAmount), 0)) " + // Agar invoice bo'lmasa 0 deb chiqaradi
            "FROM Appointment a " +
            "JOIN a.doctor d " +
            "LEFT JOIN Invoice i ON i.appointment.id = a.id " + // LEFT JOIN juda muhim!
            "WHERE a.status = :status " +
            "GROUP BY d.id, d.fullName")
    List<DoctorPerformanceDTO> getDoctorPerformance(@Param("status") StatusEnum status);

    boolean existsByUserId(Long userId);

    List<Doctor> findAllBySpecializationNameIgnoreCase(String name);
}