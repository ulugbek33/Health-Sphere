package uz.pdp.healthsphere.dto.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.pdp.healthsphere.entity.User;
import uz.pdp.healthsphere.enums.RoleEnum;
import uz.pdp.healthsphere.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByRole(RoleEnum.ADMIN)) {
            User admin = new User();
            admin.setUsername("super_admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(RoleEnum.ADMIN);
            admin.setActive(true);
            userRepository.save(admin);
            System.out.println(">>> Super Admin yaratildi: super_admin / admin123");
        }
    }
}