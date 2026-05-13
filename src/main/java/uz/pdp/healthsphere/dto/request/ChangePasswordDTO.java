package uz.pdp.healthsphere.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChangePasswordDTO {

    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

}
