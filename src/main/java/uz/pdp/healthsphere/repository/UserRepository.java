package uz.pdp.healthsphere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import uz.pdp.healthsphere.entity.User;
import uz.pdp.healthsphere.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    default User getByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id : " + id, HttpStatus.NOT_FOUND));
    }

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<User> findByPhoneNumber(String phoneNumber);
}