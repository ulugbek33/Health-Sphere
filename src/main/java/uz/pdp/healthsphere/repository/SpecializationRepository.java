package uz.pdp.healthsphere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import uz.pdp.healthsphere.entity.Specialization;
import uz.pdp.healthsphere.exceptions.EntityNotFoundException;

public interface SpecializationRepository extends JpaRepository<Specialization, Long> {

    default Specialization getByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Specialization not found with id : " + id, HttpStatus.NOT_FOUND));
    }

}