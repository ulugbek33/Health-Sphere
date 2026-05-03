package uz.pdp.healthsphere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import uz.pdp.healthsphere.entity.Medicine;
import uz.pdp.healthsphere.exceptions.EntityNotFoundException;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    default Medicine getByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new EntityNotFoundException("Medicine not found with id: " + id, HttpStatus.NOT_FOUND));
    }

    @Modifying
    @Query("update Medicine m set m.stockQuantity=m.stockQuantity-1 where m.id=:id and m.stockQuantity>0")
    int decrementStock(@Param("id") Long id);
}