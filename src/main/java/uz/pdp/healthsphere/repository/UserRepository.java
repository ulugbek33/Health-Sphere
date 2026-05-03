package uz.pdp.healthsphere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.healthsphere.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<User> findByPhoneNumber(String phoneNumber);
}