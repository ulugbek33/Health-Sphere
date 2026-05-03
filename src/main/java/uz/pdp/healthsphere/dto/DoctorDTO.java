package uz.pdp.healthsphere.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link uz.pdp.healthsphere.entity.Doctor}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private String fullName;

    private Integer experienceYears;

    private BigDecimal consultationFee;

    private String roomNumber;

    private Long userId;

    private Long specialtyId;

    private List<DoctorScheduleDTO> doctorSchedules;

    private List<AppointmentDTO> appointments;
}