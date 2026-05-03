package uz.pdp.healthsphere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.healthsphere.entity.Specialization;

public interface SpecializationRepository extends JpaRepository<Specialization, Long> {
}