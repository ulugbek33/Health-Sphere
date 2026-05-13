package uz.pdp.healthsphere.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.healthsphere.dto.UserDTO;
import uz.pdp.healthsphere.dto.request.ChangePasswordDTO;
import uz.pdp.healthsphere.dto.request.UserProfileUpdateDTO;
import uz.pdp.healthsphere.service.UserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173"
        , "https://healthsphere-kappa.vercel.app"
})
@PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT', 'RECEPTIONIST', 'PHARMACIST')")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserDTO getCurrentUser() {
        return userService.readAll();
    }

    @PutMapping("/update-profile")
    public ResponseEntity<UserDTO> updateProfile(@RequestBody @Valid UserProfileUpdateDTO userDTO) {
        return ResponseEntity.ok(userService.update(userDTO));
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody @Valid ChangePasswordDTO dto) {
        userService.editPassword(dto);
        return ResponseEntity.ok("Parol muvaffaqiyatli o'zgartirildi");
    }

}
