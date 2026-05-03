package uz.pdp.healthsphere.dto.security;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterDTO {

    private String username;

    private String password;

    @NotBlank
    private String email;

    @NotBlank
    private String phoneNumber;
}
