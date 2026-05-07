package uz.pdp.healthsphere.dto.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.healthsphere.enums.RoleEnum;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterDTO {

    private String username;

    @NotBlank(message = "Parol bo'sh bo'lmasligi kerak")
    private String password;

    @NotBlank(message = "Email bo'sh bo'lmasligi kerak")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "Iltimos, haqiqiy email manzilini kiriting"
    )
    private String email;

    @NotBlank(message = "Telefon raqami bo'sh bo'lmasligi kerak")
    @Pattern(regexp = "^\\+998-\\d{2}-\\d{3}-\\d{2}-\\d{2}$",
            message = "Telefon raqami +998-XX-XXX-XX-XX formatida bo'lishi kerak")
    private String phoneNumber;

    private RoleEnum role;

}
