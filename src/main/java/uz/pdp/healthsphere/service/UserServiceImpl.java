package uz.pdp.healthsphere.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.healthsphere.dto.UserDTO;
import uz.pdp.healthsphere.entity.User;
import uz.pdp.healthsphere.exceptions.EntityNotFoundException;
import uz.pdp.healthsphere.mapper.UserMapper;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public UserDTO readAll() {

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (currentUser == null)
            throw new EntityNotFoundException("Bunday user ro'yhatda topilmadi : " + currentUser.getUsername(), HttpStatus.NOT_FOUND);

        return userMapper.toDTO(currentUser);
    }

}
