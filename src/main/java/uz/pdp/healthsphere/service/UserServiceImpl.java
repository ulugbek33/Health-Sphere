package uz.pdp.healthsphere.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.healthsphere.dto.UserDTO;
import uz.pdp.healthsphere.dto.request.ChangePasswordDTO;
import uz.pdp.healthsphere.dto.request.UserProfileUpdateDTO;
import uz.pdp.healthsphere.entity.User;
import uz.pdp.healthsphere.exceptions.EntityNotFoundException;
import uz.pdp.healthsphere.mapper.UserMapper;
import uz.pdp.healthsphere.repository.UserRepository;
import uz.pdp.healthsphere.service.security.JWTService;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    @Override
    public UserDTO readAll() {

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (currentUser == null)
            throw new EntityNotFoundException("Bunday user ro'yhatda topilmadi : " + currentUser.getUsername(), HttpStatus.NOT_FOUND);

        return userMapper.toDTO(currentUser);
    }

    @Override
    @Transactional
    public UserDTO update(UserProfileUpdateDTO userDTO) {

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean usernameChanged = !currentUser.getUsername().equals(userDTO.getUsername());

        if (!currentUser.getEmail().equals(userDTO.getEmail()) && userRepository.existsByEmail(userDTO.getEmail()))
            throw new RuntimeException("Bu email allaqachon band!");

        if (usernameChanged && userRepository.existsByUsername(userDTO.getUsername())) {
            throw new RuntimeException("Bu username allaqachon band!");
        }

        currentUser.setUsername(userDTO.getUsername());
        currentUser.setPhoneNumber(userDTO.getPhoneNumber());
        currentUser.setEmail(userDTO.getEmail());
        currentUser.setFullName(userDTO.getFullName());

        User saved = userRepository.save(currentUser);
        UserDTO response = userMapper.toDTO(saved);

        if (usernameChanged) {
            String newToken = jwtService.generateToken(saved.getUsername());
            response.setToken(newToken);
        }

        return response;
    }

    @Override
    @Transactional
    public void editPassword(ChangePasswordDTO dto) {

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!passwordEncoder.matches(dto.getOldPassword(), currentUser.getPassword()))
            throw new RuntimeException("Eski parol noto'g'ri!");

        if (!dto.getNewPassword().equals(dto.getConfirmPassword()))
            throw new RuntimeException("Yangi parollar bir-biriga mos kelmadi!");

        currentUser.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(currentUser);

    }

}
