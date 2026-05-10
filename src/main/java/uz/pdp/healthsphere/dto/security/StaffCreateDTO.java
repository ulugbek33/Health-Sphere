package uz.pdp.healthsphere.dto.security;

import uz.pdp.healthsphere.enums.RoleEnum;

public record StaffCreateDTO(
        String username,
        String password,
        RoleEnum role, // Admin rolni o'zi tanlaydi
        String email,
        String phoneNumber
) {
}