package uz.pdp.healthsphere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import uz.pdp.healthsphere.entity.Patient;
import uz.pdp.healthsphere.entity.User;
import uz.pdp.healthsphere.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByUser(User user);

    default Patient getByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + id, HttpStatus.NOT_FOUND));
    }

    boolean existsByUserId(Long userId);
}