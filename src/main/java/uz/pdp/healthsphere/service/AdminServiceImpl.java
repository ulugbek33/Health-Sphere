package uz.pdp.healthsphere.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.healthsphere.dto.UserDTO;
import uz.pdp.healthsphere.dto.security.StaffCreateDTO;
import uz.pdp.healthsphere.entity.User;
import uz.pdp.healthsphere.exceptions.EntityAlreadyException;
import uz.pdp.healthsphere.mapper.UserMapper;
import uz.pdp.healthsphere.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDTO createStaff(StaffCreateDTO staffCreateDTO) {

        boolean exists = userRepository.existsByUsername(staffCreateDTO.username());
        if (exists)
            throw new EntityAlreadyException("Username is already in use: " + staffCreateDTO.username(), HttpStatus.CONFLICT);

        User staff = new User();
        staff.setUsername(staffCreateDTO.username());
        staff.setPassword(passwordEncoder.encode(staffCreateDTO.password()));
        staff.setEmail(staffCreateDTO.email());
        staff.setPhoneNumber(staffCreateDTO.phoneNumber());
        staff.setRole(staffCreateDTO.role());
        staff.setActive(true);

        userRepository.save(staff);

        return userMapper.toDTO(staff);
    }
}
