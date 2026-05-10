package uz.pdp.healthsphere.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.healthsphere.dto.security.LoginDTO;
import uz.pdp.healthsphere.dto.security.RegisterDTO;
import uz.pdp.healthsphere.dto.security.TokenDTO;
import uz.pdp.healthsphere.entity.User;
import uz.pdp.healthsphere.enums.RoleEnum;
import uz.pdp.healthsphere.exceptions.EntityAlreadyException;
import uz.pdp.healthsphere.exceptions.EntityNotFoundException;
import uz.pdp.healthsphere.exceptions.PasswordIncorrectException;
import uz.pdp.healthsphere.mapper.UserMapper;
import uz.pdp.healthsphere.repository.UserRepository;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public TokenDTO verifyToken(String refreshToken) {

        String username = jwtService.verifyToken(refreshToken);

        if (username == null)
            throw new EntityNotFoundException("Invalid refresh token : " + refreshToken, HttpStatus.UNAUTHORIZED);

        String accessToken = jwtService.generateToken(username, new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24));

        return new TokenDTO(accessToken, refreshToken);
    }

    @Override
    @Transactional
    public TokenDTO login(LoginDTO loginDTO) {

//        User user = loadUserByPhoneNumber(loginDTO.getPhoneNumber());
        User user = loadUserByUsername(loginDTO.getUsername());

        boolean matches = passwordEncoder.matches(loginDTO.getPassword(), user.getPassword());

        if (!matches)
            throw new PasswordIncorrectException("Invalid username or password : " + loginDTO.getUsername(), HttpStatus.BAD_REQUEST);
//            throw new PasswordIncorrectException("Invalid phone or password : " + loginDTO.getPhoneNumber(), HttpStatus.BAD_REQUEST);

        String accessToken = jwtService.generateToken(loginDTO.getUsername(), new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24));
        String refreshToken = jwtService.generateToken(loginDTO.getUsername(), new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24));

        return new TokenDTO(accessToken, refreshToken);
    }

    @Override
    @Transactional
    public TokenDTO register(RegisterDTO registerDTO) {

        boolean exists = userRepository.existsByUsername(registerDTO.getUsername());
        if (exists)
            throw new EntityAlreadyException("Username is already in use: " + registerDTO.getUsername(), HttpStatus.CONFLICT);

        User user = userMapper.toEntity(registerDTO);

        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setRole(RoleEnum.PATIENT);
        user.setActive(true);

        userRepository.save(user);

        LoginDTO loginDTO = new LoginDTO(registerDTO.getUsername(), registerDTO.getPassword());

        return login(loginDTO);
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty())
            throw new EntityNotFoundException("User not found username: " + username, HttpStatus.NOT_FOUND);

        return user.get();
    }

    @Override
    public User loadUserByPhoneNumber(String phoneNumber) throws UsernameNotFoundException {
        Optional<User> phone = userRepository.findByPhoneNumber(phoneNumber);

        if (phone.isEmpty())
            throw new EntityNotFoundException("User not found phone number: " + phoneNumber, HttpStatus.NOT_FOUND);

        return phone.get();
    }

}
