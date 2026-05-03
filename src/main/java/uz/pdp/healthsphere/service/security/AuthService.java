package uz.pdp.healthsphere.service.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import uz.pdp.healthsphere.dto.security.LoginDTO;
import uz.pdp.healthsphere.dto.security.RegisterDTO;
import uz.pdp.healthsphere.dto.security.TokenDTO;
import uz.pdp.healthsphere.entity.User;

public interface AuthService extends UserDetailsService {

    TokenDTO verifyToken(String refreshToken);

    TokenDTO login(LoginDTO loginDTO);

    TokenDTO register(RegisterDTO registerDTO);

    User loadUserByPhoneNumber(String phoneNumber);
}
