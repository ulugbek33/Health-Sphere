package uz.pdp.healthsphere.service;

import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;
import uz.pdp.healthsphere.dto.UserDTO;
import uz.pdp.healthsphere.dto.security.StaffCreateDTO;

public interface AdminService {

    UserDTO createStaff(StaffCreateDTO staffCreateDTO);
}
