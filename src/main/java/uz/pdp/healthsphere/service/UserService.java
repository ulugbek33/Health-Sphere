package uz.pdp.healthsphere.service;

import uz.pdp.healthsphere.dto.UserDTO;
import uz.pdp.healthsphere.dto.request.ChangePasswordDTO;
import uz.pdp.healthsphere.dto.request.UserProfileUpdateDTO;

public interface UserService {

    UserDTO readAll();

    UserDTO update(UserProfileUpdateDTO userDTO);

    void editPassword(ChangePasswordDTO dto);
}
