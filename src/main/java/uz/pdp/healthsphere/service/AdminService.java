package uz.pdp.healthsphere.service;

import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;
import uz.pdp.healthsphere.dto.UserDTO;
import uz.pdp.healthsphere.dto.security.StaffCreateDTO;
import uz.pdp.healthsphere.enums.ToggleStatus;

import java.util.List;

public interface AdminService {

    UserDTO createStaff(StaffCreateDTO staffCreateDTO);

    UserDTO toggleStatus(Long id, ToggleStatus status);

    List<UserDTO> getAllUsers();

}
