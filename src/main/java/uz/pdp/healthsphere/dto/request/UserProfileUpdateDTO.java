package uz.pdp.healthsphere.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProfileUpdateDTO {

    private String username;

    private String phoneNumber;

    private String email;

    private String fullName;
}
