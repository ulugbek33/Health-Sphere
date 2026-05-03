package uz.pdp.healthsphere.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uz.pdp.healthsphere.dto.security.LoginDTO;
import uz.pdp.healthsphere.dto.security.RegisterDTO;
import uz.pdp.healthsphere.dto.security.TokenDTO;
import uz.pdp.healthsphere.service.security.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/verify")
    public TokenDTO verify(@RequestParam String refreshToken) {
        return authService.verifyToken(refreshToken);
    }

    @PostMapping("/login")
    public TokenDTO login(@RequestBody @Valid LoginDTO loginDTO) {
        return authService.login(loginDTO);
    }

    @PostMapping("/register")
    public TokenDTO register(@RequestBody @Valid RegisterDTO registerDTO) {
        return authService.register(registerDTO);
    }

}
